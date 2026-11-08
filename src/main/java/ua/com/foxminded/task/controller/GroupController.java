package ua.com.foxminded.task.controller;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.service.GroupService;

@Controller
public class GroupController {

    private static final String PATH_HTML_GROUP = "group/group";
    private static final String PATH_HTML_GROUPS = "group/groups";
    private static final String PATH_HTML_GROUP_EDIT = "group/group_edit";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String ATTRIBUTE_HTML_GROUP = "groupDto";
    private static final String ATTRIBUTE_HTML_GROUPS = "groups";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private GroupService groupService;
    private Logger logger;

    @Autowired
    public GroupController(Logger logger, GroupService groupService) {
        this.logger = logger;
        this.groupService = groupService;
    }

    @GetMapping("/groups")
    public String groups(Model model) {
        logger.debug("groups()");
        String errorMessage = null;
        List<GroupDto> groups = null;
        try {
            groups = groupService.findAllDto();
        } catch (EntityNotFoundException e) {
            errorMessage = "Problem with finding group";
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Groups");
        model.addAttribute(ATTRIBUTE_HTML_GROUPS, groups);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_GROUPS;
    }

    @GetMapping("/group")
    public String group(@RequestParam("id") String idString, Model model) {
        logger.debug("group()");
        String errorMessage = null;
        GroupDto groupDto = null;

        int id = 0;
        try {
            if (checkId(idString)) {
                id = Integer.valueOf(idString);
                groupDto = groupService.findByIdDto(id);
            } else {
                errorMessage = "You id is blank";
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Group by id#" + id + " not found!";
        } catch (NumberFormatException e) {
            errorMessage = "Group id# must be numeric!";
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Group");
        model.addAttribute(ATTRIBUTE_HTML_GROUP, groupDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_GROUP;
    }

    @GetMapping("/group_edit")
    public String editGet(@RequestParam(name = "id", required = false) String id, Model model) {
        logger.debug("editGet(), id: {}", id);
        String errorMessage = null;
        GroupDto groupDto = new GroupDto();
        try {
            if (checkId(id)) {
                groupDto = groupService.findByIdDto(Integer.valueOf(id));
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Problem with finding group";
        }
        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Group edit");
        model.addAttribute(ATTRIBUTE_HTML_GROUP, groupDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_GROUP_EDIT;
    }

    @PostMapping("/group_edit")
    public String editPost(@Valid @ModelAttribute("groupDto") GroupDto groupDto, 
                           BindingResult bindingResult, 
                           Model model) {
        logger.debug("editPost()");
        String errorMessage = null;
        String successMessage = null;
        String path = PATH_HTML_GROUP;

        if (!bindingResult.hasErrors()) {

            try {
                if (groupDto.getId() != 0) {
                    groupDto = groupService.update(groupDto);
                    successMessage = "Record group was updated!";
                } else {
                    groupDto = groupService.create(groupDto);
                    successMessage = "Record group was created!";
                }
            } catch (EntityAlreadyExistsException e) {
                errorMessage = "Record group was not created! The record already exists!";
                path = PATH_HTML_GROUP_EDIT;
            } catch (EntityNotFoundException e) {
                errorMessage = "Group " + groupDto + " not found!";
                path = PATH_HTML_GROUP_EDIT;
            } catch (EntityNotValidException e) {
                errorMessage = "Record group was not updated/created! The data is not valid!";
                path = PATH_HTML_GROUP_EDIT;
            }
        } else {
            errorMessage = "You enter incorrect data!";
            path = PATH_HTML_GROUP_EDIT;
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Group edit");
        model.addAttribute(ATTRIBUTE_HTML_GROUP, groupDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        model.addAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, successMessage);
        return path;
    }

    private boolean checkId(String id) {
        return StringUtils.isNoneBlank(id);
    }

}
