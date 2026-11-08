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
import ua.com.foxminded.task.domain.dto.AuditoryDto;
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.service.AuditoryService;
import ua.com.foxminded.task.service.AuditoryTypeService;

@Controller
public class AuditoryController {

    private static final String PATH_HTML_AUDITORY = "auditory/auditory";
    private static final String PATH_HTML_AUDITORIES = "auditory/auditories";
    private static final String PATH_HTML_AUDITORY_EDIT = "auditory/auditory_edit";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String ATTRIBUTE_HTML_AUDITORY = "auditoryDto";
    private static final String ATTRIBUTE_HTML_AUDITORIES = "auditories";
    private static final String ATTRIBUTE_HTML_AUDITORYTYPE = "auditorytypes";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private Logger logger;
    private AuditoryService auditoryService;
    private AuditoryTypeService auditoryTypeService;

    @Autowired
    public AuditoryController(Logger logger, AuditoryService auditoryService, AuditoryTypeService auditoryTypeService) {
        this.logger = logger;
        this.auditoryService = auditoryService;
        this.auditoryTypeService = auditoryTypeService;
    }

    @GetMapping("/auditories")
    public String getEntities(Model model) {
        logger.debug("getEntities()");
        List<AuditoryDto> auditories = auditoryService.findAllDto();

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Auditories");
        model.addAttribute(ATTRIBUTE_HTML_AUDITORIES, auditories);
        return PATH_HTML_AUDITORIES;
    }

    @GetMapping("/auditory")
    public String getEntityById(@RequestParam("id") String idString, Model model) {
        logger.debug("getEntityById()");
        String errorMessage = null;
        AuditoryDto auditoryDto = null;
        int id = 0;
        try {
            if (checkId(idString)) {
                id = Integer.valueOf(idString);
                auditoryDto = auditoryService.findByIdDto(id);
            } else {
                errorMessage = "You id is blank";
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Auditory by id#" + id + " not found!";
        } catch (NumberFormatException e) {
            errorMessage = "Auditory id# must be numeric!";
        }
        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Auditory");
        model.addAttribute(ATTRIBUTE_HTML_AUDITORY, auditoryDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_AUDITORY;
    }

    @GetMapping("/auditory_edit")
    public String editGetEntity(@RequestParam(name = "id", required = false) String id, Model model) {
        logger.debug("editGetEntity(), id: {}", id);
        String errorMessage = null;
        AuditoryDto auditoryDto = new AuditoryDto();
        try {
            if (checkId(id)) {
                auditoryDto = auditoryService.findByIdDto(Integer.valueOf(id));
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Problem with finding auditory";
        }
        List<AuditoryTypeDto> auditorytypes = auditoryTypeService.findAllDto();

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Auditory edit");
        model.addAttribute(ATTRIBUTE_HTML_AUDITORY, auditoryDto);
        model.addAttribute(ATTRIBUTE_HTML_AUDITORYTYPE, auditorytypes);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_AUDITORY_EDIT;
    }

    @PostMapping("/auditory_edit")
    public String editPostEntity(@Valid @ModelAttribute("auditoryDto") AuditoryDto auditoryDto, 
                                 BindingResult bindingResult, 
                                 Model model) {
        logger.debug("editPost()");
        String errorMessage = null;
        String successMessage = null;
        List<AuditoryTypeDto> auditorytypes = null;
        String path = PATH_HTML_AUDITORY;

        if (!bindingResult.hasErrors()) {
            try {
                if (auditoryDto.getId() != 0) {
                    auditoryDto = auditoryService.update(auditoryDto);
                    successMessage = "Record auditory was updated!";
                } else {
                    auditoryDto = auditoryService.create(auditoryDto);
                    successMessage = "Record auditory was created";
                }
            } catch (EntityAlreadyExistsException e) {
                errorMessage = "Record auditory was not created! The record already exists!";
                path = PATH_HTML_AUDITORY_EDIT;
            } catch (EntityNotFoundException e) {
                errorMessage = "Auditory " + auditoryDto + " not found!";
                path = PATH_HTML_AUDITORY_EDIT;
            } catch (EntityNotValidException e) {
                errorMessage = "Record auditory was not updated/created! The data is not valid!";
                path = PATH_HTML_AUDITORY_EDIT;
            }
        } else {
            errorMessage = "You enter incorrect data!";
            path = PATH_HTML_AUDITORY_EDIT;
            auditorytypes = auditoryTypeService.findAllDto();
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Auditory edit");
        model.addAttribute(ATTRIBUTE_HTML_AUDITORY, auditoryDto);
        model.addAttribute(ATTRIBUTE_HTML_AUDITORYTYPE, auditorytypes);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        model.addAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, successMessage);
        return path;
    }

    private boolean checkId(String id) {
        return StringUtils.isNoneBlank(id);
    }

}
