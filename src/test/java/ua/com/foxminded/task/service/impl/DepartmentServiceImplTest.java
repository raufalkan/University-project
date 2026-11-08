package ua.com.foxminded.task.service.impl;

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
import ua.com.foxminded.task.dao.FacultyRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Department;
import ua.com.foxminded.task.domain.Faculty;
import ua.com.foxminded.task.domain.dto.DepartmentDto;
import ua.com.foxminded.task.domain.repository.DepartmentModelRepository;
import ua.com.foxminded.task.domain.repository.FacultyModelRepository;
import ua.com.foxminded.task.domain.repository.dto.DepartmentDtoModelRepository;

class DepartmentServiceImplTest {

    @Mock
    private Logger logger;
    @Mock
    private FacultyRepository facultyRepository;
    @Mock
    private DepartmentRepository departmentRepository;
    @InjectMocks
    private DepartmentServiceImpl departmentService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenFindById_thenFindEntity() {
        Department expectedDepartment = DepartmentModelRepository.getModel1();
        doReturn(expectedDepartment).when(departmentRepository).getOne(1);

        Department actuallyDepartment = departmentService.findById(1);

        verify(departmentRepository, times(1)).getOne(anyInt());
        assertEquals(expectedDepartment, actuallyDepartment);
    }

    @Test
    void whenFindById_thenFindEntityAndConvertItToDto() {
        Department department = DepartmentModelRepository.getModel1();
        DepartmentDto departmentDtoExpected = DepartmentDtoModelRepository.getModel1();
        doReturn(department).when(departmentRepository).getOne(1);

        DepartmentDto departmentDtoActually = departmentService.findByIdDto(1);

        verify(departmentRepository, times(1)).getOne(anyInt());
        assertEquals(departmentDtoExpected, departmentDtoActually);
    }

    @Test
    void whenFindByAll_thenFindEntitiesAndConvertItToDto() {
        List<Department> departments = DepartmentModelRepository.getModels();
        List<DepartmentDto> departmentDtosExpected = DepartmentDtoModelRepository.getModels();
        doReturn(departments).when(departmentRepository).findAll();

        List<DepartmentDto> departmentDtosActually = departmentService.findAllDto();

        verify(departmentRepository, times(1)).findAll();
        assertEquals(departmentDtosExpected, departmentDtosActually);
    }

    @Test
    void whenCreate_thenInvocCreateDaoClass() {
        DepartmentDto departmentDto = DepartmentDtoModelRepository.getModel1();
        departmentDto.setFacultyId(1);
        Department departmentInput = DepartmentModelRepository.getModel1();
        Department departmentExpected = DepartmentModelRepository.getModel1();
        Faculty facultyExpected = FacultyModelRepository.getModel1();

        doReturn(departmentExpected).when(departmentRepository).saveAndFlush(departmentInput);
        doReturn(facultyExpected).when(facultyRepository).getOne(departmentDto.getFacultyId());

        DepartmentDto departmentDtoActually = departmentService.create(departmentDto);

        verify(departmentRepository, times(1)).saveAndFlush(departmentInput);
        verify(facultyRepository, times(1)).getOne(departmentDto.getFacultyId());
        assertEquals(departmentDto, departmentDtoActually);
    }

    @Test
    void whenUpdate_thenInvocUpdateDaoClass() {
        DepartmentDto departmentDto = DepartmentDtoModelRepository.getModel1();
        departmentDto.setId(1);
        Department department = DepartmentModelRepository.getModel1();
        department.setId(1);
        Faculty faculty = FacultyModelRepository.getModel1();
        faculty.setId(1);

        doReturn(department).when(departmentRepository).saveAndFlush(department);
        doReturn(true).when(departmentRepository).existsById(departmentDto.getId());
        doReturn(department).when(departmentRepository).getOne(departmentDto.getId());
        doReturn(faculty).when(facultyRepository).getOne(departmentDto.getFacultyId());

        DepartmentDto departmentDtoActually = departmentService.update(departmentDto);

        verify(departmentRepository, times(1)).saveAndFlush(department);
        verify(departmentRepository, times(1)).getOne(departmentDto.getId());
        assertEquals(departmentDto, departmentDtoActually);
    }

    @Test
    void whenFindByAuditoryNumber_thenInvokeMethod() {
        Department department = DepartmentModelRepository.getModel1();
        department.setId(1);
        String expectedTitle = department.getTitle();
        doReturn(department).when(departmentRepository).findByTitle(expectedTitle);

        Department actuallyDepartment = departmentService.findByTitle(expectedTitle);

        verify(departmentRepository, times(1)).findByTitle(expectedTitle);
        assertEquals(expectedTitle, actuallyDepartment.getTitle());
    }

    @Test
    void whenCreateRecordEntityWithId_thenThrowException() {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setId(1);

        assertThatThrownBy(() -> departmentService.create(departmentDto))
            .isInstanceOf(EntityAlreadyExistsException.class)
            .hasMessage("create() departmentDto: %s", departmentDto);
    }

    @Test
    void whenCreateRecordWithNotValidEntity_thenThrowException() {
        DepartmentDto departmentDto = DepartmentDtoModelRepository.getModel1();
        Department department = DepartmentModelRepository.getModel1();
        Faculty faculty = FacultyModelRepository.getModel1();
        faculty.setId(1);

        doReturn(faculty).when(facultyRepository).getOne(departmentDto.getFacultyId());
        doThrow(DataIntegrityViolationException.class).when(departmentRepository).saveAndFlush(department);

        assertThatThrownBy(() -> departmentService.create(departmentDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("create() department: " + department);
    }

    @Test
    void whenUpdateRecordWithNotValidEntity_thenThrowException() {
        DepartmentDto departmentDto = DepartmentDtoModelRepository.getModel1();
        Department department = DepartmentModelRepository.getModel1();
        Faculty faculty = FacultyModelRepository.getModel1();
        faculty.setId(1);

        doReturn(faculty).when(facultyRepository).getOne(departmentDto.getFacultyId());
        doReturn(true).when(departmentRepository).existsById(department.getId());
        doThrow(DataIntegrityViolationException.class).when(departmentRepository).saveAndFlush(department);

        assertThatThrownBy(() -> departmentService.update(departmentDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("update() department: " + department);
    }

    @Test
    void whenUpdateRecordNotFound_thenThrowException() {
        DepartmentDto departmentDto = DepartmentDtoModelRepository.getModel1();
        Department department = DepartmentModelRepository.getModel1();
        Faculty faculty = FacultyModelRepository.getModel1();
        faculty.setId(1);

        doReturn(faculty).when(facultyRepository).getOne(departmentDto.getFacultyId());
        doReturn(false).when(departmentRepository).existsById(department.getId());

        assertThatThrownBy(() -> departmentService.update(departmentDto))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Department not exist!");
    }
}
