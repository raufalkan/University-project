package ua.com.foxminded.task.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import ua.com.foxminded.task.validation.validator.MaxStudentsInGroupLimitValidator;

@Documented
@Constraint(validatedBy = MaxStudentsInGroupLimitValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MaxStudentsInGroupLimit {

    String message() default "Max participant in group!";

    String fieldName() default "groupId";
    
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
