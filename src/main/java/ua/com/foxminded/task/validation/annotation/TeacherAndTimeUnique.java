package ua.com.foxminded.task.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ua.com.foxminded.task.validation.validator.TeacherAndTimeUniqueValidator;

/**
 * The annotated element must have the unique next value: teacher, date and number of lection. This is check by
 * finding in database
 * <p>
 * Supported types are:
 * <ul>
 * <li>{@code TimetableItemDto}</li>
 * </ul>
 * <p>
 *
 * @author Mykola Afanasiev
 */
@Documented
@Constraint(validatedBy = TeacherAndTimeUniqueValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TeacherAndTimeUnique {

    String message() default "Teacher will be busy at the time!";

    /**
     * the name of errors field for html
     */
    String fieldError() default "teacherTitle";
    
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
