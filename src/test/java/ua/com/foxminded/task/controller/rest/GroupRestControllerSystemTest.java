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
public class GroupRestControllerSystemTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DataSet(value = "group/groups.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenPerformGroupsRequest_thenExpectListOfGroups() throws Exception {
        this.mockMvc.perform(get("/api/groups").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("group1")))
                .andExpect(jsonPath("$[0].yearEntry", is(2016)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("group2")))
                .andExpect(jsonPath("$[1].yearEntry", is(2018)));
    }

    @Test
    @DataSet(value = "group/groups.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenPerformGroupAndIdRequest_thenExpectGroupById() throws Exception {
        this.mockMvc.perform(get("/api/groups/2").accept(MediaType.APPLICATION_JSON))
                  .andExpect(status().isOk())
                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                  .andDo(print())
                  .andExpect(jsonPath("$", aMapWithSize(3)))
                  .andExpect(jsonPath("$.id", is(2)))
                  .andExpect(jsonPath("$.title", is("group2")))
                  .andExpect(jsonPath("$.yearEntry", is(2018)));
    }

    @Test
    @DataSet(value = "group/groups.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenPerformPostGroupsRequest_thenUpdateGroup() throws Exception {
      String group = "{\"id\":2,"
                   + "\"title\":\"group2\","
                   + "\"yearEntry\":2001}";
      this.mockMvc.perform(post("/api/groups")
                .content(group)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("group2")))
                .andExpect(jsonPath("$.yearEntry", is(2001)));
  }

    @Test
    @DataSet(cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenPerformPostGroupsRequestWithIdZero_thenCreateGroup() throws Exception {
      String group = "{\"id\":0,"
                   + "\"title\":\"group33\","
                   + "\"yearEntry\":2013}";
      this.mockMvc.perform(post("/api/groups")
                .content(group)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.title", is("group33")))
                .andExpect(jsonPath("$.yearEntry", is(2013)));
  }
 
    @Test
    @DataSet(value = "group/groups.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenUpdateGroupWithNotCorrectValues_thenExpectError() throws Exception {
      String group = "{\"id\":2,"
                   + "\"title\":\"group1\","
                   + "\"yearEntry\":99999}";
      this.mockMvc.perform(post("/api/groups")
                .content(group)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(2)))
                .andExpect(jsonPath("$.errors.title", is("Title is already exists!")))
                .andExpect(jsonPath("$.errors.yearEntry", is("Year of entry is not correct!")));
  }
  
    @Test
    void whenUpdateGroupWithNotCorrectValues_thenExpectError2() throws Exception {
      String group = "{\"id\":2,"
                   + "\"title\":\"qwertyuiopasdfghjklzxcvbnm\","
                   + "\"yearEntry\":999}";
      this.mockMvc.perform(post("/api/groups")
                .content(group)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(2)))
                .andExpect(jsonPath("$.errors.title", is("Maximum length is 20!")))
                .andExpect(jsonPath("$.errors.yearEntry", is("Year of entry is not correct!")));
  }
  
    @Test
    void whenUpdateGroupWithNotCorrectTitle_thenExpectError() throws Exception {
      String group = "{\"id\":2,"
                   + "\"title\":\"\","
                   + "\"yearEntry\":2018}";
      this.mockMvc.perform(post("/api/groups")
                .content(group)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors.title", is("Title can`t be blank!")));
  }

}
