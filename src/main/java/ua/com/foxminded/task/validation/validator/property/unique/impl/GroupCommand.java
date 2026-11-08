package ua.com.foxminded.task.validation.validator.property.unique.impl;

import java.util.Objects;

import ua.com.foxminded.task.domain.Group;
import ua.com.foxminded.task.service.GroupService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

public class GroupCommand implements Command {

    private GroupService groupService;

    public GroupCommand(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public boolean check(String fieldId, String fieldUnique) {
        boolean result = true;
        Group objectExist = groupService.findByTitle(fieldUnique);
        if (!Objects.isNull(objectExist)) {
            result = (objectExist.getId() == Integer.valueOf(fieldId));
        }
        return result;
    }

}
