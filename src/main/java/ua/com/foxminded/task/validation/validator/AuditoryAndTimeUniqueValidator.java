package ua.com.foxminded.task.validation.validator;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import ua.com.foxminded.task.domain.TimetableItem;
import ua.com.foxminded.task.service.TimetableItemService;
import ua.com.foxminded.task.validation.annotation.AuditoryAndTimeUnique;

public class AuditoryAndTimeUniqueValidator implements ConstraintValidator<AuditoryAndTimeUnique, Object> {

    private String message;
    private String fieldError;

    @Autowired
    private TimetableItemService timetableItemService;
    @Autowired
    private Logger logger;

    @Override
    public void initialize(AuditoryAndTimeUnique annotation) {
        this.message = annotation.message();
        this.fieldError = annotation.fieldError();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean result = true;
        String fieldAuditoryId;
        String fieldLectureId;
        String fieldDate;
        String fieldId;

        try {
            fieldAuditoryId = BeanUtils.getProperty(value, "auditoryId");
            fieldLectureId = BeanUtils.getProperty(value, "lectureId");
            fieldDate = BeanUtils.getProperty(value, "date");
            fieldId = BeanUtils.getProperty(value, "id");

            Integer auditoryId = Integer.valueOf(fieldAuditoryId);
            Integer lectureId = Integer.valueOf(fieldLectureId);
            LocalDate date = LocalDate.parse(fieldDate);

            TimetableItem objectExist = timetableItemService.findByAuditoryIdAndLectureIdAndDate(auditoryId, lectureId, date);

            if (!Objects.isNull(objectExist)) {
                result = (objectExist.getId() == Integer.valueOf(fieldId));
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
