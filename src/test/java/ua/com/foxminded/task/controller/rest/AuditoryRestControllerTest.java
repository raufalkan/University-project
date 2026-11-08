package ua.com.foxminded.task.controller.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.task.domain.dto.AuditoryDto;
import ua.com.foxminded.task.domain.repository.dto.AuditoryDtoModelRepository;
import ua.com.foxminded.task.service.AuditoryService;

@ExtendWith(SpringExtension.class)
public class AuditoryRestControllerTest {

    private MockMvc mockMvc;
    private AuditoryController auditoryController;

    @MockBean
    private AuditoryService auditoryService;
    
    @MockBean
    private Logger logger;
    

    private static AuditoryDto AUDITORY_DTO1 = AuditoryDtoModelRepository.getModel1();
    private static AuditoryDto AUDITORY_DTO2 = AuditoryDtoModelRepository.getModel2();

    @BeforeEach
    public void init() {
        auditoryController = new AuditoryController(logger, auditoryService);
        mockMvc = MockMvcBuilders.standaloneSetup(auditoryController).build();
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
    void whenPerformEntitiesAndIdRequest_thenExpectEntityById() throws Exception {
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
        AUDITORY_DTO1.setId(2);

        when(auditoryService.update(AUDITORY_DTO1)).thenReturn(AUDITORY_DTO1);

        AuditoryDto actuallyEntityDto = auditoryController.saveEntity(AUDITORY_DTO1);

        verify(auditoryService, times(1)).update(AUDITORY_DTO1);
        assertThat(AUDITORY_DTO1).isEqualTo(actuallyEntityDto);
    }

    @Test
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        AUDITORY_DTO1.setId(0);

        when(auditoryService.create(AUDITORY_DTO1)).thenReturn(AUDITORY_DTO1);

        AuditoryDto actuallyEntityDto = auditoryController.saveEntity(AUDITORY_DTO1);
        
        verify(auditoryService, times(1)).create(AUDITORY_DTO1);
        assertThat(AUDITORY_DTO1).isEqualTo(actuallyEntityDto);
    }


}
