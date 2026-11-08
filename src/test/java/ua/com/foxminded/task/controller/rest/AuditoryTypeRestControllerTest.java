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

import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.domain.repository.dto.AuditoryTypeDtoModelRepository;
import ua.com.foxminded.task.service.AuditoryTypeService;

@ExtendWith(SpringExtension.class)
public class AuditoryTypeRestControllerTest {

    private MockMvc mockMvc;
    private AuditoryTypeController auditoryTypeController;

    @MockBean
    private AuditoryTypeService auditoryTypeService;
    
    @MockBean
    private Logger logger;
    

    private static AuditoryTypeDto AUDITORYTYPE_DTO1 = AuditoryTypeDtoModelRepository.getModel1();
    private static AuditoryTypeDto AUDITORYTYPE_DTO2 = AuditoryTypeDtoModelRepository.getModel2();

    @BeforeEach
    public void init() {
        auditoryTypeController = new AuditoryTypeController(logger, auditoryTypeService);
        mockMvc = MockMvcBuilders.standaloneSetup(auditoryTypeController).build();
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
    void whenPerformEntitiesAndIdRequest_thenExpectEntityById() throws Exception {
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
        AUDITORYTYPE_DTO1.setId(2);

        when(auditoryTypeService.update(AUDITORYTYPE_DTO1)).thenReturn(AUDITORYTYPE_DTO1);

        AuditoryTypeDto actuallyEntityDto = auditoryTypeController.saveEntity(AUDITORYTYPE_DTO1);

        verify(auditoryTypeService, times(1)).update(AUDITORYTYPE_DTO1);
        assertThat(AUDITORYTYPE_DTO1).isEqualTo(actuallyEntityDto);
    }

    @Test
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        AUDITORYTYPE_DTO1.setId(0);

        when(auditoryTypeService.create(AUDITORYTYPE_DTO1)).thenReturn(AUDITORYTYPE_DTO1);

        AuditoryTypeDto actuallyEntityDto = auditoryTypeController.saveEntity(AUDITORYTYPE_DTO1);
        
        verify(auditoryTypeService, times(1)).create(AUDITORYTYPE_DTO1);
        assertThat(AUDITORYTYPE_DTO1).isEqualTo(actuallyEntityDto);
    }


}
