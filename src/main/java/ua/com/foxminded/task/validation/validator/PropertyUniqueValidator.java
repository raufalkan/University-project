package ua.com.foxminded.task.validation.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import ua.com.foxminded.task.validation.annotation.PropertyValueUnique;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

public class PropertyUniqueValidator implements ConstraintValidator<PropertyValueUnique, Object> {

    private String message;
    private String fieldError;
    private String nameProperty;

    @Autowired
    @Qualifier("uniqueValidationCommandMap")
    private Map<String, Command> uniqueValidationCommandMap;
    @Autowired
    private Logger logger;

    @Override
    public void initialize(PropertyValueUnique annotation) {
        this.message = annotation.message();
        this.fieldError = annotation.fieldError();
        this.nameProperty = annotation.nameProperty();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean result = true;
        String fieldUnique;
        String fieldId;
        String className = value.getClass().getName();

        try {
            fieldUnique = BeanUtils.getProperty(value, nameProperty);
            fieldId = BeanUtils.getProperty(value, "id");
            if (uniqueValidationCommandMap.containsKey(className)) {
                Command command = uniqueValidationCommandMap.get(className);
                result = command.check(fieldId, fieldUnique);
            } else {
                throw new IllegalAccessException("isValid() Annotation not usabilety for this entity!");
            }

            if (!result) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message).addPropertyNode(fieldError).addConstraintViolation();
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.warn("isValid() [Object:{}], {}", value, e);
        }
        return result;
    }

}
