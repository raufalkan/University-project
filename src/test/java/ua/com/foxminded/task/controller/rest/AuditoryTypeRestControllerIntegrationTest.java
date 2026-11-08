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
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.domain.repository.dto.AuditoryTypeDtoModelRepository;
import ua.com.foxminded.task.service.AuditoryTypeService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

@WebMvcTest(AuditoryTypeController.class)
@Import(TestMvcConfig.class)
public class AuditoryTypeRestControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;
    @MockBean
    private AuditoryTypeService auditoryTypeService;
    @MockBean
    @Qualifier("uniqueValidationCommandMap")
    private Map<String, Command> uniqueValidationCommandMap;
    
    private static AuditoryTypeDto AUDITORYTYPE_DTO1 = AuditoryTypeDtoModelRepository.getModel1();
    private static AuditoryTypeDto AUDITORYTYPE_DTO2 = AuditoryTypeDtoModelRepository.getModel2();

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        AUDITORYTYPE_DTO1.setId(1);
        AUDITORYTYPE_DTO2.setId(2);
        List<AuditoryTypeDto> entityDtos = Arrays.asList(AUDITORYTYPE_DTO1, AUDITORYTYPE_DTO2);
       
        when(auditoryTypeService.findAllDto()).thenReturn(entityDtos);

        this.mockMvc.perform(get("/api/auditory-types").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].type", is("Lecture")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].type", is("Practic")));
    }

    @Test
    void whenPerformEntityAndIdRequest_thenExpectEntityById() throws Exception {
        AUDITORYTYPE_DTO2.setId(2);

        when(auditoryTypeService.findByIdDto(2)).thenReturn(AUDITORYTYPE_DTO2);

        this.mockMvc.perform(get("/api/auditory-types/2").accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(jsonPath("$", aMapWithSize(2)))
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.type", is("Practic")));
    }

    @Test
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        String entity = "{\"id\":2,"
                      + "\"type\":\"Practic\"}";
        AuditoryTypeDto entityDto = AUDITORYTYPE_DTO2;
        entityDto.setId(2);

        when(auditoryTypeService.update(any(AuditoryTypeDto.class))).thenReturn(entityDto);

        this.mockMvc.perform(post("/api/auditory-types").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.type", is("Practic")));
    }

    @Test
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        String entity = "{\"id\":0,"
                      + "\"type\":\"Practic\"}";
        AuditoryTypeDto entityDto = AUDITORYTYPE_DTO2;
        entityDto.setId(2);
        
        when(auditoryTypeService.create(any(AuditoryTypeDto.class))).thenReturn(entityDto);

        this.mockMvc.perform(post("/api/auditory-types").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.type", is("Practic")));
    }

    @Test
    void whenUpdateEntityWithNotCorrectValues_thenExpectError() throws Exception {
        String entity = "{\"id\":0,"
                      + "\"type\":\"\"}";
        
        this.mockMvc.perform(post("/api/auditory-types").content(entity).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors.type", is("Type name can`t be blank!")));
    }

}
