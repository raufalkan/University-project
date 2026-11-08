package ua.com.foxminded.task.service;

import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.domain.dto.GroupDto;

public interface GroupService extends ModelService<GroupDto> {

    public Group findById(int id);

    public Group findByTitle(String title);

}
