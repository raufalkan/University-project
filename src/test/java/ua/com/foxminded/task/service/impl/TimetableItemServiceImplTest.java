package ua.com.foxminded.task.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static java.util.Arrays.asList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import ua.com.foxminded.task.dao.AuditoryRepository;
import ua.com.foxminded.task.dao.GroupRepository;
import ua.com.foxminded.task.dao.LectureRepository;
import ua.com.foxminded.task.dao.SubjectRepository;
import ua.com.foxminded.task.dao.TeacherRepository;
import ua.com.foxminded.task.dao.TimetableItemRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Auditory;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.domain.TimetableItem;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.dto.TimetableItemDto;
import ua.com.foxminded.task.domain.repository.AuditoryModelRepository;
import ua.com.foxminded.task.domain.repository.GroupModelRepository;
import ua.com.foxminded.task.domain.repository.LectureModelRepository;
import ua.com.foxminded.task.domain.repository.SubjectModelRepository;
import ua.com.foxminded.task.domain.repository.TeacherModelRepository;
import ua.com.foxminded.task.domain.repository.TimetableItemModelRepository;
import ua.com.foxminded.task.domain.repository.dto.GroupDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.TimetableItemDtoModelRepository;

class TimetableItemServiceImplTest {

    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private AuditoryRepository auditoryRepository;
    @Mock
    private LectureRepository lectureRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private TimetableItemRepository timetableItemRepository;
    @Mock
    private Logger logger;
    @InjectMocks
    private TimetableItemServiceImpl timetableItemService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenFindById_thenFindEntityAndConvertItToDto() {
        TimetableItem timetableItem = TimetableItemModelRepository.getModel1();
        TimetableItemDto timetableItemDtoExpected = TimetableItemDtoModelRepository.getModel1();
        doReturn(timetableItem).when(timetableItemRepository).getOne(1);

        TimetableItemDto timetableItemDtoActually = timetableItemService.findByIdDto(1);

        verify(timetableItemRepository, times(1)).getOne(1);
        assertEquals(timetableItemDtoExpected, timetableItemDtoActually);
    }

    @Test
    void whenFindByAll_thenFindEntitiesAndConvertItToDto() {
        List<TimetableItem> timetableItems = TimetableItemModelRepository.getModels();
        List<TimetableItemDto> timetableItemDtosExpected = TimetableItemDtoModelRepository.getModels();
        doReturn(timetableItems).when(timetableItemRepository).findAll();

        List<TimetableItemDto> timetableItemDtosActually = timetableItemService.findAllDto();

        verify(timetableItemRepository, times(1)).findAll();
        assertEquals(timetableItemDtosExpected, timetableItemDtosActually);
    }

    @Test
    void whenCreate_thenInvocCreateDaoClass() {
        TimetableItem timetableItem = getTimetableItem();
        Group group1 = timetableItem.getGroups().get(0);
        Group group2 = timetableItem.getGroups().get(1);

        TimetableItemDto timetableItemDto = getTimetableItemDto();

        doReturn(group1, group2).when(groupRepository).getOne(anyInt());
        doReturn(timetableItem.getSubject()).when(subjectRepository).getOne(1);
        doReturn(timetableItem.getAuditory()).when(auditoryRepository).getOne(1);
        doReturn(timetableItem.getLecture()).when(lectureRepository).getOne(1);
        doReturn(timetableItem.getTeacher()).when(teacherRepository).getOne(1);
        doReturn(timetableItem).when(timetableItemRepository).saveAndFlush(timetableItem);

        TimetableItemDto timetableItemDtoActually = timetableItemService.create(timetableItemDto);

        verify(subjectRepository, times(1)).getOne(1);
        verify(auditoryRepository, times(1)).getOne(1);
        verify(lectureRepository, times(1)).getOne(1);
        verify(teacherRepository, times(1)).getOne(1);
        verify(groupRepository, times(2)).getOne(anyInt());
        verify(timetableItemRepository, times(1)).saveAndFlush(timetableItem);
        assertEquals(timetableItemDto, timetableItemDtoActually);
    }

    @Test
    void whenUpdate_thenInvocUpdateDaoClass() {
        TimetableItem timetableItem = getTimetableItem();
        timetableItem.setId(1);
        Group group1 = timetableItem.getGroups().get(0);
        Group group2 = timetableItem.getGroups().get(1);

        TimetableItemDto timetableItemDto = getTimetableItemDto();
        timetableItemDto.setId(1);

        doReturn(true).when(timetableItemRepository).existsById(timetableItem.getId());
        doReturn(group1, group2).when(groupRepository).getOne(anyInt());
        doReturn(timetableItem).when(timetableItemRepository).getOne(1);
        doReturn(timetableItem.getSubject()).when(subjectRepository).getOne(1);
        doReturn(timetableItem.getAuditory()).when(auditoryRepository).getOne(1);
        doReturn(timetableItem.getLecture()).when(lectureRepository).getOne(1);
        doReturn(timetableItem.getTeacher()).when(teacherRepository).getOne(1);
        doReturn(timetableItem).when(timetableItemRepository).saveAndFlush(timetableItem);

        TimetableItemDto timetableItemDtoActually = timetableItemService.update(timetableItemDto);

        verify(timetableItemRepository, times(1)).existsById(1);
        verify(timetableItemRepository, times(1)).getOne(1);
        verify(subjectRepository, times(1)).getOne(1);
        verify(auditoryRepository, times(1)).getOne(1);
        verify(lectureRepository, times(1)).getOne(1);
        verify(teacherRepository, times(1)).getOne(1);
        verify(groupRepository, times(2)).getOne(anyInt());
        verify(timetableItemRepository, times(1)).saveAndFlush(timetableItem);
        assertEquals(timetableItemDto, timetableItemDtoActually);
    }

    @Test
    void whenCreateRecordEntityWithId_thenThrowException() {
        TimetableItemDto timetableItemDto = getTimetableItemDto();
        timetableItemDto.setId(1);

        assertThatThrownBy(() -> timetableItemService.create(timetableItemDto))
            .isInstanceOf(EntityAlreadyExistsException.class)
            .hasMessage("create() timetableItemDto: %s", timetableItemDto);
    }

    @Test
    void whenCreateRecordWithNotValidEntity_thenThrowException() {
        TimetableItem timetableItem = getTimetableItem();
        Group group1 = timetableItem.getGroups().get(0);
        Group group2 = timetableItem.getGroups().get(1);

        TimetableItemDto timetableItemDto = getTimetableItemDto();

        doReturn(group1, group2).when(groupRepository).getOne(anyInt());
        doReturn(timetableItem.getSubject()).when(subjectRepository).getOne(1);
        doReturn(timetableItem.getAuditory()).when(auditoryRepository).getOne(1);
        doReturn(timetableItem.getLecture()).when(lectureRepository).getOne(1);
        doReturn(timetableItem.getTeacher()).when(teacherRepository).getOne(1);
        doReturn(timetableItem).when(timetableItemRepository).saveAndFlush(timetableItem);
        doThrow(DataIntegrityViolationException.class).when(timetableItemRepository).saveAndFlush(timetableItem);

        assertThatThrownBy(() -> timetableItemService.create(timetableItemDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("create() timetableItem: " + timetableItem);
    }

    @Test
    void whenUpdateRecordWithNotValidEntity_thenThrowException() {
        TimetableItem timetableItem = getTimetableItem();
        timetableItem.setId(1);
        Group group1 = timetableItem.getGroups().get(0);
        Group group2 = timetableItem.getGroups().get(1);

        TimetableItemDto timetableItemDto = getTimetableItemDto();
        timetableItemDto.setId(1);

        doReturn(true).when(timetableItemRepository).existsById(timetableItem.getId());
        doReturn(group1, group2).when(groupRepository).getOne(anyInt());
        doReturn(timetableItem).when(timetableItemRepository).getOne(1);
        doReturn(timetableItem.getSubject()).when(subjectRepository).getOne(1);
        doReturn(timetableItem.getAuditory()).when(auditoryRepository).getOne(1);
        doReturn(timetableItem.getLecture()).when(lectureRepository).getOne(1);
        doReturn(timetableItem.getTeacher()).when(teacherRepository).getOne(1);
        doThrow(DataIntegrityViolationException.class).when(timetableItemRepository).saveAndFlush(timetableItem);

        assertThatThrownBy(() -> timetableItemService.update(timetableItemDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("update() timetableItem: " + timetableItem);
    }

    @Test
    void whenUpdateRecordNotFound_thenThrowException() {
        TimetableItemDto timetableItemDto = TimetableItemDtoModelRepository.getModel1();
        TimetableItem timetableItem = TimetableItemModelRepository.getModel1();
        doReturn(false).when(timetableItemRepository).existsById(timetableItem.getId());

        assertThatThrownBy(() -> timetableItemService.update(timetableItemDto))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Timetable Item not exist!");
    }

    private TimetableItem getTimetableItem() {
        TimetableItem timetableItem = new TimetableItem();
        Subject subject = SubjectModelRepository.getModel1();
        subject.setId(1);
        Auditory auditory = AuditoryModelRepository.getModel1();
        auditory.setId(1);
        Group group1 = GroupModelRepository.getModel1();
        group1.setId(1);
        Group group2 = GroupModelRepository.getModel2();
        group2.setId(2);
        Lecture lecture = LectureModelRepository.getModel1();
        lecture.setId(1);
        Teacher teacher = TeacherModelRepository.getModel1();
        teacher.setId(1);
        timetableItem.setSubject(subject);
        timetableItem.setAuditory(auditory);
        timetableItem.addGroup(group1);
        timetableItem.addGroup(group2);
        timetableItem.setLecture(lecture);
        timetableItem.setDate(LocalDate.of(2020, 06, 25));
        timetableItem.setTeacher(teacher);
        return timetableItem;
    }

    private TimetableItemDto getTimetableItemDto() {
        GroupDto groupDto1 = GroupDtoModelRepository.getModel1();
        groupDto1.setId(1);
        GroupDto groupDto2 = GroupDtoModelRepository.getModel2();
        groupDto2.setId(2);
        TimetableItemDto timetableItemDto = new TimetableItemDto();
        timetableItemDto.setSubjectTitle(SubjectModelRepository.getModel1().getTitle());
        timetableItemDto.setSubjectId(1);
        timetableItemDto.setAuditoryTitle(AuditoryModelRepository.getModel1().getAuditoryNumber());
        timetableItemDto.setAuditoryId(1);
        List<GroupDto> groupDtos = asList(groupDto1, groupDto2);
        timetableItemDto.setGroups(groupDtos);
        timetableItemDto.setLectureTitle(LectureModelRepository.getModel1().getNumber());
        timetableItemDto.setLectureId(1);
        timetableItemDto.setDate(LocalDate.of(2020, 06, 25));
        timetableItemDto.setTeacherTitle(TeacherModelRepository.getModel1().getFirstName());
        timetableItemDto.setTeacherId(1);
        return timetableItemDto;
    }
}
