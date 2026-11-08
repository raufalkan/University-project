package ua.com.foxminded.task.service;

import ua.com.foxminded.task.domain.Department;
import ua.com.foxminded.task.domain.dto.DepartmentDto;

public interface DepartmentService extends ModelService<DepartmentDto> {

    public Department findByTitle(String title);
}
