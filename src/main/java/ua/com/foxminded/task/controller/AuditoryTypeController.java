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
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.service.AuditoryTypeService;

@Controller
public class AuditoryTypeController {

    private static final String PATH_HTML_AUDITORYTYPE = "auditorytype/auditorytype";
    private static final String PATH_HTML_AUDITORYTYPES = "auditorytype/auditorytypes";
    private static final String PATH_HTML_AUDITORYTYPE_EDIT = "auditorytype/auditorytype_edit";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String ATTRIBUTE_HTML_AUDITORYTYPE = "auditoryTypeDto";
    private static final String ATTRIBUTE_HTML_AUDITORYTYPES = "auditorytypes";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private AuditoryTypeService auditoryTypeService;
    private Logger logger;

    @Autowired
    public AuditoryTypeController(Logger logger, AuditoryTypeService auditoryTypeService) {
        this.logger = logger;
        this.auditoryTypeService = auditoryTypeService;
    }

    @GetMapping("/auditorytypes")
    public String getEntities(Model model) {
        logger.debug("getEntities()");
        String errorMessage = null;
        List<AuditoryTypeDto> auditorytypes = null;
        try {
            auditorytypes = auditoryTypeService.findAllDto();
        } catch (EntityNotFoundException e) {
            errorMessage = "Problem with finding auditory type";
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Auditory types");
        model.addAttribute(ATTRIBUTE_HTML_AUDITORYTYPES, auditorytypes);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_AUDITORYTYPES;
    }

    @GetMapping("/auditorytype")
    public String getEntityById(@RequestParam("id") String idString, Model model) {
        logger.debug("getEntityById()");
        String errorMessage = null;
        AuditoryTypeDto auditoryTypeDto = null;

        int id = 0;
        try {
            if (checkId(idString)) {
                id = Integer.valueOf(idString);
                auditoryTypeDto = auditoryTypeService.findByIdDto(id);
            } else {
                errorMessage = "You id is blank";
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Auditory type by id#" + id + " not found!";
        } catch (NumberFormatException e) {
            errorMessage = "Auditory type id# must be numeric!";
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Auditory type");
        model.addAttribute(ATTRIBUTE_HTML_AUDITORYTYPE, auditoryTypeDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_AUDITORYTYPE;
    }

    @GetMapping("/auditorytype_edit")
    public String editGetEntity(@RequestParam(name = "id", required = false) String id, Model model) {
        logger.debug("editGetEntity(), id: {}", id);
        String errorMessage = null;
        AuditoryTypeDto auditoryTypeDto = new AuditoryTypeDto();
        try {
            if (checkId(id)) {
                auditoryTypeDto = auditoryTypeService.findByIdDto(Integer.valueOf(id));
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Problem with finding auditory type";
        }
        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Auditory type edit");
        model.addAttribute(ATTRIBUTE_HTML_AUDITORYTYPE, auditoryTypeDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_AUDITORYTYPE_EDIT;
    }

    @PostMapping("/auditorytype_edit")
    public String editPostEntity(@Valid @ModelAttribute("auditoryTypeDto") AuditoryTypeDto auditoryTypeDto, 
                                 BindingResult bindingResult, 
                                 Model model) {
        logger.debug("editPostEntity()");
        String errorMessage = null;
        String successMessage = null;
        String path = PATH_HTML_AUDITORYTYPE;

        if (!bindingResult.hasErrors()) {

            try {
                if (auditoryTypeDto.getId() != 0) {
                    auditoryTypeDto = auditoryTypeService.update(auditoryTypeDto);
                    successMessage = "Record auditory type was updated!";
                } else {
                    auditoryTypeDto = auditoryTypeService.create(auditoryTypeDto);
                    successMessage = "Record auditory type was created!";
                }
            } catch (EntityAlreadyExistsException e) {
                errorMessage = "Record auditory type was not created! The record already exists!";
                path = PATH_HTML_AUDITORYTYPE_EDIT;
            } catch (EntityNotFoundException e) {
                errorMessage = "Auditory type " + auditoryTypeDto + " not found!";
                path = PATH_HTML_AUDITORYTYPE_EDIT;
            } catch (EntityNotValidException e) {
                errorMessage = "Record auditory type was not updated/created! The data is not valid!";
                path = PATH_HTML_AUDITORYTYPE_EDIT;
            }
        } else {
            errorMessage = "You enter incorrect data!";
            path = PATH_HTML_AUDITORYTYPE_EDIT;
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Auditory type edit");
        model.addAttribute(ATTRIBUTE_HTML_AUDITORYTYPE, auditoryTypeDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        model.addAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, successMessage);
        return path;
    }

    private boolean checkId(String id) {
        return StringUtils.isNoneBlank(id);
    }

}
