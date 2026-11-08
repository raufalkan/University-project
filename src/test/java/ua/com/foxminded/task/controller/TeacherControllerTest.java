package ua.com.foxminded.task.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.dto.DepartmentDto;
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.domain.dto.TeacherDto;
import ua.com.foxminded.task.domain.repository.dto.DepartmentDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.SubjectDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.TeacherDtoModelRepository;
import ua.com.foxminded.task.service.DepartmentService;
import ua.com.foxminded.task.service.SubjectService;
import ua.com.foxminded.task.service.TeacherService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class TeacherControllerTest {

    private MockMvc mockMvc;
    private TeacherController teacherController;

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private SubjectService subjectService;

    @MockBean
    private TeacherService teacherService;
    
    @MockBean
    private Logger logger;
    
    @MockBean
    private BindingResult bindingResult;

    private static final String PATH_HTML_TEACHER = "teacher/teacher";
    private static final String PATH_HTML_TEACHERS = "teacher/teachers";
    private static final String PATH_HTML_TEACHER_EDIT = "teacher/teacher_edit";
    private static final String ATTRIBUTE_HTML_TEACHER = "teacherDto";
    private static final String ATTRIBUTE_HTML_TEACHERS = "teachers";
    private static final String ATTRIBUTE_HTML_DEPARTMENTS = "departments";
    private static final String ATTRIBUTE_HTML_SUBJECTS = "allSubjects";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    
    @BeforeEach
    public void init() {
        teacherController = new TeacherController(logger, teacherService, departmentService, subjectService);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void whenRetrieveAllEntity_thenExpectListOfEntities() throws Exception {
        List<TeacherDto> teacherDtos = TeacherDtoModelRepository.getModels1();
        String expectedTitle = "Teachers";
        String httpRequest = "/teachers";

        when(teacherService.findAllDto()).thenReturn(teacherDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_TEACHERS, equalTo(teacherDtos)))
                .andExpect(forwardedUrl(PATH_HTML_TEACHERS))
                .andDo(print());
    }
    
    @Test
    void whenRetrieveTheEntity_thenExpectEntityById() throws Exception {
        TeacherDto teacherDto = TeacherDtoModelRepository.getModel1();
        int id = 1;
        String httpRequest = "/teacher?id=" + id;
        String expectedTitle = "Teacher";

        when(teacherService.findByIdDto(id)).thenReturn(teacherDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_TEACHER, equalTo(teacherDto)))
                .andExpect(forwardedUrl(PATH_HTML_TEACHER))
                .andDo(print());
    }

    @Test
    void whenInvokeByBlankId_thenErrorMessage() throws Exception {
        String expectedErrorMessage = "You id is blank";
        String httpRequest = "/teacher?id=";

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_TEACHER))
                .andDo(print());
    }
    
    @Test
    void whenInvokeNoFoundEntities_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Teacher by id#" + id + " not found!";
        String httpRequest = "/teacher?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(teacherService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_TEACHER))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEntitiesWithIncorrectNumberFormatId_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Teacher id# must be numeric!";
        String httpRequest = "/teacher?id=" + id;
        
        doThrow(NumberFormatException.class).when(teacherService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_TEACHER))
                .andDo(print());
    }
    
    @Test
    void whenRetrieveEditExistsEntity_thenExpectFormWithEntityField() throws Exception {
        TeacherDto teacherDto = TeacherDtoModelRepository.getModel1();
        List<DepartmentDto> departmentDtos = DepartmentDtoModelRepository.getModels();
        List<SubjectDto> subjectDtos = SubjectDtoModelRepository.getModels1();

        int id = 1;
        String httpRequest = "/teacher_edit?id=" + id;
        String expectedTitle = "Teacher edit";

        when(teacherService.findByIdDto(id)).thenReturn(teacherDto);
        when(departmentService.findAllDto()).thenReturn(departmentDtos);
        when(subjectService.findAllDto()).thenReturn(subjectDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_TEACHER, equalTo(teacherDto)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_DEPARTMENTS, equalTo(departmentDtos)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_SUBJECTS, equalTo(subjectDtos)))
                .andExpect(forwardedUrl(PATH_HTML_TEACHER_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEditEntityWithNoEntityNumber_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Problem with finding teacher";
        List<DepartmentDto> departmentDtos = DepartmentDtoModelRepository.getModels();
        List<SubjectDto> subjectDtos = SubjectDtoModelRepository.getModels1();

        int id = 1;
        String httpRequest = "/teacher_edit?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(teacherService).findByIdDto(id);
        when(departmentService.findAllDto()).thenReturn(departmentDtos);
        when(subjectService.findAllDto()).thenReturn(subjectDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_TEACHER_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenSubmitEditFormEntityWithId_thenUpdateEntity() throws Exception {
        TeacherDto teacherDto = TeacherDtoModelRepository.getModel1();
        teacherDto.setId(1);
        String expectedTitle = "Teacher edit";
        String expectedSuccessMessage = "Record teacher was updated!";
        Model model = new ExtendedModelMap();

        when(teacherService.update(teacherDto)).thenReturn(teacherDto);
        when(bindingResult.hasErrors()).thenReturn(false);

        String actuallyView = teacherController.editPostEntity(teacherDto, bindingResult, model);
        
        assertThat(PATH_HTML_TEACHER).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(teacherDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TEACHER));
    }

    @Test
    void whenSubmitEditFormEntityWithoutId_thenCreateEntity() throws Exception {
        TeacherDto teacherDto = TeacherDtoModelRepository.getModel3();
        String expectedTitle = "Teacher edit";
        String expectedSuccessMessage = "Record teacher was created";
        Model model = new ExtendedModelMap();

        when(teacherService.create(teacherDto)).thenReturn(teacherDto);

        String actuallyView = teacherController.editPostEntity(teacherDto, bindingResult, model);

        assertThat(PATH_HTML_TEACHER).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(teacherDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TEACHER));
    }
    
    @Test
    void whenInvokeCreateExistsEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record teacher was not created! The record already exists!";
        TeacherDto teacherDto = TeacherDtoModelRepository.getModel1();
        Model model = new ExtendedModelMap();

        doThrow(EntityAlreadyExistsException.class).when(teacherService).create(teacherDto);
      
        String actuallyView = teacherController.editPostEntity(teacherDto, bindingResult, model);

        assertThat(PATH_HTML_TEACHER_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(teacherDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TEACHER));
    }
  
    @Test
    void whenInvokeEditNotFoundEntity_thenExpectErrorMessage() throws Exception {
        TeacherDto teacherDto = TeacherDtoModelRepository.getModel1();
        teacherDto.setId(1);
        String expectedErrorMessage = "Teacher " + teacherDto + " not found!";
        Model model = new ExtendedModelMap();

        doThrow(EntityNotFoundException.class).when(teacherService).update(teacherDto);
      
        String actuallyView = teacherController.editPostEntity(teacherDto, bindingResult, model);

        assertThat(PATH_HTML_TEACHER_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(teacherDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TEACHER));
    }
  
    @Test
    void whenInvokeEditNotValidEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record teacher was not updated/created! The data is not valid!";
        TeacherDto teacherDto = TeacherDtoModelRepository.getModel1();
        teacherDto.setId(1);
        Model model = new ExtendedModelMap();

        doThrow(EntityNotValidException.class).when(teacherService).update(teacherDto);
      
        String actuallyView = teacherController.editPostEntity(teacherDto, bindingResult, model);

        assertThat(PATH_HTML_TEACHER_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(teacherDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TEACHER));
    }
    
}
