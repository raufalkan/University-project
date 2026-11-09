package ua.com.foxminded.task.homework.rauf;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.test.util.ReflectionTestUtils;

import ua.com.foxminded.task.domain.TimetableItem;
import ua.com.foxminded.task.domain.dto.TimetableItemDto;
import ua.com.foxminded.task.service.TimetableItemService;
import ua.com.foxminded.task.validation.validator.AuditoryAndTimeUniqueValidator;

@ExtendWith(MockitoExtension.class)
public class AuditoryAndTimeUniqueBasisPathTest {

    private static final LocalDate LESSON_DAY = LocalDate.of(2026, 11, 10);
    private static final int AUDITORY_ID = 2;
    private static final int LECTURE_ID  = 3;

    @Mock private TimetableItemService timetableItemService;
    @Mock private Logger logger;
    @Mock private ConstraintValidatorContext context;
    @Mock private ConstraintViolationBuilder violationBuilder;
    @Mock private NodeBuilderCustomizableContext nodeBuilder;

    private AuditoryAndTimeUniqueValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AuditoryAndTimeUniqueValidator();
        ReflectionTestUtils.setField(validator, "timetableItemService", timetableItemService);
        ReflectionTestUtils.setField(validator, "logger", logger);
        ReflectionTestUtils.setField(validator, "message", "Auditory busy");
        ReflectionTestUtils.setField(validator, "fieldError", "auditoryId");
    }

    private static TimetableItemDto dto(int id, int auditoryId, int lectureId, LocalDate date) {
        TimetableItemDto d = new TimetableItemDto();
        d.setId(id);
        d.setAuditoryId(auditoryId);
        d.setLectureId(lectureId);
        d.setDate(date);
        return d;
    }

    private static TimetableItem existingItem(int persistedId) {
        TimetableItem item = new TimetableItem();
        item.setId(persistedId);
        return item;
    }

    private void stubViolationChain() {
        lenient().when(context.buildConstraintViolationWithTemplate("Auditory busy"))
                 .thenReturn(violationBuilder);
        lenient().when(violationBuilder.addPropertyNode("auditoryId"))
                 .thenReturn(nodeBuilder);
    }

    @Test
    @DisplayName("Path 1: no existing record for that slot -> returns true")
    void path1_noExistingRecord_returnsTrue() {
        when(timetableItemService.findByAuditoryIdAndLectureIdAndDate(
                anyInt(), anyInt(), any(LocalDate.class))).thenReturn(null);
        stubViolationChain();

        boolean result = validator.isValid(dto(0, AUDITORY_ID, LECTURE_ID, LESSON_DAY), context);

        assertTrue(result);
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    @DisplayName("Path 2: record exists but is the same dto being updated -> returns true")
    void path2_recordExistsAndSameId_returnsTrue() {
        when(timetableItemService.findByAuditoryIdAndLectureIdAndDate(
                anyInt(), anyInt(), any(LocalDate.class))).thenReturn(existingItem(9));
        stubViolationChain();

        boolean result = validator.isValid(dto(9, AUDITORY_ID, LECTURE_ID, LESSON_DAY), context);

        assertTrue(result);
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    @DisplayName("Path 3: another record already uses that slot -> returns false + violation")
    void path3_recordExistsWithDifferentId_returnsFalseAndBuildsViolation() {
        when(timetableItemService.findByAuditoryIdAndLectureIdAndDate(
                anyInt(), anyInt(), any(LocalDate.class))).thenReturn(existingItem(99));
        stubViolationChain();

        boolean result = validator.isValid(dto(9, AUDITORY_ID, LECTURE_ID, LESSON_DAY), context);

        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Auditory busy");
        verify(violationBuilder).addPropertyNode("auditoryId");
        verify(nodeBuilder).addConstraintViolation();
    }

    @Test
    @DisplayName("Path 4: object has no required properties -> catch block, returns true")
    void path4_reflectionFails_returnsTrueAndLogs() {
        boolean result = validator.isValid(new Object(), context);

        assertTrue(result);
        verify(logger).warn(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.any(Object.class),
                org.mockito.ArgumentMatchers.any(Throwable.class));
        verify(timetableItemService, never()).findByAuditoryIdAndLectureIdAndDate(
                anyInt(), anyInt(), any(LocalDate.class));
    }
}
