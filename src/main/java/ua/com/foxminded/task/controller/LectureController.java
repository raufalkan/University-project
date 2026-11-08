package ua.com.foxminded.task.controller;

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
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.service.LectureService;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Controller
public class LectureController {

    private static final String PATH_HTML_LECTURE = "lecture/lecture";
    private static final String PATH_HTML_LECTURIES = "lecture/lecturies";
    private static final String PATH_HTML_LECTURE_EDIT = "lecture/lecture_edit";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String ATTRIBUTE_HTML_LECTURE = "lectureDto";
    private static final String ATTRIBUTE_HTML_LECTURIES = "lecturies";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private LectureService lectureService;
    private Logger logger;

    @Autowired
    public LectureController(Logger logger, LectureService lectureService) {
        this.logger = logger;
        this.lectureService = lectureService;
    }

    @GetMapping("/lecturies")
    public String getEntities(Model model) {
        logger.debug("getEntities()");
        String errorMessage = null;
        List<LectureDto> lecturies = null;
        try {
            lecturies = lectureService.findAllDto();
        } catch (EntityNotFoundException e) {
            errorMessage = "Problem with finding lecture";
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Lecture");
        model.addAttribute(ATTRIBUTE_HTML_LECTURIES, lecturies);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_LECTURIES;
    }

    @GetMapping("/lecture")
    public String getEntityById(@RequestParam("id") String idString, Model model) {
        logger.debug("getEntityById()");
        String errorMessage = null;
        LectureDto lectureDto = null;

        int id = 0;
        try {
            if (checkId(idString)) {
                id = Integer.valueOf(idString);
                lectureDto = lectureService.findByIdDto(id);
            } else {
                errorMessage = "You id is blank";
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Lecture by id#" + id + " not found!";
        } catch (NumberFormatException e) {
            errorMessage = "Lecture id# must be numeric!";
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Lecture");
        model.addAttribute(ATTRIBUTE_HTML_LECTURE, lectureDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_LECTURE;
    }

    @GetMapping("/lecture_edit")
    public String editGetEntity(@RequestParam(name = "id", required = false) String id, Model model) {
        logger.debug("editGetEntity(), id: {}", id);
        String errorMessage = null;
        LectureDto lectureDto = new LectureDto();
        try {
            if (checkId(id)) {
                lectureDto = lectureService.findByIdDto(Integer.valueOf(id));
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Problem with finding lecture";
        }
        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Lecture edit");
        model.addAttribute(ATTRIBUTE_HTML_LECTURE, lectureDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_LECTURE_EDIT;
    }

    @PostMapping("/lecture_edit")
    public String editPostEntity(@Valid @ModelAttribute("lectureDto") LectureDto lectureDto, 
                                 BindingResult bindingResult, 
                                 Model model) {
        logger.debug("editPostEntity()");
        String errorMessage = null;
        String successMessage = null;
        String path = PATH_HTML_LECTURE;

        if (!bindingResult.hasErrors()) {

            try {
                if (lectureDto.getId() != 0) {
                    lectureDto = lectureService.update(lectureDto);
                    successMessage = "Record lecture was updated!";
                } else {
                    lectureDto = lectureService.create(lectureDto);
                    successMessage = "Record lecture was created!";
                }
            } catch (EntityAlreadyExistsException e) {
                errorMessage = "Record lecture was not created! The record already exists!";
                path = PATH_HTML_LECTURE_EDIT;
            } catch (EntityNotFoundException e) {
                errorMessage = "Lecture " + lectureDto + " not found!";
                path = PATH_HTML_LECTURE_EDIT;
            } catch (EntityNotValidException e) {
                errorMessage = "Record lecture was not updated/created! The data is not valid!";
                path = PATH_HTML_LECTURE_EDIT;
            }
        } else {
            errorMessage = "You enter incorrect data!";
            path = PATH_HTML_LECTURE_EDIT;
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Lecture edit");
        model.addAttribute(ATTRIBUTE_HTML_LECTURE, lectureDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        model.addAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, successMessage);
        return path;
    }

    private boolean checkId(String id) {
        return StringUtils.isNoneBlank(id);
    }

}
