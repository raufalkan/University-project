package ua.com.foxminded.task.controller;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.task.config.TestMvcConfig;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.dto.StudentDto;
import ua.com.foxminded.task.domain.repository.dto.GroupDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.StudentDtoModelRepository;
import ua.com.foxminded.task.service.GroupService;
import ua.com.foxminded.task.service.StudentService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

@WebMvcTest(StudentController.class)
@Import(TestMvcConfig.class)
public class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;
    @MockBean
    private StudentService studentService;
    @MockBean
    @Qualifier("uniqueValidationCommandMap")
    private Map<String, Command> uniqueValidationCommandMap;

    private static final String PATH_HTML_STUDENT = "student/student";
    private static final String PATH_HTML_STUDENTS = "student/students";
    private static final String PATH_HTML_STUDENT_EDIT = "student/student_edit";
    private static final String ATTRIBUTE_HTML_STUDENT = "studentDto";
    private static final String ATTRIBUTE_HTML_STUDENTS = "students";
    private static final String ATTRIBUTE_HTML_GROUPS = "groups";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String EXPECTED_ERROR_MESSAGE = "You enter incorrect data!";

    @Test
    void whenRetriveAllStudent_thenExpectListOfStudent() throws Exception {
        List<StudentDto> studentDtos = StudentDtoModelRepository.getModels();
        String httpRequest = "/students";
        String expectedTitle = "Students";

        when(studentService.findAllDto()).thenReturn(studentDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_STUDENTS, equalTo(studentDtos)))
                .andExpect(view().name(PATH_HTML_STUDENTS))
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
                .andExpect(view().name(PATH_HTML_STUDENT))
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
                .andExpect(view().name(PATH_HTML_STUDENT_EDIT))
                .andDo(print());
    }

    @Test
    void whenSubmitEditFormStudentWithId_thenUpdateStudent() throws Exception {
        StudentDto studentDto = StudentDtoModelRepository.getModel1();
        studentDto.setId(1);
        String httpRequest = "/student_edit";
        String expectedTitle = "Student edit";
        String expectedSuccessMessage = "Record student was updated!";

        when(studentService.update(studentDto)).thenReturn(studentDto);

        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("studentDto", studentDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, equalTo(expectedSuccessMessage)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_STUDENT, equalTo(studentDto)))
                .andExpect(view().name(PATH_HTML_STUDENT))
                .andDo(print());
    }

    @Test
    void whenSubmitEditFormStudentWithoutId_thenCreateStudent() throws Exception {
        StudentDto studentDto = StudentDtoModelRepository.getModel7();
        String httpRequest = "/student_edit";
        String expectedTitle = "Student edit";
        String expectedSuccessMessage = "Record student was created";

        when(studentService.create(studentDto)).thenReturn(studentDto);

        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("studentDto", studentDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, equalTo(expectedSuccessMessage)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_STUDENT, equalTo(studentDto)))
                .andExpect(view().name(PATH_HTML_STUDENT))
                .andDo(print());
    }

    @Test
    void whenUpdateStudentWithNotCorrectValues_thenExpectError() throws Exception {
        StudentDto studentDto = StudentDtoModelRepository.getModel7();
        studentDto.setFirstName("");
        studentDto.setIdFees(1212121212);
        String httpRequest = "/student_edit";
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("studentDto", studentDto))
                .andExpect(status().isOk())
                .andExpect(view().name(PATH_HTML_STUDENT_EDIT))
                .andExpect(content().string(allOf(containsString("<div>" + EXPECTED_ERROR_MESSAGE + "</div>"))))
                .andExpect(model().attributeHasFieldErrorCode("studentDto", "firstName", "NotBlank"))
                .andExpect(model().attributeHasFieldErrorCode("studentDto", "idFees", "Max"))
                .andDo(print());
    }

    @Test
    void whenUpdateStudentWithNotCorrectValues_thenExpectError2() throws Exception {
        StudentDto studentDto = StudentDtoModelRepository.getModel7();
        studentDto.setFirstName("qwertyuiopasdfghjklzxcvbnm");
        studentDto.setMiddleName("qwertyuiopasdfghjklzxcvbnm");
        studentDto.setLastName("qwertyuiopasdfghjklzxcvbnm");
        studentDto.setGroupTitle("qwertyuiopasdfghjklzxcvbnm");
        studentDto.setIdFees(12121212);
        String httpRequest = "/student_edit";
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("studentDto", studentDto))
                .andExpect(status().isOk())
                .andExpect(view().name(PATH_HTML_STUDENT_EDIT))
                .andExpect(content().string(allOf(containsString("<div>" + EXPECTED_ERROR_MESSAGE + "</div>"))))
                .andExpect(model().attributeHasFieldErrorCode("studentDto", "firstName", "Size"))
                .andExpect(model().attributeHasFieldErrorCode("studentDto", "middleName", "Size"))
                .andExpect(model().attributeHasFieldErrorCode("studentDto", "lastName", "Size"))
                .andExpect(model().attributeHasFieldErrorCode("studentDto", "groupTitle", "Size"))
                .andExpect(model().attributeHasFieldErrorCode("studentDto", "idFees", "Min"))
                .andDo(print());
    }
    
    @Test
    void whenInvokeCreateExistsStudent_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record sudent was not created! The record already exists!";
        StudentDto studentDto = StudentDtoModelRepository.getModel1();
        String httpRequest = "/student_edit";

        doThrow(EntityAlreadyExistsException.class).when(studentService).create(studentDto);
      
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("studentDto", studentDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(view().name(PATH_HTML_STUDENT_EDIT))
                .andDo(print());
    }
  
    @Test
    void whenInvokeEditNotFoundEntity_thenExpectErrorMessage() throws Exception {
        StudentDto studentDto = StudentDtoModelRepository.getModel1();
        studentDto.setId(1);
        String expectedErrorMessage = "Student " + studentDto + " not found!";
        String httpRequest = "/student_edit";

        doThrow(EntityNotFoundException.class).when(studentService).update(studentDto);
      
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("studentDto", studentDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(view().name(PATH_HTML_STUDENT_EDIT))
                .andDo(print());
    }
  
    @Test
    void whenInvokeEditNotValidEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record sudent was not updated/created! The data is not valid!";
        StudentDto studentDto = StudentDtoModelRepository.getModel1();
        studentDto.setId(1);
        String httpRequest = "/student_edit";

        doThrow(EntityNotValidException.class).when(studentService).update(studentDto);
      
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("studentDto", studentDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(view().name(PATH_HTML_STUDENT_EDIT))
                .andDo(print());
    }
}
