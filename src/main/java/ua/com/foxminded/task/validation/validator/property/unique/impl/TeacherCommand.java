package ua.com.foxminded.task.validation.validator.property.unique.impl;

import java.util.Objects;

import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.service.TeacherService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

public class TeacherCommand implements Command {

    private TeacherService teacherService;

    public TeacherCommand(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    public boolean check(String fieldId, String fieldUnique) {
        boolean result = true;
        Teacher objectExist = teacherService.findByIdFees(Integer.valueOf(fieldUnique));
        if (!Objects.isNull(objectExist)) {
            result = (objectExist.getId() == Integer.valueOf(fieldId));
        }
        return result;
    }

}
