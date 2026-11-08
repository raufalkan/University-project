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

import ua.com.foxminded.task.domain.dto.TeacherDto;
import ua.com.foxminded.task.domain.repository.dto.TeacherDtoModelRepository;
import ua.com.foxminded.task.service.TeacherService;

@ExtendWith(SpringExtension.class)
public class TeacherRestControllerTest {

    private MockMvc mockMvc;
    private TeacherController teacherController;

    @MockBean
    private TeacherService teacherService;
    
    @MockBean
    private Logger logger;
    

    private static TeacherDto TEACHER_DTO1 = TeacherDtoModelRepository.getModel1();
    private static TeacherDto TEACHER_DTO2 = TeacherDtoModelRepository.getModel2();

    @BeforeEach
    public void init() {
        teacherController = new TeacherController(logger, teacherService);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        TEACHER_DTO1.setId(1);
        TEACHER_DTO2.setId(2);
        List<TeacherDto> entityDtos = Arrays.asList(TEACHER_DTO1, TEACHER_DTO2);

        when(teacherService.findAllDto()).thenReturn(entityDtos);

        this.mockMvc.perform(get("/api/teachers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].firstName", is("firstNameTe1")))
                .andExpect(jsonPath("$[0].middleName", is("middleNameTe1")))
                .andExpect(jsonPath("$[0].lastName", is("lastNameTe1")))
                .andExpect(jsonPath("$[0].birthday.[0]", is(1980)))
                .andExpect(jsonPath("$[0].birthday.[1]", is(6)))
                .andExpect(jsonPath("$[0].birthday.[2]", is(25)))
                .andExpect(jsonPath("$[0].idFees", is(111111166)))
                .andExpect(jsonPath("$[0].departmentTitle", is("department1")))
                .andExpect(jsonPath("$[0].departmentId", is(1)))
                .andExpect(jsonPath("$[0].subjects.[0].title", is("Phisics")))
                .andExpect(jsonPath("$[0].subjects.[1].title", is("Mathmatics")))
                .andExpect(jsonPath("$[0].subjects.[2].title", is("Biologic")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].firstName", is("firstNameTe2")))
                .andExpect(jsonPath("$[1].middleName", is("middleNameTe2")))
                .andExpect(jsonPath("$[1].lastName", is("lastNameTe2")))
                .andExpect(jsonPath("$[1].birthday.[0]", is(1966)))
                .andExpect(jsonPath("$[1].birthday.[1]", is(6)))
                .andExpect(jsonPath("$[1].birthday.[2]", is(25)))
                .andExpect(jsonPath("$[1].idFees", is(211111111)))
                .andExpect(jsonPath("$[0].departmentTitle", is("department1")))
                .andExpect(jsonPath("$[0].departmentId", is(1)))
                .andExpect(jsonPath("$[0].subjects.[0].title", is("Phisics")))
                .andExpect(jsonPath("$[0].subjects.[1].title", is("Mathmatics")))
                .andExpect(jsonPath("$[0].subjects.[2].title", is("Biologic")));
    }

    @Test
    void whenPerformEntitiesAndIdRequest_thenExpectEntityById() throws Exception {
        TEACHER_DTO2.setId(2);

        when(teacherService.findByIdDto(2)).thenReturn(TEACHER_DTO2);

        this.mockMvc.perform(get("/api/teachers/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(9)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstName", is("firstNameTe2")))
                .andExpect(jsonPath("$.middleName", is("middleNameTe2")))
                .andExpect(jsonPath("$.lastName", is("lastNameTe2")))
                .andExpect(jsonPath("$.birthday.[0]", is(1966)))
                .andExpect(jsonPath("$.birthday.[1]", is(6)))
                .andExpect(jsonPath("$.birthday.[2]", is(25)))
                .andExpect(jsonPath("$.idFees", is(211111111)))
                .andExpect(jsonPath("$.departmentTitle", is("department2")))
                .andExpect(jsonPath("$.departmentId", is(0)))
                .andExpect(jsonPath("$.subjects.[0].title", is("Phisics")))
                .andExpect(jsonPath("$.subjects.[1].title", is("Mathmatics")))
                .andExpect(jsonPath("$.subjects.[2].title", is("Biologic")));
    }

    @Test
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        TEACHER_DTO1.setId(2);

        when(teacherService.update(TEACHER_DTO1)).thenReturn(TEACHER_DTO1);

        TeacherDto actuallyEntityDto = teacherController.saveEntity(TEACHER_DTO1);

        verify(teacherService, times(1)).update(TEACHER_DTO1);
        assertThat(TEACHER_DTO1).isEqualTo(actuallyEntityDto);
    }

    @Test
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        TEACHER_DTO1.setId(0);

        when(teacherService.create(TEACHER_DTO1)).thenReturn(TEACHER_DTO1);

        TeacherDto actuallyEntityDto = teacherController.saveEntity(TEACHER_DTO1);
        
        verify(teacherService, times(1)).create(TEACHER_DTO1);
        assertThat(TEACHER_DTO1).isEqualTo(actuallyEntityDto);
    }


}
