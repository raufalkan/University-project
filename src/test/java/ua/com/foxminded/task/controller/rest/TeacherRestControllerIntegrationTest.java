package ua.com.foxminded.task.controller.rest;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ua.com.foxminded.task.config.TestMvcConfig;
import ua.com.foxminded.task.domain.dto.TeacherDto;
import ua.com.foxminded.task.domain.repository.dto.TeacherDtoModelRepository;
import ua.com.foxminded.task.service.TeacherService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

@WebMvcTest(TeacherController.class)
@Import(TestMvcConfig.class)
public class TeacherRestControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;
    @MockBean
    private TeacherService teacherService;
    @MockBean
    @Qualifier("uniqueValidationCommandMap")
    private Map<String, Command> uniqueValidationCommandMap;
    
    private static TeacherDto TEACHER_DTO1 = TeacherDtoModelRepository.getModel1();
    private static TeacherDto TEACHER_DTO2 = TeacherDtoModelRepository.getModel2();

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        TEACHER_DTO1.setId(1);
        TEACHER_DTO2.setId(2);
        List<TeacherDto> entityDtos = Arrays.asList(TEACHER_DTO1, TEACHER_DTO2);
       
        when(teacherService.findAllDto()).thenReturn(entityDtos);

        this.mockMvc.perform(get("/api/teachers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("firstNameTe1")))
                .andExpect(jsonPath("$[0].middleName", is("middleNameTe1")))
                .andExpect(jsonPath("$[0].lastName", is("lastNameTe1")))
                .andExpect(jsonPath("$[0].birthday", is("1980-06-25")))
                .andExpect(jsonPath("$[0].idFees", is(111111166)))
                .andExpect(jsonPath("$[0].departmentTitle", is("department1")))
                .andExpect(jsonPath("$[0].departmentId", is(1)))
                .andExpect(jsonPath("$[0].subjects.[0].title", is("Phisics")))
                .andExpect(jsonPath("$[0].subjects.[1].title", is("Mathmatics")))
                .andExpect(jsonPath("$[0].subjects.[2].title", is("Biologic")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("firstNameTe2")))
                .andExpect(jsonPath("$[1].middleName", is("middleNameTe2")))
                .andExpect(jsonPath("$[1].lastName", is("lastNameTe2")))
                .andExpect(jsonPath("$[1].birthday", is("1966-06-25")))
                .andExpect(jsonPath("$[1].idFees", is(211111111)))
                .andExpect(jsonPath("$[0].departmentTitle", is("department1")))
                .andExpect(jsonPath("$[0].departmentId", is(1)))
                .andExpect(jsonPath("$[0].subjects.[0].title", is("Phisics")))
                .andExpect(jsonPath("$[0].subjects.[1].title", is("Mathmatics")))
                .andExpect(jsonPath("$[0].subjects.[2].title", is("Biologic")));
    }

    @Test
    void whenPerformEntityAndIdRequest_thenExpectEntityById() throws Exception {
        TEACHER_DTO2.setId(2);

        when(teacherService.findByIdDto(2)).thenReturn(TEACHER_DTO2);

        this.mockMvc.perform(get("/api/teachers/2").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(jsonPath("$", aMapWithSize(9)))
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.firstName", is("firstNameTe2")))
        .andExpect(jsonPath("$.middleName", is("middleNameTe2")))
        .andExpect(jsonPath("$.lastName", is("lastNameTe2")))
        .andExpect(jsonPath("$.birthday", is("1966-06-25")))
        .andExpect(jsonPath("$.idFees", is(211111111)))
        .andExpect(jsonPath("$.departmentTitle", is("department2")))
        .andExpect(jsonPath("$.departmentId", is(0)))
        .andExpect(jsonPath("$.subjects.[0].title", is("Phisics")))
        .andExpect(jsonPath("$.subjects.[1].title", is("Mathmatics")))
        .andExpect(jsonPath("$.subjects.[2].title", is("Biologic")));
    }

    @Test
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        String entity = "{\"id\":2,"
                      + "\"firstName\":\"firstNameTe2\","
                      + "\"middleName\":\"middleNameTe2\","
                      + "\"lastName\":\"lastNameTe2\","
                      + "\"birthday\":\"1966-06-25\","
                      + "\"idFees\":211111111,"
                      + "\"departmentTitle\":\"department1\","
                      + "\"departmentId\":0,"
                      + "\"subjects\":[{}]}";
        TeacherDto entityDto = TEACHER_DTO2;
        entityDto.setId(2);

        when(teacherService.update(any(TeacherDto.class))).thenReturn(entityDto);

        this.mockMvc.perform(post("/api/teachers").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(9)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstName", is("firstNameTe2")))
                .andExpect(jsonPath("$.middleName", is("middleNameTe2")))
                .andExpect(jsonPath("$.lastName", is("lastNameTe2")))
                .andExpect(jsonPath("$.birthday", is("1966-06-25")))
                .andExpect(jsonPath("$.idFees", is(211111111)))
                .andExpect(jsonPath("$.departmentTitle", is("department2")))
                .andExpect(jsonPath("$.departmentId", is(0)))
                .andExpect(jsonPath("$.subjects.[0].title", is("Phisics")))
                .andExpect(jsonPath("$.subjects.[1].title", is("Mathmatics")))
                .andExpect(jsonPath("$.subjects.[2].title", is("Biologic")));
    }

    @Test
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        String entity = "{\"id\":0,"
                      + "\"firstName\":\"firstNameTe2\","
                      + "\"middleName\":\"middleNameTe2\","
                      + "\"lastName\":\"lastNameTe2\","
                      + "\"birthday\":\"1966-06-25\","
                      + "\"idFees\":211111111,"
                      + "\"departmentTitle\":\"department1\","
                      + "\"departmentId\":1,"
                      + "\"subjects\":[{\"id\":1,"
                      + "\"title\":\"Programming\"}]}";
        TeacherDto entityDto = TEACHER_DTO2;
        entityDto.setId(2);
        
        when(teacherService.create(any(TeacherDto.class))).thenReturn(entityDto);

        this.mockMvc.perform(post("/api/teachers").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(9)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstName", is("firstNameTe2")))
                .andExpect(jsonPath("$.middleName", is("middleNameTe2")))
                .andExpect(jsonPath("$.lastName", is("lastNameTe2")))
                .andExpect(jsonPath("$.birthday", is("1966-06-25")))
                .andExpect(jsonPath("$.idFees", is(211111111)))
                .andExpect(jsonPath("$.departmentTitle", is("department2")))
                .andExpect(jsonPath("$.departmentId", is(0)))
                .andExpect(jsonPath("$.subjects.[0].title", is("Phisics")))
                .andExpect(jsonPath("$.subjects.[1].title", is("Mathmatics")))
                .andExpect(jsonPath("$.subjects.[2].title", is("Biologic")));
    }

    @Test
    void whenUpdateEntityWithNotCorrectValues_thenExpectError() throws Exception {
        String entity = "{\"id\":2,"
                + "\"firstName\":\"\","
                + "\"middleName\":\"middleNameTe2\","
                + "\"lastName\":\"lastNameTe2\","
                + "\"birthday\":\"1966-06-25\","
                + "\"idFees\":1234567890,"
                + "\"departmentTitle\":\"department1\","
                + "\"departmentId\":1,"
                + "\"subjects\":[{\"id\":1,"
                + "\"title\":\"Programming\"}]}";
        
        this.mockMvc.perform(post("/api/teachers").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(2)))
                .andExpect(jsonPath("$.errors.idFees", is("Value is 9 number!")))
                .andExpect(jsonPath("$.errors.firstName", is("First name can`t be blank!")));
    }

    @Test
    void whenUpdateEntityWithNotCorrectValues_thenExpectError2() throws Exception {
        String entity = "{\"id\":2,"
                + "\"firstName\":\"asdfghjkloiuytrewqzxc\","
                + "\"middleName\":\"asdfghjkloiuytrewqzxc\","
                + "\"lastName\":\"asdfghjkloiuytrewqzxc\","
                + "\"birthday\":\"1966-06-25\","
                + "\"idFees\":12345678,"
                + "\"departmentTitle\":\"asdfghjkloiuytrewqzxc\","
                + "\"departmentId\":1,"
                + "\"subjects\":[{\"id\":1,"
                + "\"title\":\"Programming\"}]}";
        
        this.mockMvc.perform(post("/api/teachers").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors.firstName", is("Maximum length of first name is 20!")))
                .andExpect(jsonPath("$.errors.middleName", is("Maximum length of middle name is 20!")))
                .andExpect(jsonPath("$.errors.lastName", is("Maximum length of last name is 20!")))
                .andExpect(jsonPath("$.errors.idFees", is("Value is 9 number!")))
                .andExpect(jsonPath("$.errors.departmentTitle", is("Maximum length of title department is 20!")));
    }
}
