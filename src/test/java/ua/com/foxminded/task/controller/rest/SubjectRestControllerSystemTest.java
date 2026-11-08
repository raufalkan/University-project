package ua.com.foxminded.task.controller.rest;

import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.oneOf;
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
public class SubjectRestControllerSystemTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DataSet(value = "subject/subjects.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        this.mockMvc.perform(get("/api/subjects").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$", hasSize(4)))
            .andExpect(jsonPath("$[0].id", oneOf(1, 2, 3, 4)))
            .andExpect(jsonPath("$[0].title", oneOf("Programming", "Phisics", "Biologic", "Mathmatics")))
            .andExpect(jsonPath("$[1].id", oneOf(1, 2, 3, 4)))
            .andExpect(jsonPath("$[1].title", oneOf("Programming", "Phisics", "Biologic", "Mathmatics")));
    }

    @Test
    @DataSet(value = "subject/subjects.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenPerformEntityAndIdRequest_thenExpectEntityById() throws Exception {
        this.mockMvc.perform(get("/api/subjects/2").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$", aMapWithSize(2)))
            .andExpect(jsonPath("$.id", is(2)))
            .andExpect(jsonPath("$.title", is("Phisics")));
    }

    @Test
    @DataSet(value = "subject/subjects.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        String entity = "{\"id\":2,"
            + "\"title\":\"Phisics\"}";
        this.mockMvc.perform(post("/api/subjects")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$", aMapWithSize(2)))
            .andExpect(jsonPath("$.id", is(2)))
            .andExpect(jsonPath("$.title", is("Phisics")));
    }

    @Test
    @DataSet(cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        String entity = "{\"id\":0,"
            + "\"title\":\"Phisics\"}";
        this.mockMvc.perform(post("/api/subjects")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$", aMapWithSize(2)))
            .andExpect(jsonPath("$.title", is("Phisics")));
    }

    @Test
    @DataSet(value = "subject/subjects.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenCreateEntityWithNotCorrectValues_thenExpectError() throws Exception {
        String entity = "{\"id\":0,"
            + "\"title\":\"Phisics\"}";
        this.mockMvc.perform(post("/api/subjects")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$.errors", aMapWithSize(1)))
            .andExpect(jsonPath("$.errors.title", is("Subject title is already exists!")));
    }

    @Test
    void whenCreateEntityWithNotCorrectValues_thenExpectError2() throws Exception {
        String entity = "{\"id\":0,"
            + "\"title\":\"qwertyuiopasdfghjklzx\"}";
        this.mockMvc.perform(post("/api/subjects")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$.errors", aMapWithSize(1)))
            .andExpect(jsonPath("$.errors.title", is("Maximum length is 20!")));
    }

    @Test
    void whenCreateEntityWithNotCorrectTitle_thenExpectError() throws Exception {
        String entity = "{\"id\":0,"
            + "\"title\":\"\"}";
        this.mockMvc.perform(post("/api/subjects")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$.errors", aMapWithSize(1)))
            .andExpect(jsonPath("$.errors.title", is("Title can`t be blank!")));
    }
}
