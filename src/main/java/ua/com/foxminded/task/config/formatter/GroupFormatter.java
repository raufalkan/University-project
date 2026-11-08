package ua.com.foxminded.task.config.formatter;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import ua.com.foxminded.task.domain.dto.GroupDto;

@Component
public class GroupFormatter implements Formatter<GroupDto> {

    @Override
    public GroupDto parse(String id, Locale locale) throws ParseException {
        GroupDto groupDto = new GroupDto();
        groupDto.setId(Integer.parseInt(id));
        return groupDto;
    }

    @Override
    public String print(GroupDto group, Locale locale) {
        return String.valueOf(group.getId());
    }

}
