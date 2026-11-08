package ua.com.foxminded.task.homework.rauf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import ua.com.foxminded.task.dao.FacultyRepository;
import ua.com.foxminded.task.dao.GroupRepository;
import ua.com.foxminded.task.dao.LectureRepository;
import ua.com.foxminded.task.dao.SubjectRepository;
import ua.com.foxminded.task.domain.Faculty;
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.service.impl.FacultyServiceImpl;
import ua.com.foxminded.task.service.impl.GroupServiceImpl;
import ua.com.foxminded.task.service.impl.LectureServiceImpl;
import ua.com.foxminded.task.service.impl.SubjectServiceImpl;

@ExtendWith(MockitoExtension.class)
public class UpdateNotFoundTest {

    @Mock
    private Logger logger;

    @Mock
    private FacultyRepository facultyRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private LectureRepository lectureRepository;

    private FacultyServiceImpl facultyService;
    private GroupServiceImpl groupService;
    private SubjectServiceImpl subjectService;
    private LectureServiceImpl lectureService;

    @BeforeEach
    void setUp() {
        facultyService = new FacultyServiceImpl(logger, facultyRepository);
        groupService = new GroupServiceImpl(logger, groupRepository);
        subjectService = new SubjectServiceImpl(logger, subjectRepository);
        lectureService = new LectureServiceImpl(logger, lectureRepository);
    }

    @Test
    @DisplayName("faculty update throws when faculty does not exist and saves nothing")
    void facultyUpdateNotFoundThrows() {
        FacultyDto dto = new FacultyDto();
        dto.setId(3);
        when(facultyRepository.existsById(3)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> facultyService.update(dto));
        verify(facultyRepository, never()).saveAndFlush(any(Faculty.class));
    }

    @Test
    @DisplayName("group update throws when group does not exist")
    void groupUpdateNotFoundThrows() {
        GroupDto dto = new GroupDto();
        dto.setId(3);
        when(groupRepository.existsById(3)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> groupService.update(dto));
    }

    @Test
    @DisplayName("subject update throws when subject does not exist")
    void subjectUpdateNotFoundThrows() {
        SubjectDto dto = new SubjectDto();
        dto.setId(3);
        when(subjectRepository.existsById(3)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> subjectService.update(dto));
    }

    @Test
    @DisplayName("lecture update throws when lecture does not exist")
    void lectureUpdateNotFoundThrows() {
        LectureDto dto = new LectureDto();
        dto.setId(3);
        when(lectureRepository.existsById(3)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> lectureService.update(dto));
    }

    @Test
    @DisplayName("faculty update error message is exact")
    void facultyUpdateNotFoundMessage() {
        FacultyDto dto = new FacultyDto();
        dto.setId(3);
        when(facultyRepository.existsById(3)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class, () -> facultyService.update(dto));

        assertEquals("Faculty not exist!", exception.getMessage());
    }
}
