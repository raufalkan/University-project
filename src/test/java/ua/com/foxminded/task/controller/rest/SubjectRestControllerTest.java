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

import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.domain.repository.dto.SubjectDtoModelRepository;
import ua.com.foxminded.task.service.SubjectService;

@ExtendWith(SpringExtension.class)
public class SubjectRestControllerTest {

    private MockMvc mockMvc;
    private SubjectController subjectController;

    @MockBean
    private SubjectService subjectService;
    
    @MockBean
    private Logger logger;
    

    private static SubjectDto SUBJECT_DTO1 = SubjectDtoModelRepository.getModel1();
    private static SubjectDto SUBJECT_DTO2 = SubjectDtoModelRepository.getModel2();

    @BeforeEach
    public void init() {
        subjectController = new SubjectController(logger, subjectService);
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
    }

    @Test
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        SUBJECT_DTO1.setId(1);
        SUBJECT_DTO2.setId(2);
        List<SubjectDto> entityDtos = Arrays.asList(SUBJECT_DTO1, SUBJECT_DTO2);

        when(subjectService.findAllDto()).thenReturn(entityDtos);

        this.mockMvc.perform(get("/api/subjects").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("Programming")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("Phisics")));
    }

    @Test
    void whenPerformEntitiesAndIdRequest_thenExpectEntityById() throws Exception {
        SUBJECT_DTO2.setId(2);

        when(subjectService.findByIdDto(2)).thenReturn(SUBJECT_DTO2);

        this.mockMvc.perform(get("/api/subjects/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("Phisics")));
    }

    @Test
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        SUBJECT_DTO1.setId(2);

        when(subjectService.update(SUBJECT_DTO1)).thenReturn(SUBJECT_DTO1);

        SubjectDto actuallyEntityDto = subjectController.saveEntity(SUBJECT_DTO1);

        verify(subjectService, times(1)).update(SUBJECT_DTO1);
        assertThat(SUBJECT_DTO1).isEqualTo(actuallyEntityDto);
    }

    @Test
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        SUBJECT_DTO1.setId(0);

        when(subjectService.create(SUBJECT_DTO1)).thenReturn(SUBJECT_DTO1);

        SubjectDto actuallyEntityDto = subjectController.saveEntity(SUBJECT_DTO1);
        
        verify(subjectService, times(1)).create(SUBJECT_DTO1);
        assertThat(SUBJECT_DTO1).isEqualTo(actuallyEntityDto);
    }


}
