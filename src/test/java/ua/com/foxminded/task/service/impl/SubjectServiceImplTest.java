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

import ua.com.foxminded.task.dao.SubjectRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.domain.repository.SubjectModelRepository;
import ua.com.foxminded.task.domain.repository.dto.SubjectDtoModelRepository;

class SubjectServiceImplTest {

    @Mock
    private Logger logger;
    @Mock
    private SubjectRepository subjectRepository;
    @InjectMocks
    private SubjectServiceImpl subjectService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenFindById_thenFindEntity() {
        Subject expectedSubject = SubjectModelRepository.getModel1();
        doReturn(expectedSubject).when(subjectRepository).getOne(1);

        Subject actuallySubject = subjectService.findById(1);

        verify(subjectRepository, times(1)).getOne(anyInt());
        assertEquals(expectedSubject, actuallySubject);
    }

    @Test
    void whenFindById_thenFindEntityAndConvertItToDto() {
        Subject subject = SubjectModelRepository.getModel1();
        SubjectDto subjectDtoExpected = SubjectDtoModelRepository.getModel1();
        doReturn(subject).when(subjectRepository).getOne(1);

        SubjectDto subjectDtoActually = subjectService.findByIdDto(1);

        verify(subjectRepository, times(1)).getOne(anyInt());
        assertEquals(subjectDtoExpected, subjectDtoActually);
    }

    @Test
    void whenFindByAll_thenFindEntitiesAndConvertItToDto() {
        List<Subject> subjects = SubjectModelRepository.getModels1();
        List<SubjectDto> subjectDtosExpected = SubjectDtoModelRepository.getModels1();
        doReturn(subjects).when(subjectRepository).findAll();

        List<SubjectDto> subjectDtosActually = subjectService.findAllDto();

        verify(subjectRepository, times(1)).findAll();
        assertEquals(subjectDtosExpected, subjectDtosActually);
    }

    @Test
    void whenCreate_thenInvocCreateDaoClass() {
        SubjectDto subjectDto = SubjectDtoModelRepository.getModel1();
        Subject subjectInput = SubjectModelRepository.getModel1();
        Subject subjectExpected = SubjectModelRepository.getModel1();

        doReturn(subjectExpected).when(subjectRepository).saveAndFlush(subjectInput);

        SubjectDto subjectDtoActually = subjectService.create(subjectDto);

        verify(subjectRepository, times(1)).saveAndFlush(subjectInput);
        assertEquals(subjectDto, subjectDtoActually);
    }

    @Test
    void whenUpdate_thenInvocUpdateDaoClass() {
        SubjectDto subjectDto = SubjectDtoModelRepository.getModel1();
        subjectDto.setId(1);
        Subject subject = SubjectModelRepository.getModel1();
        subject.setId(1);

        doReturn(subject).when(subjectRepository).saveAndFlush(subject);
        doReturn(true).when(subjectRepository).existsById(subjectDto.getId());
        doReturn(subject).when(subjectRepository).getOne(subjectDto.getId());

        SubjectDto subjectDtoActually = subjectService.update(subjectDto);

        verify(subjectRepository, times(1)).saveAndFlush(subject);
        verify(subjectRepository, times(1)).getOne(subjectDto.getId());
        assertEquals(subjectDto, subjectDtoActually);
    }

    @Test
    void whenFindByTitle_thenInvokeMethod() {
        Subject subject = SubjectModelRepository.getModel1();
        subject.setId(1);
        String expectedTitle = subject.getTitle();
        doReturn(subject).when(subjectRepository).findByTitle(expectedTitle);

        Subject actuallySubject = subjectService.findByTitle(expectedTitle);

        verify(subjectRepository, times(1)).findByTitle(expectedTitle);
        assertEquals(expectedTitle, actuallySubject.getTitle());
    }

    @Test
    void whenCreateRecordEntityWithId_thenThrowException() {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(1);

        assertThatThrownBy(() -> subjectService.create(subjectDto))
            .isInstanceOf(EntityAlreadyExistsException.class)
            .hasMessage("create() subjectDto: %s", subjectDto);
    }

    @Test
    void whenCreateRecordWithNotValidEntity_thenThrowException() {
        SubjectDto subjectDto = SubjectDtoModelRepository.getModel1();
        Subject subject = SubjectModelRepository.getModel1();

        doThrow(DataIntegrityViolationException.class).when(subjectRepository).saveAndFlush(subject);

        assertThatThrownBy(() -> subjectService.create(subjectDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("create() subject: " + subject);
    }

    @Test
    void whenUpdateRecordWithNotValidEntity_thenThrowException() {
        SubjectDto subjectDto = SubjectDtoModelRepository.getModel1();
        Subject subject = SubjectModelRepository.getModel1();

        doReturn(true).when(subjectRepository).existsById(subject.getId());
        doThrow(DataIntegrityViolationException.class).when(subjectRepository).saveAndFlush(subject);

        assertThatThrownBy(() -> subjectService.update(subjectDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("update() subject: " + subject);
    }

    @Test
    void whenUpdateRecordNotFound_thenThrowException() {
        SubjectDto subjectDto = SubjectDtoModelRepository.getModel1();
        Subject subject = SubjectModelRepository.getModel1();

        doReturn(false).when(subjectRepository).existsById(subject.getId());

        assertThatThrownBy(() -> subjectService.update(subjectDto))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Subject not exist!");
    }
}
