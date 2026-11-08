package ua.com.foxminded.task.validation.validator.property.unique.impl;

import java.util.Objects;

import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.service.LectureService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

public class LectureCommand implements Command {

    private LectureService lectureService;

    public LectureCommand(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public boolean check(String fieldId, String fieldUnique) {
        boolean result = true;
        Lecture objectExist = lectureService.findByNumber(fieldUnique);
        if (!Objects.isNull(objectExist)) {
            result = (objectExist.getId() == Integer.valueOf(fieldId));
        }
        return result;
    }

}
