package ua.com.foxminded.task.homework.rauf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;

import ua.com.foxminded.task.dao.AuditoryTypeRepository;
import ua.com.foxminded.task.dao.FacultyRepository;
import ua.com.foxminded.task.dao.GroupRepository;
import ua.com.foxminded.task.dao.LectureRepository;
import ua.com.foxminded.task.dao.SubjectRepository;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.AuditoryType;
import ua.com.foxminded.task.domain.Faculty;
import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.service.impl.AuditoryTypeServiceImpl;
import ua.com.foxminded.task.service.impl.FacultyServiceImpl;
import ua.com.foxminded.task.service.impl.GroupServiceImpl;
import ua.com.foxminded.task.service.impl.LectureServiceImpl;
import ua.com.foxminded.task.service.impl.SubjectServiceImpl;

@ExtendWith(MockitoExtension.class)
class DataIntegrityWrappingTest {

    @Mock
    private Logger logger;

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private AuditoryTypeRepository auditoryTypeRepository;

    @Mock
    private LectureRepository lectureRepository;

    private FacultyServiceImpl facultyService;
    private GroupServiceImpl groupService;
    private SubjectServiceImpl subjectService;
    private AuditoryTypeServiceImpl auditoryTypeService;
    private LectureServiceImpl lectureService;

    @BeforeEach
    void setUp() {
        facultyService = new FacultyServiceImpl(logger, facultyRepository);
        groupService = new GroupServiceImpl(logger, groupRepository);
        subjectService = new SubjectServiceImpl(logger, subjectRepository);
        auditoryTypeService = new AuditoryTypeServiceImpl(logger, auditoryTypeRepository);
        lectureService = new LectureServiceImpl(logger, lectureRepository);
    }

    @Test
    void facultyCreateThrowsNotValid() {
        FacultyDto dto = new FacultyDto();
        dto.setTitle("Law");
        when(facultyRepository.saveAndFlush(any(Faculty.class)))
                .thenThrow(new DataIntegrityViolationException("db error"));

        assertThrows(EntityNotValidException.class, () -> facultyService.create(dto));
    }

    @Test
    void subjectCreateKeepsOriginalCause() {
        DataIntegrityViolationException original = new DataIntegrityViolationException("db error");
        SubjectDto dto = new SubjectDto();
        dto.setTitle("Math");
        when(subjectRepository.saveAndFlush(any(Subject.class))).thenThrow(original);

        EntityNotValidException exception = assertThrows(
                EntityNotValidException.class, () -> subjectService.create(dto));

        assertSame(original, exception.getCause());
    }

    @Test
    void auditoryTypeCreateMessageStart() {
        AuditoryTypeDto dto = new AuditoryTypeDto();
        dto.setType("Lab");
        when(auditoryTypeRepository.saveAndFlush(any(AuditoryType.class)))
                .thenThrow(new DataIntegrityViolationException("db error"));

        EntityNotValidException exception = assertThrows(
                EntityNotValidException.class, () -> auditoryTypeService.create(dto));

        assertThat(exception.getMessage(), startsWith("create() auditoryType"));
    }

    @Test
    void groupCreateCauseType() {
        GroupDto dto = new GroupDto();
        dto.setTitle("CS-101");
        dto.setYearEntry(Integer.valueOf(2020));
        when(groupRepository.saveAndFlush(any(Group.class)))
                .thenThrow(new DataIntegrityViolationException("db error"));

        EntityNotValidException exception = assertThrows(
                EntityNotValidException.class, () -> groupService.create(dto));

        assertInstanceOf(DataIntegrityViolationException.class, exception.getCause());
    }

    @Test
    void lectureCreateCauseAndMessage() {
        LectureDto dto = new LectureDto();
        dto.setNumber("1");
        when(lectureRepository.saveAndFlush(any(Lecture.class)))
                .thenThrow(new DataIntegrityViolationException("db error"));

        EntityNotValidException exception = assertThrows(
                EntityNotValidException.class, () -> lectureService.create(dto));

        assertAll(
                () -> assertNotNull(exception.getCause()),
                () -> assertTrue(exception.getMessage().startsWith("create() lecture")));
    }
}