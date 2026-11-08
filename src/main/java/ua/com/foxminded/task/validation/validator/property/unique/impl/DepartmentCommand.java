package ua.com.foxminded.task.validation.validator.property.unique.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ua.com.foxminded.task.domain.Department;
import ua.com.foxminded.task.service.DepartmentService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

@Component
public class DepartmentCommand implements Command {

    @Autowired
    private DepartmentService departmentService;

    public DepartmentCommand(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @Override
    public boolean check(String fieldId, String fieldUnique) {
        boolean result = true;
        Department objectExist = departmentService.findByTitle(fieldUnique);
        if (!Objects.isNull(objectExist)) {
            result = (objectExist.getId() == Integer.valueOf(fieldId));
        }
        return result;
    }

}
