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

import ua.com.foxminded.task.domain.dto.DepartmentDto;
import ua.com.foxminded.task.domain.repository.dto.DepartmentDtoModelRepository;
import ua.com.foxminded.task.service.DepartmentService;

@ExtendWith(SpringExtension.class)
public class DepartmentRestControllerTest {

    private MockMvc mockMvc;
    private DepartmentController departmentController;

    @MockBean
    private DepartmentService departmentService;
    
    @MockBean
    private Logger logger;
    

    private static DepartmentDto DEPARTMENT_DTO1 = DepartmentDtoModelRepository.getModel1();
    private static DepartmentDto DEPARTMENT_DTO2 = DepartmentDtoModelRepository.getModel2();

    @BeforeEach
    public void init() {
        departmentController = new DepartmentController(logger, departmentService);
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
    }

    @Test
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        DEPARTMENT_DTO1.setId(1);
        DEPARTMENT_DTO2.setId(2);
        List<DepartmentDto> entityDtos = Arrays.asList(DEPARTMENT_DTO1, DEPARTMENT_DTO2);

        when(departmentService.findAllDto()).thenReturn(entityDtos);

        this.mockMvc.perform(get("/api/departments").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("department1")))
                .andExpect(jsonPath("$[0].description", is("bla bla bla 1")))
                .andExpect(jsonPath("$[0].facultyTitle", is("faculty1")))
                .andExpect(jsonPath("$[0].facultyId", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("department2")))
                .andExpect(jsonPath("$[1].description", is("bla bla bla 2")))
                .andExpect(jsonPath("$[1].facultyTitle", is("faculty1")))
                .andExpect(jsonPath("$[1].facultyId", is(2)));
    }

    @Test
    void whenPerformEntitiesAndIdRequest_thenExpectEntityById() throws Exception {
        DEPARTMENT_DTO2.setId(2);

        when(departmentService.findByIdDto(2)).thenReturn(DEPARTMENT_DTO2);

        this.mockMvc.perform(get("/api/departments/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(5)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("department2")))
                .andExpect(jsonPath("$.description", is("bla bla bla 2")))
                .andExpect(jsonPath("$.facultyTitle", is("faculty1")))
                .andExpect(jsonPath("$.facultyId", is(2)));
    }

    @Test
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        DEPARTMENT_DTO1.setId(2);

        when(departmentService.update(DEPARTMENT_DTO1)).thenReturn(DEPARTMENT_DTO1);

        DepartmentDto actuallyEntityDto = departmentController.saveEntity(DEPARTMENT_DTO1);

        verify(departmentService, times(1)).update(DEPARTMENT_DTO1);
        assertThat(DEPARTMENT_DTO1).isEqualTo(actuallyEntityDto);
    }

    @Test
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        DEPARTMENT_DTO1.setId(0);

        when(departmentService.create(DEPARTMENT_DTO1)).thenReturn(DEPARTMENT_DTO1);

        DepartmentDto actuallyEntityDto = departmentController.saveEntity(DEPARTMENT_DTO1);
        
        verify(departmentService, times(1)).create(DEPARTMENT_DTO1);
        assertThat(DEPARTMENT_DTO1).isEqualTo(actuallyEntityDto);
    }


}
