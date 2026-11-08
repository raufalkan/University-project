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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

@DBRider
@SpringBootTest
public class FacultyRestControllerSystemTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DataSet(value = "faculty/faculties.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        this.mockMvc.perform(get("/api/faculties").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("faculty1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("faculty2")));
    }

    @Test
    @DataSet(value = "faculty/faculties.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenPerformEntityAndIdRequest_thenExpectEntityById() throws Exception {
        this.mockMvc.perform(get("/api/faculties/2").accept(MediaType.APPLICATION_JSON))
                  .andExpect(status().isOk())
                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                  .andDo(print())
                  .andExpect(jsonPath("$", aMapWithSize(2)))
                  .andExpect(jsonPath("$.title", is("faculty2")));
    }

    @Test
    @DataSet(value = "faculty/faculties.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
      String entity = "{\"id\":2,"
                    + "\"title\":\"faculty2\"}";
      this.mockMvc.perform(post("/api/faculties")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("faculty2")));
  }

    @Test
    @DataSet(cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
      String entity = "{\"id\":0,"
                    + "\"title\":\"faculty2\"}";
      this.mockMvc.perform(post("/api/faculties")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.title", is("faculty2")));
  }
 
    @Test
    @DataSet(value = "faculty/faculties.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenCreateEntityWithNotCorrectValues_thenExpectError() throws Exception {
      String entity = "{\"id\":0,"
                    + "\"title\":\"faculty2\"}";
      this.mockMvc.perform(post("/api/faculties")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors.title", is("Faculty title is already exists!")));
  }
  
    @Test
    void whenCreateEntityWithNotCorrectValues_thenExpectError2() throws Exception {
      String entity = "{\"id\":0,"
                    + "\"title\":\"qwertyuiopasdfghjklzx\"}";
      this.mockMvc.perform(post("/api/faculties")
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
      this.mockMvc.perform(post("/api/faculties")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors.title", is("Title can`t be blank!")));
  }

}
