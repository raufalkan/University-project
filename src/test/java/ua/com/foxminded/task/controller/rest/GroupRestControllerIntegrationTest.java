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
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.repository.dto.GroupDtoModelRepository;
import ua.com.foxminded.task.service.GroupService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

@WebMvcTest(GroupController.class)
@Import(TestMvcConfig.class)
public class GroupRestControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;
    @MockBean
    private GroupService groupService;
    @MockBean
    @Qualifier("uniqueValidationCommandMap")
    private Map<String, Command> uniqueValidationCommandMap;
    
    private static GroupDto GROUP_DTO1 = GroupDtoModelRepository.getModel1();
    private static GroupDto GROUP_DTO2 = GroupDtoModelRepository.getModel2();

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
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
        String group = "{\"id\":2," 
                     + "\"title\":\"group2\"," 
                     + "\"yearEntry\":2001}";
        GroupDto groupDto = new GroupDto();
        groupDto.setId(2);
        groupDto.setTitle("group2");
        groupDto.setYearEntry(2001);

        when(groupService.update(any(GroupDto.class))).thenReturn(groupDto);

        this.mockMvc.perform(post("/api/groups").content(group).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.title", is("group2")))
                .andExpect(jsonPath("$.yearEntry", is(2001)));
    }

    @Test
    void whenPerformPostGroupsRequestWithIdZero_thenCreateGroup() throws Exception {
        String group = "{\"id\":0," 
                     + "\"title\":\"group3\"," 
                     + "\"yearEntry\":2013}";
        GroupDto groupDto = new GroupDto();
        groupDto.setId(3);
        groupDto.setTitle("group3");
        groupDto.setYearEntry(2013);

        when(groupService.create(any(GroupDto.class))).thenReturn(groupDto);

        this.mockMvc.perform(post("/api/groups").content(group).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$", aMapWithSize(3)))
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.title", is("group3")))
                .andExpect(jsonPath("$.yearEntry", is(2013)));
    }

    @Test
    void whenUpdateGroupWithNotCorrectValues_thenExpectError() throws Exception {
        String group = "{\"id\":2," 
                     + "\"title\":\"group1\"," 
                     + "\"yearEntry\":99999}";
        this.mockMvc.perform(post("/api/groups").content(group).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors.yearEntry", is("Year of entry is not correct!")));
    }

    @Test
    void whenUpdateGroupWithNotCorrectValues_thenExpectError2() throws Exception {
        String group = "{\"id\":2," 
                     + "\"title\":\"qwertyuiopasdfghjklzxcvbnm\"," 
                     + "\"yearEntry\":999}";
        this.mockMvc.perform(post("/api/groups").content(group).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(2)))
                .andExpect(jsonPath("$.errors.title", is("Maximum length is 20!")))
                .andExpect(jsonPath("$.errors.yearEntry", is("Year of entry is not correct!")));
    }

    @Test
    void whenUpdateGroupWithNotCorrectTitle_thenExpectError() throws Exception {
        String group = "{\"id\":2," + "\"title\":\"\"," + "\"yearEntry\":2018}";
        this.mockMvc.perform(post("/api/groups").content(group).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.errors", aMapWithSize(1)))
                .andExpect(jsonPath("$.errors.title", is("Title can`t be blank!")));
    }
}
