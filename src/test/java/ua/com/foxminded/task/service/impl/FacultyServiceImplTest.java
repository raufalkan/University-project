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

import ua.com.foxminded.task.dao.FacultyRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Faculty;
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.domain.repository.FacultyModelRepository;
import ua.com.foxminded.task.domain.repository.dto.FacultyDtoModelRepository;

class FacultyServiceImplTest {

    @Mock
    private Logger logger;
    @Mock
    private FacultyRepository facultyRepository;
    @InjectMocks
    private FacultyServiceImpl facultyService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenFindById_thenFindEntity() {
        Faculty expectedFaculty = FacultyModelRepository.getModel1();
        doReturn(expectedFaculty).when(facultyRepository).getOne(1);

        Faculty actuallyFaculty = facultyService.findById(1);

        verify(facultyRepository, times(1)).getOne(anyInt());
        assertEquals(expectedFaculty, actuallyFaculty);
    }

    @Test
    void whenFindById_thenFindEntityAndConvertItToDto() {
        Faculty faculty = FacultyModelRepository.getModel1();
        FacultyDto facultyDtoExpected = FacultyDtoModelRepository.getModel1();
        doReturn(faculty).when(facultyRepository).getOne(1);

        FacultyDto facultyDtoActually = facultyService.findByIdDto(1);

        verify(facultyRepository, times(1)).getOne(anyInt());
        assertEquals(facultyDtoExpected, facultyDtoActually);
    }

    @Test
    void whenFindByAll_thenFindEntitiesAndConvertItToDto() {
        List<Faculty> faculties = FacultyModelRepository.getModels();
        List<FacultyDto> facultyDtosExpected = FacultyDtoModelRepository.getModels();
        doReturn(faculties).when(facultyRepository).findAll();

        List<FacultyDto> facultyDtosActually = facultyService.findAllDto();

        verify(facultyRepository, times(1)).findAll();
        assertEquals(facultyDtosExpected, facultyDtosActually);
    }

    @Test
    void whenCreate_thenInvocCreateDaoClass() {
        FacultyDto facultyDto = FacultyDtoModelRepository.getModel1();
        Faculty facultyInput = FacultyModelRepository.getModel1();
        Faculty facultyExpected = FacultyModelRepository.getModel1();

        doReturn(facultyExpected).when(facultyRepository).saveAndFlush(facultyInput);

        FacultyDto facultyDtoActually = facultyService.create(facultyDto);

        verify(facultyRepository, times(1)).saveAndFlush(facultyInput);
        assertEquals(facultyDto, facultyDtoActually);
    }

    @Test
    void whenUpdate_thenInvocUpdateDaoClass() {
        FacultyDto facultyDto = FacultyDtoModelRepository.getModel1();
        facultyDto.setId(1);
        Faculty faculty = FacultyModelRepository.getModel1();
        faculty.setId(1);

        doReturn(faculty).when(facultyRepository).saveAndFlush(faculty);
        doReturn(true).when(facultyRepository).existsById(facultyDto.getId());
        doReturn(faculty).when(facultyRepository).getOne(facultyDto.getId());

        FacultyDto facultyDtoActually = facultyService.update(facultyDto);

        verify(facultyRepository, times(1)).saveAndFlush(faculty);
        verify(facultyRepository, times(1)).getOne(facultyDto.getId());
        assertEquals(facultyDto, facultyDtoActually);
    }

    @Test
    void whenFindByTitle_thenInvokeMethod() {
        Faculty faculty = FacultyModelRepository.getModel1();
        faculty.setId(1);
        String expectedTitle = faculty.getTitle();
        doReturn(faculty).when(facultyRepository).findByTitle(expectedTitle);

        Faculty actuallyFaculty = facultyService.findByTitle(expectedTitle);

        verify(facultyRepository, times(1)).findByTitle(expectedTitle);
        assertEquals(expectedTitle, actuallyFaculty.getTitle());
    }

    @Test
    void whenCreateRecordEntityWithId_thenThrowException() {
        FacultyDto facultyDto = new FacultyDto();
        facultyDto.setId(1);

        assertThatThrownBy(() -> facultyService.create(facultyDto))
            .isInstanceOf(EntityAlreadyExistsException.class)
            .hasMessage("create() facultyDto: %s", facultyDto);
    }

    @Test
    void whenCreateRecordWithNotValidEntity_thenThrowException() {
        FacultyDto facultyDto = FacultyDtoModelRepository.getModel1();
        Faculty faculty = FacultyModelRepository.getModel1();

        doThrow(DataIntegrityViolationException.class).when(facultyRepository).saveAndFlush(faculty);

        assertThatThrownBy(() -> facultyService.create(facultyDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("create() faculty: " + faculty);
    }

    @Test
    void whenUpdateRecordWithNotValidEntity_thenThrowException() {
        FacultyDto facultyDto = FacultyDtoModelRepository.getModel1();
        Faculty faculty = FacultyModelRepository.getModel1();

        doReturn(true).when(facultyRepository).existsById(faculty.getId());
        doThrow(DataIntegrityViolationException.class).when(facultyRepository).saveAndFlush(faculty);

        assertThatThrownBy(() -> facultyService.update(facultyDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("update() faculty: " + faculty);
    }

    @Test
    void whenUpdateRecordNotFound_thenThrowException() {
        FacultyDto facultyDto = FacultyDtoModelRepository.getModel1();
        Faculty faculty = FacultyModelRepository.getModel1();

        doReturn(false).when(facultyRepository).existsById(faculty.getId());

        assertThatThrownBy(() -> facultyService.update(facultyDto))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Faculty not exist!");
    }
}
