package ua.com.foxminded.task.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import ua.com.foxminded.task.dao.DepartmentRepository;
import ua.com.foxminded.task.dao.SubjectRepository;
import ua.com.foxminded.task.dao.TeacherRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.domain.dto.TeacherDto;
import ua.com.foxminded.task.domain.repository.SubjectModelRepository;
import ua.com.foxminded.task.domain.repository.TeacherModelRepository;
import ua.com.foxminded.task.domain.repository.dto.SubjectDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.TeacherDtoModelRepository;

class TeacherServiceImplTest {

    @Mock
    private Logger logger;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @InjectMocks
    private TeacherServiceImpl teacherService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenFindById_thenFindEntityAndConvertItToDto() {
        Teacher teacher = TeacherModelRepository.getModel1();
        TeacherDto teacherDtoExpected = TeacherDtoModelRepository.getModel1();
        doReturn(teacher).when(teacherRepository).getOne(1);

        TeacherDto teacherDtoActually = teacherService.findByIdDto(1);

        verify(teacherRepository, times(1)).getOne(anyInt());
        assertEquals(teacherDtoExpected, teacherDtoActually);
    }

    @Test
    void whenFindByAll_thenFindEntitiesAndConvertItToDto() {
        List<Teacher> teachers = TeacherModelRepository.getModels1();
        List<TeacherDto> teacherDtosExpected = TeacherDtoModelRepository.getModels1();
        doReturn(teachers).when(teacherRepository).findAll();

        List<TeacherDto> teacherDtosActually = teacherService.findAllDto();

        verify(teacherRepository, times(1)).findAll();
        assertEquals(teacherDtosExpected, teacherDtosActually);
    }

    @Test
    void whenCreate_thenInvocCreateDaoClass() {
        Teacher teacher = TeacherModelRepository.getModel1();
        Subject subject2 = SubjectModelRepository.getModel2();
        Subject subject3 = SubjectModelRepository.getModel3();
        Subject subject4 = SubjectModelRepository.getModel4();

        TeacherDto teacherDto = TeacherDtoModelRepository.getModel1();

        doReturn(subject2, subject3, subject4).when(subjectRepository).getOne(anyInt());
        doReturn(teacher).when(teacherRepository).saveAndFlush(teacher);
        doReturn(teacher.getDepartment()).when(departmentRepository).getOne(teacher.getDepartment().getId());

        TeacherDto teacherDtoActually = teacherService.create(teacherDto);

        verify(teacherRepository, times(1)).saveAndFlush(teacher);
        verify(departmentRepository, times(1)).getOne(teacher.getDepartment().getId());
        assertThat(teacherDto).isEqualToComparingFieldByField(teacherDtoActually);
    }

    @Test
    void whenUpdate_thenInvocUpdateDaoClass() {
        Teacher teacher = TeacherModelRepository.getModel1();
        teacher.setId(1);
        Subject subject2 = SubjectModelRepository.getModel2();
        Subject subject3 = SubjectModelRepository.getModel3();
        Subject subject4 = SubjectModelRepository.getModel4();

        TeacherDto teacherDto = TeacherDtoModelRepository.getModel1();
        teacherDto.setId(1);

        doReturn(true).when(teacherRepository).existsById(teacherDto.getId());
        doReturn(subject2, subject3, subject4).when(subjectRepository).getOne(anyInt());
        doReturn(teacher).when(teacherRepository).saveAndFlush(teacher);
        doReturn(teacher.getDepartment()).when(departmentRepository).getOne(teacher.getDepartment().getId());
        doReturn(teacher).when(teacherRepository).getOne(teacher.getId());

        TeacherDto teacherDtoActually = teacherService.update(teacherDto);

        verify(teacherRepository, times(1)).saveAndFlush(teacher);
        verify(departmentRepository, times(1)).getOne(teacher.getDepartment().getId());
        assertThat(teacherDto).isEqualToComparingFieldByField(teacherDtoActually);
    }

    @Test
    void whenFindByIdFees_thenInvokeMethod() {
        Teacher expectedTeacher = TeacherModelRepository.getModel1();
        Integer expectedIdFees = expectedTeacher.getIdFees();
        doReturn(expectedTeacher).when(teacherRepository).findByIdFees(expectedIdFees);

        Teacher actuallyStudent = teacherService.findByIdFees(expectedIdFees);

        verify(teacherRepository, times(1)).findByIdFees(expectedIdFees);
        assertEquals(expectedIdFees, actuallyStudent.getIdFees());
    }

    @Test
    void whenCreateRecordEntityWithId_thenThrowException() {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1);

        assertThatThrownBy(() -> teacherService.create(teacherDto))
            .isInstanceOf(EntityAlreadyExistsException.class)
            .hasMessage("create() teacherDto: %s", teacherDto);
    }

    @Test
    void whenCreateRecordWithNotValidEntity_thenThrowException() {
        Teacher teacher = TeacherModelRepository.getModel1();
        Subject subject2 = SubjectModelRepository.getModel2();
        Subject subject3 = SubjectModelRepository.getModel3();
        Subject subject4 = SubjectModelRepository.getModel4();

        TeacherDto teacherDto = TeacherDtoModelRepository.getModel1();
        teacherDto.setSubjects(SubjectDtoModelRepository.getModelsWithId1());

        doReturn(subject2, subject3, subject4).when(subjectRepository).getOne(anyInt());
        doReturn(teacher).when(teacherRepository).saveAndFlush(teacher);
        doReturn(teacher.getDepartment()).when(departmentRepository).getOne(teacher.getDepartment().getId());
        doThrow(DataIntegrityViolationException.class).when(teacherRepository).saveAndFlush(teacher);

        assertThatThrownBy(() -> teacherService.create(teacherDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("create() teacher: " + teacher);
    }

    @Test
    void whenUpdateRecordWithNotValidEntity_thenThrowException() {
        Teacher teacher = TeacherModelRepository.getModel1();
        teacher.setId(1);
        Subject subject2 = SubjectModelRepository.getModel2();
        Subject subject3 = SubjectModelRepository.getModel3();
        Subject subject4 = SubjectModelRepository.getModel4();

        TeacherDto teacherDto = TeacherDtoModelRepository.getModel1();
        teacherDto.setId(1);

        doReturn(true).when(teacherRepository).existsById(teacherDto.getId());
        doReturn(subject2, subject3, subject4).when(subjectRepository).getOne(anyInt());
        doReturn(teacher.getDepartment()).when(departmentRepository).getOne(teacher.getDepartment().getId());
        doReturn(teacher).when(teacherRepository).getOne(teacher.getId());
        doThrow(DataIntegrityViolationException.class).when(teacherRepository).saveAndFlush(teacher);
        doReturn(true).when(teacherRepository).existsById(teacher.getId());

        assertThatThrownBy(() -> teacherService.update(teacherDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("update() teacher: " + teacher);
    }

    @Test
    void whenUpdateRecordNotFpund_thenThrowException() {
        TeacherDto studentDto = TeacherDtoModelRepository.getModel1();
        Teacher student = TeacherModelRepository.getModel1();
        doReturn(false).when(teacherRepository).existsById(student.getId());

        assertThatThrownBy(() -> teacherService.update(studentDto))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Teacher not exist!");
    }
}
