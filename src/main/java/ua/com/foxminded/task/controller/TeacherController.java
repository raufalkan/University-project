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
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.domain.dto.TeacherDto;
import ua.com.foxminded.task.service.DepartmentService;
import ua.com.foxminded.task.service.SubjectService;
import ua.com.foxminded.task.service.TeacherService;

@Controller
public class TeacherController {

    private static final String PATH_HTML_TEACHER = "teacher/teacher";
    private static final String PATH_HTML_TEACHERS = "teacher/teachers";
    private static final String PATH_HTML_TEACHER_EDIT = "teacher/teacher_edit";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String ATTRIBUTE_HTML_TEACHER = "teacherDto";
    private static final String ATTRIBUTE_HTML_TEACHERS = "teachers";
    private static final String ATTRIBUTE_HTML_DEPARTMENTS = "departments";
    private static final String ATTRIBUTE_HTML_SUBJECTS = "allSubjects";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private Logger logger;
    private TeacherService teacherService;
    private DepartmentService departmentService;
    private SubjectService subjectService;
    
    @Autowired
    public TeacherController(Logger logger, 
                             TeacherService teacherService, 
                             DepartmentService departmentService, 
                             SubjectService subjectService) {
        this.logger = logger;
        this.teacherService = teacherService;
        this.departmentService = departmentService;
        this.subjectService = subjectService;
    }

    @GetMapping("/teachers")
    public String getEntities(Model model) {
        logger.debug("getEntities()");
        List<TeacherDto> teachers = teacherService.findAllDto();

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Teachers");
        model.addAttribute(ATTRIBUTE_HTML_TEACHERS, teachers);
        return PATH_HTML_TEACHERS;
    }

    @GetMapping("/teacher")
    public String getEntityById(@RequestParam("id") String idString, Model model) {
        logger.debug("getEntityById()");
        String errorMessage = null;
        TeacherDto teacherDto = null;

        int id = 0;
        try {
            if (checkId(idString)) {
                id = Integer.valueOf(idString);
                teacherDto = teacherService.findByIdDto(id);
            } else {
                errorMessage = "You id is blank";
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Teacher by id#" + id + " not found!";
        } catch (NumberFormatException e) {
            errorMessage = "Teacher id# must be numeric!";
        }
        
        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Teacher");
        model.addAttribute(ATTRIBUTE_HTML_TEACHER, teacherDto);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_TEACHER;
    }

    @GetMapping("/teacher_edit")
    public String editGetEntity(@RequestParam(name = "id", required = false) String id, Model model) {
        logger.debug("editGetEntity(), id: {}", id);
        String errorMessage = null;
        TeacherDto teacherDto = new TeacherDto();
        try {
            if (checkId(id)) {
                teacherDto = teacherService.findByIdDto(Integer.valueOf(id));
            }
        } catch (EntityNotFoundException e) {
            errorMessage = "Problem with finding teacher";
        }
        List<DepartmentDto> departments = departmentService.findAllDto();
        List<SubjectDto> allSubjects = subjectService.findAllDto();

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Teacher edit");
        model.addAttribute(ATTRIBUTE_HTML_TEACHER, teacherDto);
        model.addAttribute(ATTRIBUTE_HTML_DEPARTMENTS, departments);
        model.addAttribute(ATTRIBUTE_HTML_SUBJECTS, allSubjects);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        return PATH_HTML_TEACHER_EDIT;
    }

    @PostMapping("/teacher_edit")
    public String editPostEntity(@Valid @ModelAttribute("teacherDto") TeacherDto teacherDto, 
                                 BindingResult bindingResult, 
                                 Model model) {
        logger.debug("editPost()");
        String errorMessage = null;
        String successMessage = null;
        List<DepartmentDto> departments = null;
        List<SubjectDto> subjectDtos = null;
        String path = PATH_HTML_TEACHER;

        if (!bindingResult.hasErrors()) {
            try {
                if (teacherDto.getId() != 0) {
                    teacherDto = teacherService.update(teacherDto);
                    successMessage = "Record teacher was updated!";
                } else {
                    teacherDto = teacherService.create(teacherDto);
                    successMessage = "Record teacher was created";
                }
            } catch (EntityAlreadyExistsException e) {
                errorMessage = "Record teacher was not created! The record already exists!";
                path = PATH_HTML_TEACHER_EDIT;
            } catch (EntityNotFoundException e) {
                errorMessage = "Teacher " + teacherDto + " not found!";
                path = PATH_HTML_TEACHER_EDIT;
            } catch (EntityNotValidException e) {
                errorMessage = "Record teacher was not updated/created! The data is not valid!";
                path = PATH_HTML_TEACHER_EDIT;
            }
        } else {
            errorMessage = "You enter incorrect data!";
            path = PATH_HTML_TEACHER_EDIT;
            departments = departmentService.findAllDto();
            subjectDtos = subjectService.findAllDto();
        }

        model.addAttribute(ATTRIBUTE_HTML_TITLE, "Teacher edit");
        model.addAttribute(ATTRIBUTE_HTML_TEACHER, teacherDto);
        model.addAttribute(ATTRIBUTE_HTML_DEPARTMENTS, departments);
        model.addAttribute(ATTRIBUTE_HTML_SUBJECTS, subjectDtos);
        model.addAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE, errorMessage);
        model.addAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, successMessage);
        return path;
    }

    private boolean checkId(String id) {
        return StringUtils.isNoneBlank(id);
    }
    
}
