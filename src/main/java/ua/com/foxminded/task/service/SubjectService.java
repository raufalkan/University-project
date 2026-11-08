package ua.com.foxminded.task.service;

import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.dto.SubjectDto;

public interface SubjectService extends ModelService<SubjectDto> {

    public Subject findByTitle(String title);
}
