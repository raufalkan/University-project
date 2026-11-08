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
public class TeacherRestControllerSystemTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DataSet(value = "teacher/teachers.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        this.mockMvc.perform(get("/api/teachers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("firstNameTe1")))
                .andExpect(jsonPath("$[0].middleName", is("middleNameTe1")))
                .andExpect(jsonPath("$[0].lastName", is("lastNameTe1")))
                .andExpect(jsonPath("$[0].birthday", is("1980-06-25")))
                .andExpect(jsonPath("$[0].idFees", is(111111166)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("firstNameTe2")))
                .andExpect(jsonPath("$[1].middleName", is("middleNameTe2")))
                .andExpect(jsonPath("$[1].lastName", is("lastNameTe2")))
                .andExpect(jsonPath("$[1].birthday", is("1966-06-25")))
                .andExpect(jsonPath("$[1].idFees", is(211111111)));
    }

    @Test
    @DataSet(value = "teacher/teachers.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenPerformEntityAndIdRequest_thenExpectEntityById() throws Exception {
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
                  .andExpect(jsonPath("$.idFees", is(211111111)));
    }

    @Test
    @DataSet(value = "teacher/teachers.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
      String entity = "{\"id\":2,"
                    + "\"firstName\":\"firstNameTe2\","
                    + "\"middleName\":\"middleNameTe2\","
                    + "\"lastName\":\"lastNameTe2\","
                    + "\"birthday\":\"1966-06-25\","
                    + "\"idFees\":211111111,"
                    + "\"departmentTitle\":\"department1\","
                    + "\"departmentId\":0,"
                    + "\"subjects\":[]}";
      this.mockMvc.perform(post("/api/teachers")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(9)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstName", is("firstNameTe2")))
                .andExpect(jsonPath("$.middleName", is("middleNameTe2")))
                .andExpect(jsonPath("$.lastName", is("lastNameTe2")))
                .andExpect(jsonPath("$.birthday", is("1966-06-25")))
                .andExpect(jsonPath("$.idFees", is(211111111)));
  }

    @Test
    @DataSet(value = {"department/departments.yml", "subject/subjects.yml"},
             cleanBefore = true, 
             cleanAfter = true,
             skipCleaningFor = "flyway_schema_history")
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
      String entity = "{\"id\":0,"
              + "\"firstName\":\"firstNameTe2\","
              + "\"middleName\":\"middleNameTe2\","
              + "\"lastName\":\"lastNameTe2\","
              + "\"birthday\":\"1966-06-25\","
              + "\"idFees\":211111111,"
              + "\"departmentTitle\":\"department1\","
              + "\"departmentId\":10,"
              + "\"subjects\":[{\"id\":1,"
              + "\"title\":\"Programming\"}]}";
      this.mockMvc.perform(post("/api/teachers")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(9)))
                .andExpect(jsonPath("$.firstName", is("firstNameTe2")))
                .andExpect(jsonPath("$.middleName", is("middleNameTe2")))
                .andExpect(jsonPath("$.lastName", is("lastNameTe2")))
                .andExpect(jsonPath("$.birthday", is("1966-06-25")))
                .andExpect(jsonPath("$.idFees", is(211111111)))
                .andExpect(jsonPath("$.departmentTitle", is("department1")))
                .andExpect(jsonPath("$.departmentId", is(10)))
                .andExpect(jsonPath("$.subjects.[0].title", is("Programming")));
  }
 
    @Test
    @DataSet(value = "teacher/teachers.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenCreateEntityWithNotCorrectValues_thenExpectError() throws Exception {
      String entity = "{\"id\":0,"
                    + "\"firstName\":\"qwertyuiopasdfghjklzxcvbnm\","
                    + "\"middleName\":\"qwertyuiopasdfghjklzxcvbnm\","
                    + "\"lastName\":\"qwertyuiopasdfghjklzxcvbnm\","
                    + "\"birthday\":\"1966-06-25\","
                    + "\"idFees\":211111111,"
                    + "\"departmentTitle\":\"qwertyuiopasdfghjklzxcvbnm\","
                    + "\"departmentId\":1,"
                    + "\"subjects\":[{\"id\":1,"
                    + "\"title\":\"Programming\"}]}";
      this.mockMvc.perform(post("/api/teachers")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(5)))
                .andExpect(jsonPath("$.errors.idFees", is("Teacher id fees is already exists!")))
                .andExpect(jsonPath("$.errors.firstName", is("Maximum length of first name is 20!")))
                .andExpect(jsonPath("$.errors.middleName", is("Maximum length of middle name is 20!")))
                .andExpect(jsonPath("$.errors.lastName", is("Maximum length of last name is 20!")))
                .andExpect(jsonPath("$.errors.departmentTitle", is("Maximum length of title department is 20!")));
  }
  
    @Test
    void whenCreateEntityWithNotCorrectValues_thenExpectError2() throws Exception {
      String entity = "{\"id\":0,"
                    + "\"firstName\":\"\","
                    + "\"middleName\":\"middleNameTe2\","
                    + "\"lastName\":\"lastNameTe2\","
                    + "\"birthday\":\"1966-06-25\","
                    + "\"idFees\":2111111112,"
                    + "\"departmentTitle\":\"department1\","
                    + "\"departmentId\":1,"
                    + "\"subjects\":[{\"id\":1,"
                    + "\"title\":\"Programming\"}]}";
      this.mockMvc.perform(post("/api/teachers")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(2)))
                .andExpect(jsonPath("$.errors.firstName", is("First name can`t be blank!")))
                .andExpect(jsonPath("$.errors.idFees", is("Value is 9 number!")));
  }
  
    @Test
    void whenCreateEntityWithNotCorrectTitle_thenExpectError() throws Exception {
      String entity = "{\"id\":0,"
                    + "\"firstName\":\"firstNameTe2\","
                    + "\"middleName\":\"middleNameTe2\","
                    + "\"lastName\":\"lastNameTe2\","
                    + "\"birthday\":\"1966-06-25\","
                    + "\"idFees\":21111111,"
                    + "\"departmentTitle\":\"department1\","
                    + "\"departmentId\":1,"
                    + "\"subjects\":[{\"id\":1,"
                    + "\"title\":\"Programming\"}]}";
      this.mockMvc.perform(post("/api/teachers")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors.idFees", is("Value is 9 number!")));
  }
}
