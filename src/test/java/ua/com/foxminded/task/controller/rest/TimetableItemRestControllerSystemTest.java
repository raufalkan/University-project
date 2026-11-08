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
public class TimetableItemRestControllerSystemTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DataSet(value = "timetableItem/timetableItemsExtend.yml", 
             cleanBefore = true,
             cleanAfter = true,
             skipCleaningFor = "flyway_schema_history")
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        this.mockMvc.perform(get("/api/timetable-items").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(20)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].subjectTitle", is("Programming")))
                .andExpect(jsonPath("$[0].subjectId", is(1)))
                .andExpect(jsonPath("$[0].auditoryTitle", is("101a")))
                .andExpect(jsonPath("$[0].auditoryId", is(1)))
                .andExpect(jsonPath("$[0].groups.[0].title", is("group1")))
                .andExpect(jsonPath("$[0].groups.[1].title", is("group2")))
                .andExpect(jsonPath("$[0].lectureTitle", is("1")))
                .andExpect(jsonPath("$[0].lectureId", is(1)))
                .andExpect(jsonPath("$[0].date", is("2020-06-25")))
                .andExpect(jsonPath("$[0].teacherTitle", is("firstNameTe1")))
                .andExpect(jsonPath("$[0].teacherId", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].subjectTitle", is("Phisics")))
                .andExpect(jsonPath("$[1].subjectId", is(2)))
                .andExpect(jsonPath("$[1].auditoryTitle", is("102a")))
                .andExpect(jsonPath("$[1].auditoryId", is(2)))
                .andExpect(jsonPath("$[1].groups.[0].title", is("group3")))
                .andExpect(jsonPath("$[1].groups.[1].title", is("group4")))
                .andExpect(jsonPath("$[1].lectureTitle", is("1")))
                .andExpect(jsonPath("$[1].lectureId", is(1)))
                .andExpect(jsonPath("$[1].date", is("2020-06-25")))
                .andExpect(jsonPath("$[1].teacherTitle", is("firstNameTe2")))
                .andExpect(jsonPath("$[1].teacherId", is(2)));
    }

    @Test
    @DataSet(value = "timetableItem/timetableItemsExtend.yml", 
             cleanBefore = true,
             cleanAfter = true,
             skipCleaningFor = "flyway_schema_history")
    void whenPerformEntityAndIdRequest_thenExpectEntityById() throws Exception {
        this.mockMvc.perform(get("/api/timetable-items/2").accept(MediaType.APPLICATION_JSON))
                  .andExpect(status().isOk())
                  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                  .andDo(print())
                  .andExpect(jsonPath("$", aMapWithSize(11)))
                  .andExpect(jsonPath("$.id", is(2)))
                  .andExpect(jsonPath("$.subjectTitle", is("Phisics")))
                  .andExpect(jsonPath("$.subjectId", is(2)))
                  .andExpect(jsonPath("$.auditoryTitle", is("102a")))
                  .andExpect(jsonPath("$.auditoryId", is(2)))
                  .andExpect(jsonPath("$.groups.[0].title", is("group3")))
                  .andExpect(jsonPath("$.groups.[1].title", is("group4")))
                  .andExpect(jsonPath("$.lectureTitle", is("1")))
                  .andExpect(jsonPath("$.lectureId", is(1)))
                  .andExpect(jsonPath("$.date", is("2020-06-25")))
                  .andExpect(jsonPath("$.teacherTitle", is("firstNameTe2")))
                  .andExpect(jsonPath("$.teacherId", is(2)));
    }

    @Test
    @DataSet(value = "timetableItem/timetableItemsExtend.yml", 
             cleanBefore = true,
             cleanAfter = true,
             skipCleaningFor = "flyway_schema_history")
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
      String entity = "{\"id\":2,"
                    + "\"subjectTitle\":\"Phisics\","
                    + "\"subjectId\":2,"
                    + "\"auditoryTitle\":\"102a\","
                    + "\"auditoryId\":2,"
                    + "\"groups\":[{\"id\":3,\"title\":\"G153\",\"yearEntry\":2015},"
                    +             "{\"id\":4,\"title\":\"G154\",\"yearEntry\":2015}],"
                    + "\"lectureTitle\":\"1\","
                    + "\"lectureId\":1,"
                    + "\"date\":\"2020-06-25\","
                    + "\"teacherTitle\":\"firstNameTe2\","
                    + "\"teacherId\":2}";
      this.mockMvc.perform(post("/api/timetable-items")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(11)))
                .andExpect(jsonPath("$.subjectTitle", is("Phisics")))
                .andExpect(jsonPath("$.subjectId", is(2)))
                .andExpect(jsonPath("$.auditoryTitle", is("102a")))
                .andExpect(jsonPath("$.auditoryId", is(2)))
                .andExpect(jsonPath("$.groups.[0].title", is("group3")))
                .andExpect(jsonPath("$.groups.[1].title", is("group4")))
                .andExpect(jsonPath("$.lectureTitle", is("1")))
                .andExpect(jsonPath("$.lectureId", is(1)))
                .andExpect(jsonPath("$.date", is("2020-06-25")))
                .andExpect(jsonPath("$.teacherTitle", is("firstNameTe2")))
                .andExpect(jsonPath("$.teacherId", is(2)));
  }

    @Test
    @DataSet(value = "timetableItem/timetableItems.yml",
             cleanBefore = true,
             cleanAfter = true,
             skipCleaningFor = "flyway_schema_history")
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
      String entity = "{\"id\":0,"
                    + "\"subjectTitle\":\"Phisics\","
                    + "\"subjectId\":2,"
                    + "\"auditoryTitle\":\"102a\","
                    + "\"auditoryId\":2,"
                    + "\"groups\":[{\"id\":3,\"title\":\"G153\",\"yearEntry\":2015},"
                    +             "{\"id\":4,\"title\":\"G154\",\"yearEntry\":2015}],"
                    + "\"lectureTitle\":\"1\","
                    + "\"lectureId\":1,"
                    + "\"date\":\"2021-06-25\","
                    + "\"teacherTitle\":\"firstNameTe2\","
                    + "\"teacherId\":2}";
      this.mockMvc.perform(post("/api/timetable-items")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(11)))
                .andExpect(jsonPath("$.subjectTitle", is("Phisics")))
                .andExpect(jsonPath("$.subjectId", is(2)))
                .andExpect(jsonPath("$.auditoryTitle", is("102a")))
                .andExpect(jsonPath("$.auditoryId", is(2)))
                .andExpect(jsonPath("$.groups.[0].title", is("group3")))
                .andExpect(jsonPath("$.groups.[1].title", is("group4")))
                .andExpect(jsonPath("$.lectureTitle", is("1")))
                .andExpect(jsonPath("$.lectureId", is(1)))
                .andExpect(jsonPath("$.date", is("2021-06-25")))
                .andExpect(jsonPath("$.teacherTitle", is("firstNameTe2")))
                .andExpect(jsonPath("$.teacherId", is(2)));
  }
 
    @Test
    @DataSet(value = "timetableItem/timetableItemsExtend.yml", 
             cleanBefore = true,
             cleanAfter = true,
             skipCleaningFor = "flyway_schema_history")
    void whenCreateEntityWithNotCorrectValues_thenExpectError() throws Exception {
      String entity = "{\"id\":0,"
                    + "\"subjectTitle\":\"Phisics\","
                    + "\"subjectId\":2,"
                    + "\"auditoryTitle\":\"102a\","
                    + "\"auditoryId\":2,"
                    + "\"groups\":[{\"id\":3,\"title\":\"G153\",\"yearEntry\":2015},"
                    +             "{\"id\":4,\"title\":\"G154\",\"yearEntry\":2015}],"
                    + "\"lectureTitle\":\"1\","
                    + "\"lectureId\":1,"
                    + "\"date\":\"2020-06-25\","
                    + "\"teacherTitle\":\"firstNameTe2\","
                    + "\"teacherId\":2}";
      this.mockMvc.perform(post("/api/timetable-items")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(2)))
                .andExpect(jsonPath("$.errors.teacherId", is("Teacher will be busy at the time!")))
                .andExpect(jsonPath("$.errors.auditoryId", is("Auditory will be busy at the time!")));
  }
  
    @Test
    void whenCreateEntityWithNotCorrectValues_thenExpectError2() throws Exception {
      String entity = "{\"id\":0,"
                    + "\"subjectTitle\":\"qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklz\","
                    + "\"subjectId\":2,"
                    + "\"auditoryTitle\":\"qwertyuiopasdfghjklzx\","
                    + "\"auditoryId\":2,"
                    + "\"groups\":[{\"id\":3,\"title\":\"G153\",\"yearEntry\":2015},"
                    +             "{\"id\":4,\"title\":\"G154\",\"yearEntry\":2015}],"
                    + "\"lectureTitle\":\"qwerty\","
                    + "\"lectureId\":1,"
                    + "\"date\":\"2021-06-25\","
                    + "\"teacherTitle\":\"qwertyuiopasdfghjklzx\","
                    + "\"teacherId\":2}";
      this.mockMvc.perform(post("/api/timetable-items")
                .content(entity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(4)))
                .andExpect(jsonPath("$.errors.subjectTitle", is("Maximum length of title subject is 45!")))
                .andExpect(jsonPath("$.errors.auditoryTitle", is("Maximum length of title auditory is 20!")))
                .andExpect(jsonPath("$.errors.lectureTitle", is("Maximum length of title of lecture is 5!")))
                .andExpect(jsonPath("$.errors.teacherTitle", is("Maximum length of teacher first name is 20!")));
  }

}
