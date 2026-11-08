package ua.com.foxminded.task.homework.rauf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import ua.com.foxminded.task.dao.LectureRepository;
import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.service.impl.LectureServiceImpl;

@ExtendWith(MockitoExtension.class)
class LectureServiceTest {

    @Mock
    private Logger logger;

    @Mock
    private LectureRepository lectureRepository;

    private LectureServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new LectureServiceImpl(logger, lectureRepository);
    }

    @Test
    @DisplayName("create returns dto with start and end time")
    void createKeepsStartAndEndTime() {
        LectureDto input = new LectureDto();
        input.setNumber("1");
        input.setStartTime(LocalTime.of(8, 30));
        input.setEndTime(LocalTime.of(10, 5));
        when(lectureRepository.saveAndFlush(any(Lecture.class)))
                .thenReturn(lecture(1, "1", LocalTime.of(8, 30), LocalTime.of(10, 5)));

        LectureDto result = service.create(input);

        assertEquals(LocalTime.of(8, 30), result.getStartTime());
        assertEquals(LocalTime.of(10, 5), result.getEndTime());
    }

    @Test
    @DisplayName("findByNumber returns the lecture found by repository")
    void findByNumberReturnsSameLecture() {
        Lecture second = lecture(2, "2", LocalTime.of(10, 15), LocalTime.of(11, 50));
        when(lectureRepository.findByNumber("2")).thenReturn(second);

        assertSame(second, service.findByNumber("2"));
    }

    @Test
    @DisplayName("findByNumber returns null for unknown number")
    void findByNumberUnknownReturnsNull() {
        when(lectureRepository.findByNumber("99")).thenReturn(null);

        assertNull(service.findByNumber("99"));
    }

    @Test
    @DisplayName("lecture ends after it starts")
    void findByIdDtoEndsAfterStart() {
        when(lectureRepository.getOne(2))
                .thenReturn(lecture(2, "2", LocalTime.of(10, 15), LocalTime.of(11, 50)));

        LectureDto result = service.findByIdDto(2);

        assertTrue(result.getEndTime().isAfter(result.getStartTime()));
    }

    @Test
    @DisplayName("findAllDto finishes within one second")
    void findAllDtoIsFast() {
        when(lectureRepository.findAll()).thenReturn(Arrays.asList(
                lecture(1, "1", LocalTime.of(8, 30), LocalTime.of(10, 5)),
                lecture(2, "2", LocalTime.of(10, 15), LocalTime.of(11, 50)),
                lecture(3, "3", LocalTime.of(12, 0), LocalTime.of(13, 35))));

        assertTimeout(Duration.ofSeconds(1), () -> {
            service.findAllDto();
        });
    }

    private Lecture lecture(int id, String number, LocalTime start, LocalTime end) {
        Lecture lecture = new Lecture();
        lecture.setId(id);
        lecture.setNumber(number);
        lecture.setStartTime(start);
        lecture.setEndTime(end);
        return lecture;
    }
}