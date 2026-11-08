package ua.com.foxminded.task.homework.rauf;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import ua.com.foxminded.task.dao.FacultyRepository;
import ua.com.foxminded.task.dao.GroupRepository;
import ua.com.foxminded.task.dao.LectureRepository;
import ua.com.foxminded.task.dao.SubjectRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.service.impl.FacultyServiceImpl;
import ua.com.foxminded.task.service.impl.GroupServiceImpl;
import ua.com.foxminded.task.service.impl.LectureServiceImpl;
import ua.com.foxminded.task.service.impl.SubjectServiceImpl;

@ExtendWith(MockitoExtension.class)
class CreateRejectsExistingId {

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


    @ParameterizedTest
    @ValueSource(ints = {1,8,999})
    @DisplayName("faculty create rejects any dto that already has an id")
    void facultyCreateWithExistingIdThrows(int id){
        FacultyDto dto = new FacultyDto();
        dto.setId(id);

        assertThrows(EntityAlreadyExistsException.class, ()-> facultyService.create(dto));
    }


    @Test
    @DisplayName("group create rejects dto with an id")
    void groupCreateWithExistingIdThrows() {
        GroupDto dto = new GroupDto();
        dto.setId(5);

        assertThrows(EntityAlreadyExistsException.class, () -> groupService.create(dto));
    }

    @Test
    @DisplayName("subject create rejects dto with an id")
    void subjectCreateWithExistingIdThrows() {
        SubjectDto dto = new SubjectDto();
        dto.setId(5);

        assertThrows(EntityAlreadyExistsException.class, () -> subjectService.create(dto));
    }

    @Test
    @DisplayName("lecture create rejects dto with an id")
    void lectureCreateWithExistingIdThrows() {
        LectureDto dto = new LectureDto();
        dto.setId(5);

        assertThrows(EntityAlreadyExistsException.class, () -> lectureService.create(dto));
    }

    @Test
    @DisplayName("exception message starts with the method name")
    void exceptionMessageStartsWithCreate() {
        FacultyDto dto = new FacultyDto();
        dto.setId(5);

        EntityAlreadyExistsException exception = assertThrows(
                EntityAlreadyExistsException.class, () -> facultyService.create(dto));

        assertThat(exception.getMessage(), startsWith("create() facultyDto"));
    }
}