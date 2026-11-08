package ua.com.foxminded.task.service;

import ua.com.foxminded.task.domain.Teacher;
import ua.com.foxminded.task.domain.dto.TeacherDto;

public interface TeacherService extends ModelService<TeacherDto> {

    public Teacher findByIdFees(Integer idFees);
}
