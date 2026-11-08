package ua.com.foxminded.task.validation.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import ua.com.foxminded.task.domain.dto.StudentDto;
import ua.com.foxminded.task.service.StudentService;
import ua.com.foxminded.task.validation.annotation.MaxStudentsInGroupLimit;

public class MaxStudentsInGroupLimitValidator implements ConstraintValidator<MaxStudentsInGroupLimit, StudentDto> {

    private String message;
    private String fieldName;

    @Value("${univer.validation.maxStudentCountInGroup}")
    private int limit;

    @Autowired
    private StudentService studentService;

    @Override
    public void initialize(MaxStudentsInGroupLimit constraintAnnotation) {
        this.message = constraintAnnotation.message();
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(StudentDto studentDto, ConstraintValidatorContext context) {
        boolean result = true;
        int studentId = studentDto.getId();
        int idGroup = studentDto.getGroupId();

        long countStudentsInGroup = studentService.countByGroupId(idGroup);

        if (countStudentsInGroup >= limit) {
            boolean isStudentAtGroup = studentService.existsStudentByIdAndGroupId(studentId, idGroup);

            if (!isStudentAtGroup) {
                result = false;
            }
        }
        if (!result) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message).addPropertyNode(fieldName).addConstraintViolation();
        }
        return result;
    }

}
