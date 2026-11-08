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
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.service.SubjectService;

@Controller
public class SubjectController {

    private static final String PATH_HTML_SUBJECT = "subject/subject";
    private static final String PATH_HTML_SUBJECTS = "subject/subjects";
    private static final String PATH_HTML_SUBJECT_EDIT = "subject/subject_edit";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String ATTRIBUTE_HTML_SUBJECT = "subjectDto";
    private static final String ATTRIBUTE_HTML_SUBJECTS = "subjects";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private SubjectService subjectService;
    private Logger logger;

    @Autowired
    public SubjectController(Logger logger, SubjectService subjectService) {
        this.logger = logger;
        this.subjectService = subjectService;
    }

    @GetMapping("/subjects")
    public String getEntities(Model model) {
        logger.debug("getEntities()");
        String errorMessage = null;
        List<SubjectDto> subjects = null;
        try {
            subjects = subjectService.findAllDto();
        } catch (EntityNotFoundException e) {
            errorMessage = "Problem with finding subject";
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Subject");
        model.addAttribute(ATTRIBUTE_HTML_SUBJECTS, subjects);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_SUBJECTS;
    }

    @GetMapping("/subject")
    public String getEntityById(@RequestParam("id") String idString, Model model) {
        logger.debug("getEntityById()");
        String errorMessage = null;
        SubjectDto subjectDto = null;

        int id = 0;
        try {
            if (checkId(idString)) {
                id = Integer.valueOf(idString);
                subjectDto = subjectService.findByIdDto(id);
            } else {
                errorMessage = "You id is blank";
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Subject by id#" + id + " not found!";
        } catch (NumberFormatException e) {
            errorMessage = "Subject id# must be numeric!";
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Subject");
        model.addAttribute(ATTRIBUTE_HTML_SUBJECT, subjectDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_SUBJECT;
    }

    @GetMapping("/subject_edit")
    public String editGetEntity(@RequestParam(name = "id", required = false) String id, Model model) {
        logger.debug("editGetEntity(), id: {}", id);
        String errorMessage = null;
        SubjectDto subjectDto = new SubjectDto();
        try {
            if (checkId(id)) {
                subjectDto = subjectService.findByIdDto(Integer.valueOf(id));
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Problem with finding subject";
        }
        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Subject edit");
        model.addAttribute(ATTRIBUTE_HTML_SUBJECT, subjectDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_SUBJECT_EDIT;
    }

    @PostMapping("/subject_edit")
    public String editPostEntity(@Valid @ModelAttribute("subjectDto") SubjectDto subjectDto, 
                                 BindingResult bindingResult, 
                                 Model model) {
        logger.debug("editPostEntity()");
        String errorMessage = null;
        String successMessage = null;
        String path = PATH_HTML_SUBJECT;

        if (!bindingResult.hasErrors()) {

            try {
                if (subjectDto.getId() != 0) {
                    subjectDto = subjectService.update(subjectDto);
                    successMessage = "Record subject was updated!";
                } else {
                    subjectDto = subjectService.create(subjectDto);
                    successMessage = "Record subject was created!";
                }
            } catch (EntityAlreadyExistsException e) {
                errorMessage = "Record subject was not created! The record already exists!";
                path = PATH_HTML_SUBJECT_EDIT;
            } catch (EntityNotFoundException e) {
                errorMessage = "Subject " + subjectDto + " not found!";
                path = PATH_HTML_SUBJECT_EDIT;
            } catch (EntityNotValidException e) {
                errorMessage = "Record subject was not updated/created! The data is not valid!";
                path = PATH_HTML_SUBJECT_EDIT;
            }
        } else {
            errorMessage = "You enter incorrect data!";
            path = PATH_HTML_SUBJECT_EDIT;
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Subject edit");
        model.addAttribute(ATTRIBUTE_HTML_SUBJECT, subjectDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        model.addAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, successMessage);
        return path;
    }

    private boolean checkId(String id) {
        return StringUtils.isNoneBlank(id);
    }

}
