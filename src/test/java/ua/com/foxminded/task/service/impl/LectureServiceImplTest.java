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

import ua.com.foxminded.task.dao.LectureRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.domain.repository.LectureModelRepository;
import ua.com.foxminded.task.domain.repository.dto.LectureDtoModelRepository;

class LectureServiceImplTest {

    @Mock
    private Logger logger;
    @Mock
    private LectureRepository lectureRepository;
    @InjectMocks
    private LectureServiceImpl lectureService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenFindById_thenFindEntity() {
        Lecture expectedLecture = LectureModelRepository.getModel1();
        doReturn(expectedLecture).when(lectureRepository).getOne(1);

        Lecture actuallyLecture = lectureService.findById(1);

        verify(lectureRepository, times(1)).getOne(anyInt());
        assertEquals(expectedLecture, actuallyLecture);
    }

    @Test
    void whenFindById_thenFindEntityAndConvertItToDto() {
        Lecture lecture = LectureModelRepository.getModel1();
        LectureDto lectureDtoExpected = LectureDtoModelRepository.getModel1();
        doReturn(lecture).when(lectureRepository).getOne(1);

        LectureDto lectureDtoActually = lectureService.findByIdDto(1);

        verify(lectureRepository, times(1)).getOne(anyInt());
        assertEquals(lectureDtoExpected, lectureDtoActually);
    }

    @Test
    void whenFindByAll_thenFindEntitiesAndConvertItToDto() {
        List<Lecture> lectures = LectureModelRepository.getModels();
        List<LectureDto> lectureDtosExpected = LectureDtoModelRepository.getModels();
        doReturn(lectures).when(lectureRepository).findAll();

        List<LectureDto> lectureDtosActually = lectureService.findAllDto();

        verify(lectureRepository, times(1)).findAll();
        assertEquals(lectureDtosExpected, lectureDtosActually);
    }

    @Test
    void whenCreate_thenInvocCreateDaoClass() {
        LectureDto lectureDto = LectureDtoModelRepository.getModel1();
        Lecture lectureInput = LectureModelRepository.getModel1();
        Lecture lectureExpected = LectureModelRepository.getModel1();

        doReturn(lectureExpected).when(lectureRepository).saveAndFlush(lectureInput);

        LectureDto lectureDtoActually = lectureService.create(lectureDto);

        verify(lectureRepository, times(1)).saveAndFlush(lectureInput);
        assertEquals(lectureDto, lectureDtoActually);
    }

    @Test
    void whenUpdate_thenInvocUpdateDaoClass() {
        LectureDto lectureDto = LectureDtoModelRepository.getModel1();
        lectureDto.setId(1);
        Lecture lecture = LectureModelRepository.getModel1();
        lecture.setId(1);

        doReturn(lecture).when(lectureRepository).saveAndFlush(lecture);
        doReturn(true).when(lectureRepository).existsById(lectureDto.getId());
        doReturn(lecture).when(lectureRepository).getOne(lectureDto.getId());

        LectureDto lectureDtoActually = lectureService.update(lectureDto);

        verify(lectureRepository, times(1)).saveAndFlush(lecture);
        verify(lectureRepository, times(1)).getOne(lectureDto.getId());
        assertEquals(lectureDto, lectureDtoActually);
    }

    @Test
    void whenFindByNumber_thenInvokeMethod() {
        Lecture lecture = LectureModelRepository.getModel1();
        lecture.setId(1);
        String expectedNumber = lecture.getNumber();
        doReturn(lecture).when(lectureRepository).findByNumber(expectedNumber);

        Lecture actuallyLecture = lectureService.findByNumber(expectedNumber);

        verify(lectureRepository, times(1)).findByNumber(expectedNumber);
        assertEquals(expectedNumber, actuallyLecture.getNumber());
    }

    @Test
    void whenCreateRecordEntityWithId_thenThrowException() {
        LectureDto lectureDto = new LectureDto();
        lectureDto.setId(1);

        assertThatThrownBy(() -> lectureService.create(lectureDto))
            .isInstanceOf(EntityAlreadyExistsException.class)
            .hasMessage("create() lectureDto: %s", lectureDto);
    }

    @Test
    void whenCreateRecordWithNotValidEntity_thenThrowException() {
        LectureDto lectureDto = LectureDtoModelRepository.getModel1();
        Lecture lecture = LectureModelRepository.getModel1();

        doThrow(DataIntegrityViolationException.class).when(lectureRepository).saveAndFlush(lecture);

        assertThatThrownBy(() -> lectureService.create(lectureDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("create() lecture: " + lecture);
    }

    @Test
    void whenUpdateRecordWithNotValidEntity_thenThrowException() {
        LectureDto lectureDto = LectureDtoModelRepository.getModel1();
        Lecture lecture = LectureModelRepository.getModel1();

        doReturn(true).when(lectureRepository).existsById(lecture.getId());
        doThrow(DataIntegrityViolationException.class).when(lectureRepository).saveAndFlush(lecture);

        assertThatThrownBy(() -> lectureService.update(lectureDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("update() lecture: " + lecture);
    }

    @Test
    void whenUpdateRecordNotFound_thenThrowException() {
        LectureDto lectureDto = LectureDtoModelRepository.getModel1();
        Lecture lecture = LectureModelRepository.getModel1();

        doReturn(false).when(lectureRepository).existsById(lecture.getId());

        assertThatThrownBy(() -> lectureService.update(lectureDto))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Lecture not exist!");
    }
}
