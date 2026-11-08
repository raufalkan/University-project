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
import ua.com.foxminded.task.domain.dto.DepartmentDto;
import ua.com.foxminded.task.domain.repository.dto.DepartmentDtoModelRepository;
import ua.com.foxminded.task.service.DepartmentService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

@WebMvcTest(DepartmentController.class)
@Import(TestMvcConfig.class)
public class DepartmentRestControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;
    @MockBean
    private DepartmentService departmentService;
    @MockBean
    @Qualifier("uniqueValidationCommandMap")
    private Map<String, Command> uniqueValidationCommandMap;
    
    private static DepartmentDto DEPARTMENT_DTO1 = DepartmentDtoModelRepository.getModel1();
    private static DepartmentDto DEPARTMENT_DTO2 = DepartmentDtoModelRepository.getModel2();

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        DEPARTMENT_DTO1.setId(1);
        DEPARTMENT_DTO2.setId(2);
        List<DepartmentDto> entityDtos = Arrays.asList(DEPARTMENT_DTO1, DEPARTMENT_DTO2);
       
        when(departmentService.findAllDto()).thenReturn(entityDtos);

        this.mockMvc.perform(get("/api/departments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("department1")))
                .andExpect(jsonPath("$[0].description", is("bla bla bla 1")))
                .andExpect(jsonPath("$[0].facultyTitle", is("faculty1")))
                .andExpect(jsonPath("$[0].facultyId", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("department2")))
                .andExpect(jsonPath("$[1].description", is("bla bla bla 2")))
                .andExpect(jsonPath("$[1].facultyTitle", is("faculty1")))
                .andExpect(jsonPath("$[1].facultyId", is(2)));
    }

    @Test
    void whenPerformEntityAndIdRequest_thenExpectEntityById() throws Exception {
        DEPARTMENT_DTO2.setId(2);

        when(departmentService.findByIdDto(2)).thenReturn(DEPARTMENT_DTO2);

        this.mockMvc.perform(get("/api/departments/2").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(jsonPath("$", aMapWithSize(5)))
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.title", is("department2")))
        .andExpect(jsonPath("$.description", is("bla bla bla 2")))
        .andExpect(jsonPath("$.facultyTitle", is("faculty1")))
        .andExpect(jsonPath("$.facultyId", is(2)));
    }

    @Test
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        String entity = "{\"id\":2,"
                      + "\"title\":\"department2\","
                      + "\"description\":\"bla bla bla 2\","
                      + "\"facultyTitle\":\"faculty1\","
                      + "\"facultyId\":1}";
        DepartmentDto entityDto = DEPARTMENT_DTO2;
        entityDto.setId(2);

        when(departmentService.update(any(DepartmentDto.class))).thenReturn(entityDto);

        this.mockMvc.perform(post("/api/departments").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(5)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("department2")))
                .andExpect(jsonPath("$.description", is("bla bla bla 2")))
                .andExpect(jsonPath("$.facultyTitle", is("faculty1")))
                .andExpect(jsonPath("$.facultyId", is(2)));
    }

    @Test
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        String entity = "{\"id\":0,"
                      + "\"title\":\"department2\","
                      + "\"description\":\"bla bla bla 2\","
                      + "\"facultyTitle\":\"faculty1\","
                      + "\"facultyId\":1}";
        DepartmentDto entityDto = DEPARTMENT_DTO2;
        entityDto.setId(2);
        
        when(departmentService.create(any(DepartmentDto.class))).thenReturn(entityDto);

        this.mockMvc.perform(post("/api/departments").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(5)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("department2")))
                .andExpect(jsonPath("$.description", is("bla bla bla 2")))
                .andExpect(jsonPath("$.facultyTitle", is("faculty1")))
                .andExpect(jsonPath("$.facultyId", is(2)));
    }

    @Test
    void whenUpdateEntityWithNotCorrectValues_thenExpectError() throws Exception {
         String entity = "{\"id\":2,"
                       + "\"title\":\"\","
                       + "\"description\":\"bla bla bla 2\","
                       + "\"facultyTitle\":\"faculty1\","
                       + "\"facultyId\":1}";
        
        this.mockMvc.perform(post("/api/departments").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors.title", is("Title can`t be blank!")));
    }

}
