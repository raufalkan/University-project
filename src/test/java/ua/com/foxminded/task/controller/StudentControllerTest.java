package ua.com.foxminded.task.controller;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityNotFoundException;

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
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.dto.StudentDto;
import ua.com.foxminded.task.domain.repository.dto.GroupDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.StudentDtoModelRepository;
import ua.com.foxminded.task.service.GroupService;
import ua.com.foxminded.task.service.StudentService;

@ExtendWith(SpringExtension.class)
public class StudentControllerTest {

    private MockMvc mockMvc;
    private StudentController studentController;

    @MockBean
    private GroupService groupService;

    @MockBean
    private StudentService studentService;
    
    @MockBean
    private Logger logger;
    
    @MockBean
    private BindingResult bindingResult;

    private static final String PATH_HTML_STUDENT = "student/student";
    private static final String PATH_HTML_STUDENTS = "student/students";
    private static final String PATH_HTML_STUDENT_EDIT = "student/student_edit";
    private static final String ATTRIBUTE_HTML_STUDENT = "studentDto";
    private static final String ATTRIBUTE_HTML_STUDENTS = "students";
    private static final String ATTRIBUTE_HTML_GROUPS = "groups";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    
    @BeforeEach
    public void init() {
        studentController = new StudentController(logger, studentService, groupService);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }
    
    @Test
    void whenRetriveAllStudent_thenExpectListOfStudent() throws Exception {
        List<StudentDto> studentDtos = StudentDtoModelRepository.getModels();
        String expectedTitle = "Students";
        String httpRequest = "/students";

        when(studentService.findAllDto()).thenReturn(studentDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_STUDENTS, equalTo(studentDtos)))
                .andExpect(forwardedUrl(PATH_HTML_STUDENTS))
                .andDo(print());
    }
    
    @Test
    void whenRetriveTheStudent_thenExpectStudentById() throws Exception {
        StudentDto studentDto = StudentDtoModelRepository.getModel1();
        int id = 1;
        String httpRequest = "/student?id=" + id;
        String expectedTitle = "Student";

        when(studentService.findByIdDto(id)).thenReturn(studentDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_STUDENT, equalTo(studentDto)))
                .andExpect(forwardedUrl(PATH_HTML_STUDENT))
                .andDo(print());
    }

    @Test
    void whenInvokeByBlankId_thenErrorMessage() throws Exception {
        String expectedErrorMessage = "You id is blank";
        String httpRequest = "/student?id=";

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_STUDENT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeNoFoundGroup_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Student by id#" + id + " not found!";
        String httpRequest = "/student?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(studentService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_STUDENT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeGroupWithIncorrectNumberFormatId_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Student id# must be numeric!";
        String httpRequest = "/student?id=" + id;
        
        doThrow(NumberFormatException.class).when(studentService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_STUDENT))
                .andDo(print());
    }
    
    @Test
    void whenRetriveEditExistsStudent_thenExpectFormWithStudentField() throws Exception {
        StudentDto studentDto = StudentDtoModelRepository.getModel1();
        List<GroupDto> groupDtos = GroupDtoModelRepository.getModels();
        int id = 1;
        String httpRequest = "/student_edit?id=" + id;
        String expectedTitle = "Student edit";

        when(studentService.findByIdDto(id)).thenReturn(studentDto);
        when(groupService.findAllDto()).thenReturn(groupDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_STUDENT, equalTo(studentDto)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUPS, equalTo(groupDtos)))
                .andExpect(forwardedUrl(PATH_HTML_STUDENT_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEditGroupWithNoEntityNumber_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Problem with finding group";
        List<GroupDto> groupDtos = GroupDtoModelRepository.getModels();
        int id = 1;
        String httpRequest = "/student_edit?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(studentService).findByIdDto(id);
        when(groupService.findAllDto()).thenReturn(groupDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_STUDENT_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenSubmitEditFormStudentWithId_thenUpdateStudent() throws Exception {
        StudentDto studentDto = StudentDtoModelRepository.getModel1();
        studentDto.setId(1);
        String expectedTitle = "Student edit";
        String expectedSuccessMessage = "Record student was updated!";
        Model model = new ExtendedModelMap();

        when(studentService.update(studentDto)).thenReturn(studentDto);
        when(bindingResult.hasErrors()).thenReturn(false);

        String actuallyView = studentController.editPost(studentDto, bindingResult, model);
        
        assertThat(PATH_HTML_STUDENT).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(studentDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_STUDENT));
    }

    @Test
    void whenSubmitEditFormStudentWithoutId_thenCreateStudent() throws Exception {
        StudentDto studentDto = StudentDtoModelRepository.getModel7();
        String expectedTitle = "Student edit";
        String expectedSuccessMessage = "Record student was created";
        Model model = new ExtendedModelMap();

        when(studentService.create(studentDto)).thenReturn(studentDto);

        String actuallyView = studentController.editPost(studentDto, bindingResult, model);

        assertThat(PATH_HTML_STUDENT).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(studentDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_STUDENT));
    }
    
    @Test
    void whenInvokeCreateExistsStudent_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record sudent was not created! The record already exists!";
        StudentDto studentDto = StudentDtoModelRepository.getModel1();
        Model model = new ExtendedModelMap();

        doThrow(EntityAlreadyExistsException.class).when(studentService).create(studentDto);
      
        String actuallyView = studentController.editPost(studentDto, bindingResult, model);

        assertThat(PATH_HTML_STUDENT_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(studentDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_STUDENT));
    }
  
    @Test
    void whenInvokeEditNotFoundEntity_thenExpectErrorMessage() throws Exception {
        StudentDto studentDto = StudentDtoModelRepository.getModel1();
        studentDto.setId(1);
        String expectedErrorMessage = "Student " + studentDto + " not found!";
        Model model = new ExtendedModelMap();

        doThrow(EntityNotFoundException.class).when(studentService).update(studentDto);
      
        String actuallyView = studentController.editPost(studentDto, bindingResult, model);

        assertThat(PATH_HTML_STUDENT_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(studentDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_STUDENT));
    }
  
    @Test
    void whenInvokeEditNotValidEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record sudent was not updated/created! The data is not valid!";
        StudentDto studentDto = StudentDtoModelRepository.getModel1();
        studentDto.setId(1);
        Model model = new ExtendedModelMap();

        doThrow(EntityNotValidException.class).when(studentService).update(studentDto);
      
        String actuallyView = studentController.editPost(studentDto, bindingResult, model);

        assertThat(PATH_HTML_STUDENT_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(studentDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_STUDENT));
    }
    
}
