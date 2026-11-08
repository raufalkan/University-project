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
public class AuditoryRestControllerSystemTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DataSet(value = "auditory/auditories.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        this.mockMvc.perform(get("/api/auditories").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$", hasSize(6)))
            .andExpect(jsonPath("$[0].id", is(1)))
            .andExpect(jsonPath("$[0].auditoryNumber", is("101a")))
            .andExpect(jsonPath("$[0].auditoryTypeTitle", is("Lecture")))
            .andExpect(jsonPath("$[0].auditoryTypeId", is(1)))
            .andExpect(jsonPath("$[0].maxCapacity", is(100)))
            .andExpect(jsonPath("$[0].description", is("bla bla bla 1")))
            .andExpect(jsonPath("$[1].id", is(2)))
            .andExpect(jsonPath("$[1].auditoryNumber", is("102a")))
            .andExpect(jsonPath("$[1].auditoryTypeTitle", is("Practic")))
            .andExpect(jsonPath("$[1].auditoryTypeId", is(2)))
            .andExpect(jsonPath("$[1].maxCapacity", is(50)))
            .andExpect(jsonPath("$[1].description", is("bla bla bla 2")));
    }

    @Test
    @DataSet(value = "auditory/auditories.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenPerformEntityAndIdRequest_thenExpectEntityById() throws Exception {
        this.mockMvc.perform(get("/api/auditories/2").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$", aMapWithSize(6)))
            .andExpect(jsonPath("$.id", is(2)))
            .andExpect(jsonPath("$.auditoryNumber", is("102a")))
            .andExpect(jsonPath("$.auditoryTypeTitle", is("Practic")))
            .andExpect(jsonPath("$.auditoryTypeId", is(2)))
            .andExpect(jsonPath("$.maxCapacity", is(50)))
            .andExpect(jsonPath("$.description", is("bla bla bla 2")));
    }

    @Test
    @DataSet(value = "auditory/auditories.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        String entity = "{\"id\":2,"
            + "\"auditoryNumber\":\"102a\","
            + "\"auditoryTypeTitle\":\"Practic\","
            + "\"auditoryTypeId\":2,"
            + "\"maxCapacity\":50,"
            + "\"description\":\"bla bla bla 2\"}";
        this.mockMvc.perform(post("/api/auditories")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$", aMapWithSize(6)))
            .andExpect(jsonPath("$.id", is(2)))
            .andExpect(jsonPath("$.auditoryNumber", is("102a")))
            .andExpect(jsonPath("$.auditoryTypeTitle", is("Practic")))
            .andExpect(jsonPath("$.auditoryTypeId", is(2)))
            .andExpect(jsonPath("$.maxCapacity", is(50)))
            .andExpect(jsonPath("$.description", is("bla bla bla 2")));
    }

    @Test
    @DataSet(value = "auditory-type/auditorytypes.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        String entity = "{\"id\":0,"
            + "\"auditoryNumber\":\"102a\","
            + "\"auditoryTypeTitle\":\"Practic\","
            + "\"auditoryTypeId\":2,"
            + "\"maxCapacity\":50,"
            + "\"description\":\"bla bla bla 2\"}";
        this.mockMvc.perform(post("/api/auditories")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$", aMapWithSize(6)))
            .andExpect(jsonPath("$.auditoryNumber", is("102a")))
            .andExpect(jsonPath("$.auditoryTypeTitle", is("Practic")))
            .andExpect(jsonPath("$.auditoryTypeId", is(2)))
            .andExpect(jsonPath("$.maxCapacity", is(50)))
            .andExpect(jsonPath("$.description", is("bla bla bla 2")));
    }

    @Test
    @DataSet(value = "auditory/auditories.yml",
        cleanBefore = true,
        skipCleaningFor = "flyway_schema_history")
    void whenCreateEntityWithNotCorrectValues_thenExpectError() throws Exception {
        String entity = "{\"id\":0,"
            + "\"auditoryNumber\":\"102a\","
            + "\"auditoryTypeTitle\":\"qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklf\","
            + "\"auditoryTypeId\":2,"
            + "\"maxCapacity\":50,"
            + "\"description\":\"bla bla bla 2\"}";
        this.mockMvc.perform(post("/api/auditories")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$.errors", aMapWithSize(2)))
            .andExpect(jsonPath("$.errors.auditoryNumber", is("Auditory number is already exists!")))
            .andExpect(
                jsonPath("$.errors.auditoryTypeTitle", is("Maximum length of auditory type title group is 45!")));
    }

    @Test
    void whenCreateEntityWithNotCorrectValues_thenExpectError2() throws Exception {
        String entity = "{\"id\":0,"
            + "\"auditoryNumber\":\"qwertyuiopasdfghjklzx\","
            + "\"auditoryTypeTitle\":\"Practic\","
            + "\"auditoryTypeId\":2,"
            + "\"maxCapacity\":50,"
            + "\"description\":\"bla bla bla 2\"}";
        this.mockMvc.perform(post("/api/auditories")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$.errors", aMapWithSize(1)))
            .andExpect(jsonPath("$.errors.auditoryNumber", is("Maximum length is 20!")));
    }

    @Test
    void whenCreateEntityWithNotCorrectTitle_thenExpectError() throws Exception {
        String entity = "{\"id\":0,"
            + "\"auditoryNumber\":\"\","
            + "\"auditoryTypeTitle\":\"Practic\","
            + "\"auditoryTypeId\":2,"
            + "\"maxCapacity\":50,"
            + "\"description\":\"bla bla bla 2\"}";
        this.mockMvc.perform(post("/api/auditories")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$.errors", aMapWithSize(1)))
            .andExpect(jsonPath("$.errors.auditoryNumber", is("Title of auditory can`t be blank!")));
    }
}
