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
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.domain.repository.dto.LectureDtoModelRepository;
import ua.com.foxminded.task.service.LectureService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

@WebMvcTest(LectureController.class)
@Import(TestMvcConfig.class)
public class LectureRestControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;
    @MockBean
    private LectureService lectureService;
    @MockBean
    @Qualifier("uniqueValidationCommandMap")
    private Map<String, Command> uniqueValidationCommandMap;
    
    private static LectureDto LECTURE_DTO1 = LectureDtoModelRepository.getModel1();
    private static LectureDto LECTURE_DTO2 = LectureDtoModelRepository.getModel2();

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        LECTURE_DTO1.setId(1);
        LECTURE_DTO2.setId(2);
        List<LectureDto> entityDtos = Arrays.asList(LECTURE_DTO1, LECTURE_DTO2);
       
        when(lectureService.findAllDto()).thenReturn(entityDtos);

        this.mockMvc.perform(get("/api/lecturies").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].number", is("1")))
                .andExpect(jsonPath("$[0].startTime", is("07:45:00")))
                .andExpect(jsonPath("$[0].endTime", is("09:20:00")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].number", is("2")))
                .andExpect(jsonPath("$[1].startTime", is("09:30:00")))
                .andExpect(jsonPath("$[1].endTime", is("11:05:00")));
    }

    @Test
    void whenPerformEntityAndIdRequest_thenExpectEntityById() throws Exception {
        LECTURE_DTO2.setId(2);

        when(lectureService.findByIdDto(2)).thenReturn(LECTURE_DTO2);

        this.mockMvc.perform(get("/api/lecturies/2").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(jsonPath("$", aMapWithSize(4)))
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.number", is("2")))
        .andExpect(jsonPath("$.startTime", is("09:30:00")))
        .andExpect(jsonPath("$.endTime", is("11:05:00")));
    }

    @Test
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        String entity = "{\"id\":2,"
                      + "\"number\":\"2\","
                      + "\"startTime\":\"09:30:00\","
                      + "\"endTime\":\"11:05:00\"}";
        LectureDto entityDto = LECTURE_DTO2;
        entityDto.setId(2);

        when(lectureService.update(any(LectureDto.class))).thenReturn(entityDto);

        this.mockMvc.perform(post("/api/lecturies").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(4)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.number", is("2")))
                .andExpect(jsonPath("$.startTime", is("09:30:00")))
                .andExpect(jsonPath("$.endTime", is("11:05:00")));
    }

    @Test
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        String entity = "{\"id\":0,"
                      + "\"number\":\"2\","
                      + "\"startTime\":\"09:30:00\","
                      + "\"endTime\":\"11:05:00\"}";
        LectureDto entityDto = LECTURE_DTO2;
        entityDto.setId(2);
        
        when(lectureService.create(any(LectureDto.class))).thenReturn(entityDto);

        this.mockMvc.perform(post("/api/lecturies").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(4)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.number", is("2")))
                .andExpect(jsonPath("$.startTime", is("09:30:00")))
                .andExpect(jsonPath("$.endTime", is("11:05:00")));
    }

    @Test
    void whenUpdateEntityWithNotCorrectValues_thenExpectError() throws Exception {
        String entity = "{\"id\":2,"
                      + "\"number\":\"\","
                      + "\"startTime\":\"09:30:00\","
                      + "\"endTime\":\"11:05:00\"}";
        
        this.mockMvc.perform(post("/api/lecturies").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors.number", is("Number name can`t be blank!")));
    }

}
