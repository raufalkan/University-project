package ua.com.foxminded.task.controller.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.task.domain.dto.StudentDto;
import ua.com.foxminded.task.domain.repository.dto.StudentDtoModelRepository;
import ua.com.foxminded.task.service.StudentService;

@ExtendWith(SpringExtension.class)
public class StudentRestControllerTest {

    private MockMvc mockMvc;
    private StudentController studentController;

    @MockBean
    private StudentService studentService;
    
    @MockBean
    private Logger logger;

    private static StudentDto STUDENT_DTO1 = StudentDtoModelRepository.getModel1();
    private static StudentDto STUDENT_DTO2 = StudentDtoModelRepository.getModel2();

    @BeforeEach
    public void init() {
        studentController = new StudentController(logger, studentService);
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    void whenPerformStudentsRequest_thenExpectListOfStudent() throws Exception {
        STUDENT_DTO1.setId(1);
        STUDENT_DTO2.setId(2);
        List<StudentDto> studentDtos = Arrays.asList(STUDENT_DTO1, STUDENT_DTO2);

        when(studentService.findAllDto()).thenReturn(studentDtos);

        this.mockMvc.perform(get("/api/students").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("firstName1")))
                .andExpect(jsonPath("$[0].middleName", is("middleName1")))
                .andExpect(jsonPath("$[0].lastName", is("lastName1")))
                .andExpect(jsonPath("$[0].birthday.[0]", is(1999)))
                .andExpect(jsonPath("$[0].birthday.[1]", is(6)))
                .andExpect(jsonPath("$[0].birthday.[2]", is(25)))
                .andExpect(jsonPath("$[0].idFees", is(111111111)))
                .andExpect(jsonPath("$[0].groupTitle", is("group11")))
                .andExpect(jsonPath("$[0].groupId", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("firstName2")))
                .andExpect(jsonPath("$[1].middleName", is("middleName2")))
                .andExpect(jsonPath("$[1].lastName", is("lastName2")))
                .andExpect(jsonPath("$[1].birthday.[0]", is(1998)))
                .andExpect(jsonPath("$[1].birthday.[1]", is(6)))
                .andExpect(jsonPath("$[1].birthday.[2]", is(25)))
                .andExpect(jsonPath("$[1].idFees", is(222211111)))
                .andExpect(jsonPath("$[1].groupTitle", is("group11")))
                .andExpect(jsonPath("$[1].groupId", is(1)));
    }

    @Test
    void whenPerformStudentsAndIdRequest_thenExpectStudentById() throws Exception {
        STUDENT_DTO2.setId(2);

        when(studentService.findByIdDto(2)).thenReturn(STUDENT_DTO2);

        this.mockMvc.perform(get("/api/students/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(8)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstName", is("firstName2")))
                .andExpect(jsonPath("$.middleName", is("middleName2")))
                .andExpect(jsonPath("$.lastName", is("lastName2")))
                .andExpect(jsonPath("$.birthday.[0]", is(1998)))
                .andExpect(jsonPath("$.birthday.[1]", is(6)))
                .andExpect(jsonPath("$.birthday.[2]", is(25)))
                .andExpect(jsonPath("$.idFees", is(222211111)))
                .andExpect(jsonPath("$.groupTitle", is("group11")))
                .andExpect(jsonPath("$.groupId", is(1)));
    }

    @Test
    void whenPerformPostStudentsRequest_thenUpdateStudent() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(2);
        studentDto.setFirstName("firstName2");
        studentDto.setMiddleName("middleName2");
        studentDto.setLastName("lastName2");
        studentDto.setBirthday(LocalDate.parse("1997-06-25"));
        studentDto.setIdFees(232211111);
        studentDto.setGroupTitle(null);
        studentDto.setGroupId(0);

        when(studentService.update(any(StudentDto.class))).thenReturn(studentDto);

        StudentDto actuallyStudentDto = studentController.saveStudent(studentDto);
        
        verify(studentService, times(1)).update(studentDto);
        assertThat(studentDto).isEqualTo(actuallyStudentDto);
    }

    @Test
    void whenPerformPostStudentsRequestWithIdZero_thenCreateStudent() throws Exception {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(0);
        studentDto.setFirstName("firstName7");
        studentDto.setMiddleName("middleName7");
        studentDto.setLastName("lastName7");
        studentDto.setBirthday(LocalDate.parse("1997-06-25"));
        studentDto.setIdFees(232211111);
        studentDto.setGroupTitle("group3");
        studentDto.setGroupId(3);

        when(studentService.create(any(StudentDto.class))).thenReturn(studentDto);
        
        StudentDto actuallyStudentDto = studentController.saveStudent(studentDto);

        verify(studentService, times(1)).create(studentDto);
        assertThat(studentDto).isEqualTo(actuallyStudentDto);
    }
}
