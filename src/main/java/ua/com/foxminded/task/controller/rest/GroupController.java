package ua.com.foxminded.task.controller.rest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.service.GroupService;

@RestController("groupRestController")
@RequestMapping("/api")
@Tag(name = "Group", description = "Group management System")
public class GroupController {

    private GroupService groupService;
    private Logger logger;

    @Autowired
    public GroupController(Logger logger, GroupService groupService) {
        this.logger = logger;
        this.groupService = groupService;
    }

    @GetMapping(path = "/groups", produces = "application/json")
    @Operation(description = "View a list of available groups")
    public List<GroupDto> getGroups() {
        logger.debug("groups()");
        return groupService.findAllDto();
    }

    @GetMapping(path = "/groups/{id}", produces = "application/json")
    @Operation(description = "View a group by id")
    public GroupDto getGroupById(@PathVariable("id") int id) {
        logger.debug("groupById()");
        return groupService.findByIdDto(id);
    }

    @PostMapping(path = "/groups", produces = "application/json")
    @Operation(description = "Create (if id=0) or update a group")
    public GroupDto saveGroup(@Valid @RequestBody GroupDto groupDto) {
        logger.debug("editPost()");
        if (groupDto.getId() != 0) {
            groupDto = groupService.update(groupDto);
        } else {
            groupDto = groupService.create(groupDto);
        }
        return groupDto;
    }
}
