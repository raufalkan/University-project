package ua.com.foxminded.task.validation.validator.property.unique.impl;

import java.util.Objects;

import ua.com.foxminded.task.domain.Faculty;
import ua.com.foxminded.task.service.FacultyService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

public class FacultyCommand implements Command {

    private FacultyService facultyService;

    public FacultyCommand(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @Override
    public boolean check(String fieldId, String fieldUnique) {
        boolean result = true;
        Faculty objectExist = facultyService.findByTitle(fieldUnique);
        if (!Objects.isNull(objectExist)) {
            result = (objectExist.getId() == Integer.valueOf(fieldId));
        }
        return result;
    }

}
