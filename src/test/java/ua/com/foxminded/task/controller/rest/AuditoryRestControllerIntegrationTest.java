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
import ua.com.foxminded.task.domain.dto.AuditoryDto;
import ua.com.foxminded.task.domain.repository.dto.AuditoryDtoModelRepository;
import ua.com.foxminded.task.service.AuditoryService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

@WebMvcTest(AuditoryController.class)
@Import(TestMvcConfig.class)
public class AuditoryRestControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;
    @MockBean
    private AuditoryService auditoryService;
    @MockBean
    @Qualifier("uniqueValidationCommandMap")
    private Map<String, Command> uniqueValidationCommandMap;
    
    private static AuditoryDto AUDITORY_DTO1 = AuditoryDtoModelRepository.getModel1();
    private static AuditoryDto AUDITORY_DTO2 = AuditoryDtoModelRepository.getModel2();

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        AUDITORY_DTO1.setId(1);
        AUDITORY_DTO2.setId(2);
        List<AuditoryDto> entityDtos = Arrays.asList(AUDITORY_DTO1, AUDITORY_DTO2);
       
        when(auditoryService.findAllDto()).thenReturn(entityDtos);

        this.mockMvc.perform(get("/api/auditories").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
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
    void whenPerformEntityAndIdRequest_thenExpectEntityById() throws Exception {
        AUDITORY_DTO2.setId(2);

        when(auditoryService.findByIdDto(2)).thenReturn(AUDITORY_DTO2);

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
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        String entity = "{\"id\":2,"
                      + "\"auditoryNumber\":\"102a\","
                      + "\"auditoryTypeTitle\":\"Practic\","
                      + "\"auditoryTypeId\":2,"
                      + "\"maxCapacity\":50,"
                      + "\"description\":\"bla bla bla 2\"}";
        AuditoryDto entityDto = AUDITORY_DTO2;
        entityDto.setId(2);

        when(auditoryService.update(any(AuditoryDto.class))).thenReturn(entityDto);

        this.mockMvc.perform(post("/api/auditories").content(entity).contentType(MediaType.APPLICATION_JSON))
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
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        String entity = "{\"id\":0,"
                      + "\"auditoryNumber\":\"102a\","
                      + "\"auditoryTypeTitle\":\"Practic\","
                      + "\"auditoryTypeId\":2,"
                      + "\"maxCapacity\":50,"
                      + "\"description\":\"bla bla bla 2\"}";
        AuditoryDto entityDto = AUDITORY_DTO2;
        entityDto.setId(2);
        
        when(auditoryService.create(any(AuditoryDto.class))).thenReturn(entityDto);

        this.mockMvc.perform(post("/api/auditories").content(entity).contentType(MediaType.APPLICATION_JSON))
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
    void whenUpdateEntityWithNotCorrectValues_thenExpectError() throws Exception {
        String entity = "{\"id\":0,"
                      + "\"auditoryNumber\":\"\","
                      + "\"auditoryTypeTitle\":\"Practic\","
                      + "\"auditoryTypeId\":2,"
                      + "\"maxCapacity\":50,"
                      + "\"description\":\"bla bla bla 2\"}";
        
        this.mockMvc.perform(post("/api/auditories").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors.auditoryNumber", is("Title of auditory can`t be blank!")));
    }

}
