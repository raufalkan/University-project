package ua.com.foxminded.task.service;

import ua.com.foxminded.task.domain.Faculty;
import ua.com.foxminded.task.domain.dto.FacultyDto;

public interface FacultyService extends ModelService<FacultyDto> {

    public Faculty findByTitle(String title);
}
