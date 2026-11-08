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

import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.domain.repository.dto.FacultyDtoModelRepository;
import ua.com.foxminded.task.service.FacultyService;

@ExtendWith(SpringExtension.class)
public class FacultyRestControllerTest {

    private MockMvc mockMvc;
    private FacultyController facultyController;

    @MockBean
    private FacultyService facultyService;
    
    @MockBean
    private Logger logger;
    

    private static FacultyDto FACULTY_DTO1 = FacultyDtoModelRepository.getModel1();
    private static FacultyDto FACULTY_DTO2 = FacultyDtoModelRepository.getModel2();

    @BeforeEach
    public void init() {
        facultyController = new FacultyController(logger, facultyService);
        mockMvc = MockMvcBuilders.standaloneSetup(facultyController).build();
    }

    @Test
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        FACULTY_DTO1.setId(1);
        FACULTY_DTO2.setId(2);
        List<FacultyDto> entityDtos = Arrays.asList(FACULTY_DTO1, FACULTY_DTO2);

        when(facultyService.findAllDto()).thenReturn(entityDtos);

        this.mockMvc.perform(get("/api/faculties").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("faculty1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("faculty2")));
    }

    @Test
    void whenPerformEntitiesAndIdRequest_thenExpectEntityById() throws Exception {
        FACULTY_DTO2.setId(2);

        when(facultyService.findByIdDto(2)).thenReturn(FACULTY_DTO2);

        this.mockMvc.perform(get("/api/faculties/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("faculty2")));
    }

    @Test
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        FACULTY_DTO1.setId(2);

        when(facultyService.update(FACULTY_DTO1)).thenReturn(FACULTY_DTO1);

        FacultyDto actuallyEntityDto = facultyController.saveEntity(FACULTY_DTO1);

        verify(facultyService, times(1)).update(FACULTY_DTO1);
        assertThat(FACULTY_DTO1).isEqualTo(actuallyEntityDto);
    }

    @Test
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        FACULTY_DTO1.setId(0);

        when(facultyService.create(FACULTY_DTO1)).thenReturn(FACULTY_DTO1);

        FacultyDto actuallyEntityDto = facultyController.saveEntity(FACULTY_DTO1);
        
        verify(facultyService, times(1)).create(FACULTY_DTO1);
        assertThat(FACULTY_DTO1).isEqualTo(actuallyEntityDto);
    }


}
