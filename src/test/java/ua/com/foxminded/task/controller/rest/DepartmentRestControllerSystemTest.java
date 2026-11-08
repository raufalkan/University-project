package ua.com.foxminded.task.controller.rest;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DBRider
@SpringBootTest
public class DepartmentRestControllerSystemTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DataSet(value = "department/departments.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        this.mockMvc.perform(get("/api/departments").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$", hasSize(3)))
            .andExpect(jsonPath("$[0].id", is(10)))
            .andExpect(jsonPath("$[0].title", is("department1")))
            .andExpect(jsonPath("$[0].description", is("bla bla bla 1")))
            .andExpect(jsonPath("$[1].id", is(20)))
            .andExpect(jsonPath("$[1].title", is("department2")))
            .andExpect(jsonPath("$[1].description", is("bla bla bla 2")));
    }

    @Test
    @DataSet(value = "department/departments.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenPerformEntityAndIdRequest_thenExpectEntityById() throws Exception {
        this.mockMvc.perform(get("/api/departments/20").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$", aMapWithSize(5)))
            .andExpect(jsonPath("$.id", is(20)))
            .andExpect(jsonPath("$.title", is("department2")))
            .andExpect(jsonPath("$.description", is("bla bla bla 2")));
    }

    @Test
    @DataSet(value = {"department/departments.yml", "faculty/faculties.yml"},
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        String entity = "{\"id\":20,"
            + "\"title\":\"department2\","
            + "\"description\":\"bla bla bla 2\","
            + "\"facultyTitle\":\"faculty1\","
            + "\"facultyId\":2}";
        this.mockMvc.perform(post("/api/departments")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$", aMapWithSize(5)))
            .andExpect(jsonPath("$.id", is(20)))
            .andExpect(jsonPath("$.title", is("department2")))
            .andExpect(jsonPath("$.description", is("bla bla bla 2")))
            .andExpect(jsonPath("$.facultyTitle", is("faculty2")))
            .andExpect(jsonPath("$.facultyId", is(2)));
    }

    @Test
    @DataSet(value = {"department/departments.yml", "faculty/faculties.yml"},
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        String entity = "{\"id\":0,"
            + "\"title\":\"department20\","
            + "\"description\":\"bla bla bla 2\","
            + "\"facultyTitle\":\"faculty2\","
            + "\"facultyId\":2}";
        this.mockMvc.perform(post("/api/departments")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$", aMapWithSize(5)))
            .andExpect(jsonPath("$.title", is("department20")))
            .andExpect(jsonPath("$.description", is("bla bla bla 2")))
            .andExpect(jsonPath("$.facultyTitle", is("faculty2")))
            .andExpect(jsonPath("$.facultyId", is(2)));
    }

    @Test
    @DataSet(value = "department/departments.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenCreateEntityWithNotCorrectValues_thenExpectError() throws Exception {
        String entity = "{\"id\":0,"
            + "\"title\":\"department2\","
            + "\"description\":\"bla bla bla 2\","
            + "\"facultyTitle\":\"qwertyuiopasdfghjklzx\","
            + "\"facultyId\":1}";
        this.mockMvc.perform(post("/api/departments")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$.errors", aMapWithSize(2)))
            .andExpect(jsonPath("$.errors.title", is("Department title is already exists!")))
            .andExpect(jsonPath("$.errors.facultyTitle", is("Maximum length of faculty title is 20!")));
    }

    @Test
    void whenCreateEntityWithNotCorrectValues_thenExpectError2() throws Exception {
        String entity = "{\"id\":0,"
            + "\"title\":\"qwertyuiopasdfghjklzx\","
            + "\"description\":\"bla bla bla 2\","
            + "\"facultyTitle\":\"faculty1\","
            + "\"facultyId\":1}";
        this.mockMvc.perform(post("/api/departments")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$.errors", aMapWithSize(1)))
            .andExpect(jsonPath("$.errors.title", is("Maximum length is 20!")));
    }
}
