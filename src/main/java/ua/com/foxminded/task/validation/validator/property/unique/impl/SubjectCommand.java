package ua.com.foxminded.task.validation.validator.property.unique.impl;

import java.util.Objects;

import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.service.SubjectService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

public class SubjectCommand implements Command {

    private SubjectService subjectService;

    public SubjectCommand(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public boolean check(String fieldId, String fieldUnique) {
        boolean result = true;
        Subject objectExist = subjectService.findByTitle(fieldUnique);
        if (!Objects.isNull(objectExist)) {
            result = (objectExist.getId() == Integer.valueOf(fieldId));
        }
        return result;
    }

}
