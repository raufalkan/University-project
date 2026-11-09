package ua.com.foxminded.task.homework.rauf;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
public class MaxStudentsInGroupLimitBasisPathTest {

    private static final int LIMIT = 30;

    @Mock
    private StudentService studentService;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintViolationBuilder violationBuilder;

    @Mock
    private NodeBuilderCustomizableContext nodeBuilder;

    private MaxStudentsInGroupLimitValidator validator;

    @BeforeEach
    void setUp() {
        validator = new MaxStudentsInGroupLimitValidator();
        ReflectionTestUtils.setField(validator, "studentService", studentService);
        ReflectionTestUtils.setField(validator, "limit", LIMIT);
        ReflectionTestUtils.setField(validator, "message", "Group is full");
        ReflectionTestUtils.setField(validator, "fieldName", "groupId");
    }

    private static StudentDto dto(int studentId, int groupId) {
        StudentDto d = new StudentDto();
        d.setId(studentId);
        d.setGroupId(groupId);
        return d;
    }

    @Test
    @DisplayName("Path 1: count < limit -> returns true, no violation built")
    void path1_countBelowLimit_returnsTrue() {
        when(studentService.countByGroupId(5)).thenReturn(10L);

        boolean result = validator.isValid(dto(1, 5), context);

        assertTrue(result);
        verify(studentService, never()).existsStudentByIdAndGroupId(anyInt(), anyInt());
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    @DisplayName("Path 2: count>=limit but student already in group -> returns true")
    void path2_countAtLimitAndStudentAlreadyInGroup_returnsTrue() {
        when(studentService.countByGroupId(5)).thenReturn(30L);
        when(studentService.existsStudentByIdAndGroupId(7, 5)).thenReturn(true);

        boolean result = validator.isValid(dto(7, 5), context);

        assertTrue(result);
        verify(context, never()).disableDefaultConstraintViolation();
    }

    @Test
    @DisplayName("Path 3: count>=limit AND student not in group -> returns false, violation built")
    void path3_countAboveLimitAndNewStudent_returnsFalseAndBuildsViolation() {
        when(studentService.countByGroupId(5)).thenReturn(31L);
        when(studentService.existsStudentByIdAndGroupId(9, 5)).thenReturn(false);
        when(context.buildConstraintViolationWithTemplate("Group is full"))
                .thenReturn(violationBuilder);
        when(violationBuilder.addPropertyNode("groupId")).thenReturn(nodeBuilder);

        boolean result = validator.isValid(dto(9, 5), context);

        assertFalse(result);
        verify(context).disableDefaultConstraintViolation();
        verify(context).buildConstraintViolationWithTemplate("Group is full");
        verify(violationBuilder).addPropertyNode("groupId");
        verify(nodeBuilder).addConstraintViolation();
    }

    @Test
    @Disabled("Infeasible basis path: result cannot be false when D1=false.")
    @DisplayName("Path 4 (infeasible): D1=F then D3=T -- documented, cannot occur")
    void path4_infeasiblePath_documented() {
    }
}
