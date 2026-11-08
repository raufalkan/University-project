package ua.com.foxminded.task.homework.rauf;

// HW2 - decision table tests for MaxStudentsInGroupLimitValidator
// C1 = count >= limit, C2 = isStudentAtGroup

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import ua.com.foxminded.task.domain.dto.StudentDto;
import ua.com.foxminded.task.service.StudentService;
import ua.com.foxminded.task.validation.validator.MaxStudentsInGroupLimitValidator;

@ExtendWith(MockitoExtension.class)
class MaxStudentsInGroupLimitDecisionTableTest {

    private static final int LIMIT = 30;
    private static final long COUNT_BELOW_LIMIT = 20L;
    private static final long COUNT_ABOVE_LIMIT = 31L;

    private static final int STUDENT_ID = 42;
    private static final int GROUP_ID   = 5;

    @Mock private StudentService studentService;
    @Mock private ConstraintValidatorContext context;
    @Mock private ConstraintViolationBuilder violationBuilder;
    @Mock private NodeBuilderCustomizableContext nodeBuilder;

    private MaxStudentsInGroupLimitValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MaxStudentsInGroupLimitValidator();
        ReflectionTestUtils.setField(validator, "studentService", studentService);
        ReflectionTestUtils.setField(validator, "limit", LIMIT);
        ReflectionTestUtils.setField(validator, "message", "Group is full");
        ReflectionTestUtils.setField(validator, "fieldName", "groupId");
    }

    private static StudentDto newStudent() {
        StudentDto dto = new StudentDto();
        dto.setId(STUDENT_ID);
        dto.setGroupId(GROUP_ID);
        return dto;
    }

    private void stubViolationChain() {
        lenient().when(context.buildConstraintViolationWithTemplate("Group is full"))
                 .thenReturn(violationBuilder);
        lenient().when(violationBuilder.addPropertyNode("groupId"))
                 .thenReturn(nodeBuilder);
    }

    @Test
    @DisplayName("R1: count<limit AND student not in group -> admit")
    void rule1_belowLimitAndNotMember_admits() {
        when(studentService.countByGroupId(GROUP_ID)).thenReturn(COUNT_BELOW_LIMIT);
        stubViolationChain();

        boolean result = validator.isValid(newStudent(), context);

        assertTrue(result);
        verify(studentService, never()).existsStudentByIdAndGroupId(anyInt(), anyInt());
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    @DisplayName("R2: count<limit AND student already in group -> admit (same as R1)")
    void rule2_belowLimitAndExistingMember_admits() {
        when(studentService.countByGroupId(GROUP_ID)).thenReturn(COUNT_BELOW_LIMIT);
        stubViolationChain();

        boolean result = validator.isValid(newStudent(), context);

        assertTrue(result);
        verify(studentService, never()).existsStudentByIdAndGroupId(anyInt(), anyInt());
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    @DisplayName("R3: count>=limit AND student already in group -> admit (update path)")
    void rule3_overLimitAndExistingMember_admits() {
        when(studentService.countByGroupId(GROUP_ID)).thenReturn(COUNT_ABOVE_LIMIT);
        when(studentService.existsStudentByIdAndGroupId(STUDENT_ID, GROUP_ID))
                .thenReturn(true);
        stubViolationChain();

        boolean result = validator.isValid(newStudent(), context);

        assertTrue(result);
        verify(studentService).existsStudentByIdAndGroupId(STUDENT_ID, GROUP_ID);
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    @DisplayName("R4: count>=limit AND student not in group -> reject + build violation")
    void rule4_overLimitAndNewStudent_rejectsAndBuildsViolation() {
        when(studentService.countByGroupId(GROUP_ID)).thenReturn(COUNT_ABOVE_LIMIT);
        when(studentService.existsStudentByIdAndGroupId(STUDENT_ID, GROUP_ID))
                .thenReturn(false);
        stubViolationChain();

        boolean result = validator.isValid(newStudent(), context);

        assertFalse(result);
        verify(studentService).existsStudentByIdAndGroupId(STUDENT_ID, GROUP_ID);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Group is full");
        verify(violationBuilder).addPropertyNode("groupId");
        verify(nodeBuilder).addConstraintViolation();
    }
}
