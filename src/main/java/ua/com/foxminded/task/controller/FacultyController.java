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
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.service.FacultyService;

@Controller
public class FacultyController {

    private static final String PATH_HTML_FACULTY = "faculty/faculty";
    private static final String PATH_HTML_FACULTIES = "faculty/faculties";
    private static final String PATH_HTML_FACULTY_EDIT = "faculty/faculty_edit";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String ATTRIBUTE_HTML_FACULTY = "facultyDto";
    private static final String ATTRIBUTE_HTML_FACULTIES = "faculties";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private FacultyService facultyService;
    private Logger logger;

    @Autowired
    public FacultyController(Logger logger, FacultyService facultyService) {
        this.logger = logger;
        this.facultyService = facultyService;
    }

    @GetMapping("/faculties")
    public String getEntities(Model model) {
        logger.debug("getEntities()");
        String errorMessage = null;
        List<FacultyDto> faculties = null;
        try {
            faculties = facultyService.findAllDto();
        } catch (EntityNotFoundException e) {
            errorMessage = "Problem with finding faculties";
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Faculties");
        model.addAttribute(ATTRIBUTE_HTML_FACULTIES, faculties);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_FACULTIES;
    }

    @GetMapping("/faculty")
    public String getEntityById(@RequestParam("id") String idString, Model model) {
        logger.debug("getEntityById()");
        String errorMessage = null;
        FacultyDto facultyDto = null;

        int id = 0;
        try {
            if (checkId(idString)) {
                id = Integer.valueOf(idString);
                facultyDto = facultyService.findByIdDto(id);
            } else {
                errorMessage = "You id is blank";
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Faculty by id#" + id + " not found!";
        } catch (NumberFormatException e) {
            errorMessage = "Faculty id# must be numeric!";
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Faculty");
        model.addAttribute(ATTRIBUTE_HTML_FACULTY, facultyDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_FACULTY;
    }

    @GetMapping("/faculty_edit")
    public String editGetEntity(@RequestParam(name = "id", required = false) String id, Model model) {
        logger.debug("editGetEntity(), id: {}", id);
        String errorMessage = null;
        FacultyDto facultyDto = new FacultyDto();
        try {
            if (checkId(id)) {
                facultyDto = facultyService.findByIdDto(Integer.valueOf(id));
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Problem with finding faculty";
        }
        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Faculty edit");
        model.addAttribute(ATTRIBUTE_HTML_FACULTY, facultyDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_FACULTY_EDIT;
    }

    @PostMapping("/faculty_edit")
    public String editPostEntity(@Valid @ModelAttribute("facultyDto") FacultyDto facultyDto, 
                                 BindingResult bindingResult, 
                                 Model model) {
        logger.debug("editPostEntity()");
        String errorMessage = null;
        String successMessage = null;
        String path = PATH_HTML_FACULTY;

        if (!bindingResult.hasErrors()) {

            try {
                if (facultyDto.getId() != 0) {
                    facultyDto = facultyService.update(facultyDto);
                    successMessage = "Record faculty was updated!";
                } else {
                    facultyDto = facultyService.create(facultyDto);
                    successMessage = "Record faculty was created!";
                }
            } catch (EntityAlreadyExistsException e) {
                errorMessage = "Record faculty was not created! The record already exists!";
                path = PATH_HTML_FACULTY_EDIT;
            } catch (EntityNotFoundException e) {
                errorMessage = "Faculty " + facultyDto + " not found!";
                path = PATH_HTML_FACULTY_EDIT;
            } catch (EntityNotValidException e) {
                errorMessage = "Record faculty was not updated/created! The data is not valid!";
                path = PATH_HTML_FACULTY_EDIT;
            }
        } else {
            errorMessage = "You enter incorrect data!";
            path = PATH_HTML_FACULTY_EDIT;
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Faculty edit");
        model.addAttribute(ATTRIBUTE_HTML_FACULTY, facultyDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        model.addAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, successMessage);
        return path;
    }

    private boolean checkId(String id) {
        return StringUtils.isNoneBlank(id);
    }

}
