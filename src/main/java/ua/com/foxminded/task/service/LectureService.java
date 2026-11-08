package ua.com.foxminded.task.service;

import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.domain.dto.LectureDto;

public interface LectureService extends ModelService<LectureDto> {

    public Lecture findByNumber(String number);
}
