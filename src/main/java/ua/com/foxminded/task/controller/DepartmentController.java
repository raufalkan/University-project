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
import ua.com.foxminded.task.domain.dto.DepartmentDto;
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.service.DepartmentService;
import ua.com.foxminded.task.service.FacultyService;

@Controller
public class DepartmentController {

    private static final String PATH_HTML_DEPARTMENT = "department/department";
    private static final String PATH_HTML_DEPARTMENTS = "department/departments";
    private static final String PATH_HTML_DEPARTMENT_EDIT = "department/department_edit";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String ATTRIBUTE_HTML_DEPARTMENT = "departmentDto";
    private static final String ATTRIBUTE_HTML_DEPARTMENTS = "departments";
    private static final String ATTRIBUTE_HTML_FACULTIES = "faculties";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private Logger logger;
    private DepartmentService departmentService;
    private FacultyService facultyService;

    @Autowired
    public DepartmentController(Logger logger, 
                                DepartmentService departmentService, 
                                FacultyService facultyService) {
        this.logger = logger;
        this.departmentService = departmentService;
        this.facultyService = facultyService;
    }

    @GetMapping("/departments")
    public String getEntities(Model model) {
        logger.debug("getEntities()");
        List<DepartmentDto> departments = departmentService.findAllDto();

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Departments");
        model.addAttribute(ATTRIBUTE_HTML_DEPARTMENTS, departments);
        return PATH_HTML_DEPARTMENTS;
    }

    @GetMapping("/department")
    public String getEntityById(@RequestParam("id") String idString, Model model) {
        logger.debug("getEntityById()");
        String errorMessage = null;
        DepartmentDto departmentDto = null;
        int id = 0;
        try {
            if (checkId(idString)) {
                id = Integer.valueOf(idString);
                departmentDto = departmentService.findByIdDto(id);
            } else {
                errorMessage = "You id is blank";
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Department by id#" + id + " not found!";
        } catch (NumberFormatException e) {
            errorMessage = "Department id# must be numeric!";
        }
        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Department");
        model.addAttribute(ATTRIBUTE_HTML_DEPARTMENT, departmentDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_DEPARTMENT;
    }

    @GetMapping("/department_edit")
    public String editGetEntity(@RequestParam(name = "id", required = false) String id, Model model) {
        logger.debug("editGetEntity(), id: {}", id);
        String errorMessage = null;
        DepartmentDto departmentDto = new DepartmentDto();
        try {
            if (checkId(id)) {
                departmentDto = departmentService.findByIdDto(Integer.valueOf(id));
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Problem with finding department";
        }
        List<FacultyDto> faculties = facultyService.findAllDto();

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Department edit");
        model.addAttribute(ATTRIBUTE_HTML_DEPARTMENT, departmentDto);
        model.addAttribute(ATTRIBUTE_HTML_FACULTIES, faculties);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_DEPARTMENT_EDIT;
    }

    @PostMapping("/department_edit")
    public String editPostEntity(@Valid @ModelAttribute("departmentDto") DepartmentDto departmentDto, 
                                 BindingResult bindingResult, 
                                 Model model) {
        logger.debug("editPost()");
        String errorMessage = null;
        String successMessage = null;
        List<FacultyDto> faculties = null;
        String path = PATH_HTML_DEPARTMENT;

        if (!bindingResult.hasErrors()) {
            try {
                if (departmentDto.getId() != 0) {
                    departmentDto = departmentService.update(departmentDto);
                    successMessage = "Record department was updated!";
                } else {
                    departmentDto = departmentService.create(departmentDto);
                    successMessage = "Record department was created";
                }
            } catch (EntityAlreadyExistsException e) {
                errorMessage = "Record department was not created! The record already exists!";
                path = PATH_HTML_DEPARTMENT_EDIT;
            } catch (EntityNotFoundException e) {
                errorMessage = "Department " + departmentDto + " not found!";
                path = PATH_HTML_DEPARTMENT_EDIT;
            } catch (EntityNotValidException e) {
                errorMessage = "Record department was not updated/created! The data is not valid!";
                path = PATH_HTML_DEPARTMENT_EDIT;
            }
        } else {
            errorMessage = "You enter incorrect data!";
            path = PATH_HTML_DEPARTMENT_EDIT;
            faculties = facultyService.findAllDto();
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Department edit");
        model.addAttribute(ATTRIBUTE_HTML_DEPARTMENT, departmentDto);
        model.addAttribute(ATTRIBUTE_HTML_FACULTIES, faculties);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        model.addAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, successMessage);
        return path;
    }

    private boolean checkId(String id) {
        return StringUtils.isNoneBlank(id);
    }

}
