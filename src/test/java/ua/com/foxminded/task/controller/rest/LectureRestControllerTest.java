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

import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.domain.repository.dto.LectureDtoModelRepository;
import ua.com.foxminded.task.service.LectureService;

@ExtendWith(SpringExtension.class)
public class LectureRestControllerTest {

    private MockMvc mockMvc;
    private LectureController lectureController;

    @MockBean
    private LectureService lectureService;
    
    @MockBean
    private Logger logger;
    

    private static LectureDto LECTURE_DTO1 = LectureDtoModelRepository.getModel1();
    private static LectureDto LECTURE_DTO2 = LectureDtoModelRepository.getModel2();

    @BeforeEach
    public void init() {
        lectureController = new LectureController(logger, lectureService);
        mockMvc = MockMvcBuilders.standaloneSetup(lectureController).build();
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
                .andExpect(jsonPath("$[0].startTime.[0]", is(7)))
                .andExpect(jsonPath("$[0].startTime.[1]", is(45)))
                .andExpect(jsonPath("$[0].endTime.[0]", is(9)))
                .andExpect(jsonPath("$[0].endTime.[1]", is(20)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].number", is("2")))
                .andExpect(jsonPath("$[1].startTime.[0]", is(9)))
                .andExpect(jsonPath("$[1].startTime.[1]", is(30)))
                .andExpect(jsonPath("$[1].endTime.[0]", is(11)))
                .andExpect(jsonPath("$[1].endTime.[1]", is(05)));
    }

    @Test
    void whenPerformEntitiesAndIdRequest_thenExpectEntityById() throws Exception {
        LECTURE_DTO2.setId(2);

        when(lectureService.findByIdDto(2)).thenReturn(LECTURE_DTO2);

        this.mockMvc.perform(get("/api/lecturies/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(4)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.number", is("2")))
                .andExpect(jsonPath("$.startTime.[0]", is(9)))
                .andExpect(jsonPath("$.startTime.[1]", is(30)))
                .andExpect(jsonPath("$.endTime.[0]", is(11)))
                .andExpect(jsonPath("$.endTime.[1]", is(05)));
    }

    @Test
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        LECTURE_DTO1.setId(2);

        when(lectureService.update(LECTURE_DTO1)).thenReturn(LECTURE_DTO1);

        LectureDto actuallyEntityDto = lectureController.saveEntity(LECTURE_DTO1);

        verify(lectureService, times(1)).update(LECTURE_DTO1);
        assertThat(LECTURE_DTO1).isEqualTo(actuallyEntityDto);
    }

    @Test
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        LECTURE_DTO1.setId(0);

        when(lectureService.create(LECTURE_DTO1)).thenReturn(LECTURE_DTO1);

        LectureDto actuallyEntityDto = lectureController.saveEntity(LECTURE_DTO1);
        
        verify(lectureService, times(1)).create(LECTURE_DTO1);
        assertThat(LECTURE_DTO1).isEqualTo(actuallyEntityDto);
    }


}
