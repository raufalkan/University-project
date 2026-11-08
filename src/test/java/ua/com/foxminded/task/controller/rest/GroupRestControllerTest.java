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

import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.repository.dto.GroupDtoModelRepository;
import ua.com.foxminded.task.service.GroupService;

@ExtendWith(SpringExtension.class)
public class GroupRestControllerTest {

    private MockMvc mockMvc;
    private GroupController groupController;

    @MockBean
    private GroupService groupService;
    
    @MockBean
    private Logger logger;
    

    private static GroupDto GROUP_DTO1 = GroupDtoModelRepository.getModel1();
    private static GroupDto GROUP_DTO2 = GroupDtoModelRepository.getModel2();

    @BeforeEach
    public void init() {
        groupController = new GroupController(logger, groupService);
        mockMvc = MockMvcBuilders.standaloneSetup(groupController).build();
    }

    @Test
    void whenPerformGroupsRequest_thenExpectListOfGroups() throws Exception {
        GROUP_DTO1.setId(1);
        GROUP_DTO2.setId(2);
        List<GroupDto> groupDtos = Arrays.asList(GROUP_DTO1, GROUP_DTO2);

        when(groupService.findAllDto()).thenReturn(groupDtos);

        this.mockMvc.perform(get("/api/groups").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].title", is("group1")))
                .andExpect(jsonPath("$[0].yearEntry", is(2016)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].title", is("group2")))
                .andExpect(jsonPath("$[1].yearEntry", is(2018)));
    }

    @Test
    void whenPerformGroupAndIdRequest_thenExpectGroupById() throws Exception {
        GROUP_DTO2.setId(2);

        when(groupService.findByIdDto(2)).thenReturn(GROUP_DTO2);

        this.mockMvc.perform(get("/api/groups/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("group2")))
                .andExpect(jsonPath("$.yearEntry", is(2018)));
    }

    @Test
    void whenPerformPostGroupsRequest_thenUpdateGroup() throws Exception {
        GroupDto expectedGroupDto = new GroupDto();
        expectedGroupDto.setId(2);
        expectedGroupDto.setTitle("group2");
        expectedGroupDto.setYearEntry(2001);

        when(groupService.update(expectedGroupDto)).thenReturn(expectedGroupDto);

        GroupDto actuallyGroupDto = groupController.saveGroup(expectedGroupDto);

        verify(groupService, times(1)).update(expectedGroupDto);
        assertThat(expectedGroupDto).isEqualTo(actuallyGroupDto);
    }

    @Test
    void whenPerformPostGroupsRequestWithIdZero_thenCreateGroup() throws Exception {
        GroupDto expectedGroupDto = new GroupDto();
        expectedGroupDto.setId(0);
        expectedGroupDto.setTitle("group3");
        expectedGroupDto.setYearEntry(2013);

        when(groupService.create(expectedGroupDto)).thenReturn(expectedGroupDto);

        GroupDto actuallyGroupDto = groupController.saveGroup(expectedGroupDto);
        
        verify(groupService, times(1)).create(expectedGroupDto);
        assertThat(expectedGroupDto).isEqualTo(actuallyGroupDto);
    }


}
