package ua.com.foxminded.task.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ua.com.foxminded.task.validation.validator.PropertyUniqueValidator;

/**
 * The annotated element must have the unique value of properties. This is check by
 * finding in database
 * <p>
 * Supported types are:
 * <ul>
 * <li>{@code AuditoryDto}</li>
 * <li>{@code AuditoryTypeDto}</li>
 * <li>{@code DepartmentDto}</li>
 * <li>{@code FacultyDto}</li>
 * <li>{@code GroupDto}</li>
 * <li>{@code LectureDto}</li>
 * <li>{@code StudentDto}</li>
 * <li>{@code SubjectDto}</li>
 * <li>{@code TeacherDto}</li>
 * </ul>
 * <p>
 *
 * @author Mykola Afanasiev
 */
@Documented
@Constraint(validatedBy = PropertyUniqueValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyValueUnique {

    String message() default "Title not unigue!";

    /**
     * the name of errors field
     */
    String fieldError() default "title";

    /**
     * the name of property field. This property is mandatory.
     * 
     * @return if value is unique and don`t exist in database return true
     */
    String nameProperty();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
