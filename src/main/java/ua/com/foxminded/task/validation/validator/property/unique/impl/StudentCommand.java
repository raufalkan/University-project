package ua.com.foxminded.task.validation.validator.property.unique.impl;

import java.util.Objects;

import ua.com.foxminded.task.domain.Student;
import ua.com.foxminded.task.service.StudentService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

public class StudentCommand implements Command {

    private StudentService studentService;

    public StudentCommand(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public boolean check(String fieldId, String fieldUnique) {
        boolean result = true;
        Student objectExist = studentService.findByIdFees(Integer.valueOf(fieldUnique));
        if (!Objects.isNull(objectExist)) {
            result = (objectExist.getId() == Integer.valueOf(fieldId));
        }
        return result;
    }

}
