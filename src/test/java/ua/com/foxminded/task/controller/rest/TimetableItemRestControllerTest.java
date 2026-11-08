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

import ua.com.foxminded.task.domain.dto.TimetableItemDto;
import ua.com.foxminded.task.domain.repository.dto.TimetableItemDtoModelRepository;
import ua.com.foxminded.task.service.TimetableItemService;

@ExtendWith(SpringExtension.class)
public class TimetableItemRestControllerTest {

    private MockMvc mockMvc;
    private TimetableItemController timetableItemController;

    @MockBean
    private TimetableItemService timetableItemService;
    
    @MockBean
    private Logger logger;
    

    private static TimetableItemDto TIMETABLEITEM_DTO1 = TimetableItemDtoModelRepository.getModel1();
    private static TimetableItemDto TIMETABLEITEM_DTO2 = TimetableItemDtoModelRepository.getModel2();

    @BeforeEach
    public void init() {
        timetableItemController = new TimetableItemController(logger, timetableItemService);
        mockMvc = MockMvcBuilders.standaloneSetup(timetableItemController).build();
    }

    @Test
    void whenPerformEntitiesRequest_thenExpectListOfEntities() throws Exception {
        TIMETABLEITEM_DTO1.setId(1);
        TIMETABLEITEM_DTO2.setId(2);
        List<TimetableItemDto> entityDtos = Arrays.asList(TIMETABLEITEM_DTO1, TIMETABLEITEM_DTO2);

        when(timetableItemService.findAllDto()).thenReturn(entityDtos);

        this.mockMvc.perform(get("/api/timetable-items").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].subjectTitle", is("Programming")))
                .andExpect(jsonPath("$[0].subjectId", is(0)))
                .andExpect(jsonPath("$[0].auditoryTitle", is("101a")))
                .andExpect(jsonPath("$[0].auditoryId", is(0)))
                .andExpect(jsonPath("$[0].groups.[0].title", is("group1")))
                .andExpect(jsonPath("$[0].groups.[1].title", is("group2")))
                .andExpect(jsonPath("$[0].lectureTitle", is("1")))
                .andExpect(jsonPath("$[0].lectureId", is(0)))
                .andExpect(jsonPath("$[0].date.[0]", is(2020)))
                .andExpect(jsonPath("$[0].date.[1]", is(6)))
                .andExpect(jsonPath("$[0].date.[2]", is(25)))
                .andExpect(jsonPath("$[0].teacherTitle", is("firstNameTe1")))
                .andExpect(jsonPath("$[0].teacherId", is(0)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].subjectTitle", is("Phisics")))
                .andExpect(jsonPath("$[1].subjectId", is(0)))
                .andExpect(jsonPath("$[1].auditoryTitle", is("102a")))
                .andExpect(jsonPath("$[1].auditoryId", is(0)))
                .andExpect(jsonPath("$[1].groups.[0].title", is("group3")))
                .andExpect(jsonPath("$[1].groups.[1].title", is("group4")))
                .andExpect(jsonPath("$[1].lectureTitle", is("1")))
                .andExpect(jsonPath("$[1].lectureId", is(0)))
                .andExpect(jsonPath("$[1].date.[0]", is(2020)))
                .andExpect(jsonPath("$[1].date.[1]", is(6)))
                .andExpect(jsonPath("$[1].date.[2]", is(25)))
                .andExpect(jsonPath("$[1].teacherTitle", is("firstNameTe2")))
                .andExpect(jsonPath("$[1].teacherId", is(0)));
    }

    @Test
    void whenPerformEntitiesAndIdRequest_thenExpectEntityById() throws Exception {
        TIMETABLEITEM_DTO2.setId(2);

        when(timetableItemService.findByIdDto(2)).thenReturn(TIMETABLEITEM_DTO2);

        this.mockMvc.perform(get("/api/timetable-items/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(11)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.subjectTitle", is("Phisics")))
                .andExpect(jsonPath("$.subjectId", is(0)))
                .andExpect(jsonPath("$.auditoryTitle", is("102a")))
                .andExpect(jsonPath("$.auditoryId", is(0)))
                .andExpect(jsonPath("$.groups.[0].title", is("group3")))
                .andExpect(jsonPath("$.groups.[1].title", is("group4")))
                .andExpect(jsonPath("$.lectureTitle", is("1")))
                .andExpect(jsonPath("$.lectureId", is(0)))
                .andExpect(jsonPath("$.date.[0]", is(2020)))
                .andExpect(jsonPath("$.date.[1]", is(6)))
                .andExpect(jsonPath("$.date.[2]", is(25)))
                .andExpect(jsonPath("$.teacherTitle", is("firstNameTe2")))
                .andExpect(jsonPath("$.teacherId", is(0)));
    }

    @Test
    void whenPerformPostEntitiesRequest_thenUpdateEntity() throws Exception {
        TIMETABLEITEM_DTO1.setId(2);

        when(timetableItemService.update(TIMETABLEITEM_DTO1)).thenReturn(TIMETABLEITEM_DTO1);

        TimetableItemDto actuallyEntityDto = timetableItemController.saveEntity(TIMETABLEITEM_DTO1);

        verify(timetableItemService, times(1)).update(TIMETABLEITEM_DTO1);
        assertThat(TIMETABLEITEM_DTO1).isEqualTo(actuallyEntityDto);
    }

    @Test
    void whenPerformPostEntitiesRequestWithIdZero_thenCreateEntity() throws Exception {
        TIMETABLEITEM_DTO1.setId(0);

        when(timetableItemService.create(TIMETABLEITEM_DTO1)).thenReturn(TIMETABLEITEM_DTO1);

        TimetableItemDto actuallyEntityDto = timetableItemController.saveEntity(TIMETABLEITEM_DTO1);
        
        verify(timetableItemService, times(1)).create(TIMETABLEITEM_DTO1);
        assertThat(TIMETABLEITEM_DTO1).isEqualTo(actuallyEntityDto);
    }


}
