package ua.com.foxminded.task.controller;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;

import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.repository.dto.GroupDtoModelRepository;

@DBRider
@SpringBootTest
public class GroupControllerSystemTest {

    @Autowired
    private WebApplicationContext context;

    private static final String PATH_HTML_GROUP = "group/group";
    private static final String PATH_HTML_GROUPS = "group/groups";
    private static final String PATH_HTML_GROUP_EDIT = "group/group_edit";
    private static final String ATTRIBUTE_HTML_GROUP = "groupDto";
    private static final String ATTRIBUTE_HTML_GROUPS = "groups";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String EXPECTED_ERROR_MESSAGE = "You enter incorrect data!";
    private static GroupDto GROUP_DTO1 = GroupDtoModelRepository.getModel1();
    private static GroupDto GROUP_DTO2 = GroupDtoModelRepository.getModel2();
    private static GroupDto GROUP_DTO3 = GroupDtoModelRepository.getModel3();
    private static GroupDto GROUP_DTO4 = GroupDtoModelRepository.getModel4();

    private MockMvc mockMvc;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DataSet(value = "group/groups.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenRetriveAllGroups_thenExpectListOfGroups() throws Exception {
        String expectedTitle = "Groups";
        GROUP_DTO1.setId(1);
        GROUP_DTO2.setId(2);
        GROUP_DTO3.setId(3);
        GROUP_DTO4.setId(4);
        List<GroupDto> groups = Arrays.asList(GROUP_DTO1, GROUP_DTO2, GROUP_DTO3, GROUP_DTO4);
        this.mockMvc.perform(get("/groups").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUPS, equalTo(groups)))
                .andExpect(view().name(PATH_HTML_GROUPS))
                .andExpect(content().contentType("text/html; charset=UTF-8"))
                .andExpect(content().string(allOf(containsString("<title>" + expectedTitle + "</title>"))))
                .andDo(print());
    }

    @Test
    @DataSet(value = "group/groups.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenRetriveTheGroup_thenExpectGroupById() throws Exception {
        String expectedTitle = "Group";
        GroupDto groupDto = new GroupDto();
        int id = 2;
        groupDto.setId(id);
        groupDto.setTitle("group2");
        groupDto.setYearEntry(2018);
        String httpRequest = "/group?id=" + id;
        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUP, equalTo(groupDto)))
                .andExpect(view().name(PATH_HTML_GROUP))
                .andDo(print());
    }

    @Test
    @DataSet(value = "group/groups.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenRetriveEditExistsGroup_thenExpectFormWithGroupField() throws Exception {
        String expectedTitle = "Group edit";
        GroupDto groupDto = new GroupDto();
        int id = 2;
        groupDto.setId(id);
        groupDto.setTitle("group2");
        groupDto.setYearEntry(2018);
        String httpRequest = "/group_edit?id=" + id;
        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUP, equalTo(groupDto)))
                .andExpect(view().name(PATH_HTML_GROUP_EDIT))
                .andDo(print());
    }

    @Test
    @DataSet(value = "group/groups.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenSubmitEditFormGroupWithId_thenUpdateGroup() throws Exception {
        String expectedTitle = "Group edit";
        String expectedSuccessMessage = "Record group was updated!";
        GroupDto groupDto = GroupDtoModelRepository.getModelWithId();
        String httpRequest = "/group_edit";
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("groupDto", groupDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, equalTo(expectedSuccessMessage)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUP, equalTo(groupDto)))
                .andExpect(view().name(PATH_HTML_GROUP))
                .andDo(print());
    }

    @Test
    @DataSet(cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenSubmitEditFormGroupWithoutId_thenCreateGroup() throws Exception {
        String expectedTitle = "Group edit";
        String expectedSuccessMessage = "Record group was created!";
        GroupDto groupDto = new GroupDto();
        groupDto.setTitle("group27");
        groupDto.setYearEntry(2015);
        String httpRequest = "/group_edit";
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("groupDto", groupDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, equalTo(expectedSuccessMessage)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUP, hasProperty("title", is("group27"))))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUP, hasProperty("yearEntry", is(2015))))
                .andExpect(view().name(PATH_HTML_GROUP))
                .andDo(print());
    }

    @Test
    @DataSet(value = "group/groups.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenUpdateGroupWithNotCorrectValues_thenExpectError() throws Exception {
        GroupDto groupDto = GROUP_DTO4;
        groupDto.setId(1);
        groupDto.setTitle(GROUP_DTO2.getTitle());
        groupDto.setYearEntry(99999);
        String httpRequest = "/group_edit";
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("groupDto", groupDto))
                .andExpect(status().isOk())
                .andExpect(view().name(PATH_HTML_GROUP_EDIT))
                .andExpect(content().string(allOf(containsString("<div>" + EXPECTED_ERROR_MESSAGE + "</div>"))))
                .andExpect(model().attributeHasFieldErrorCode("groupDto", "title", "PropertyValueUnique"))
                .andExpect(model().attributeHasFieldErrorCode("groupDto", "yearEntry", "Max"))
                .andDo(print());
    }

    @Test
    @DataSet(value = "group/groups.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenUpdateGroupWithNotCorrectValues_thenExpectError2() throws Exception {
        GroupDto groupDto = GROUP_DTO4;
        groupDto.setId(1);
        groupDto.setTitle("qwertyuiopasdfghjklzxcvbnm");
        groupDto.setYearEntry(999);
        String httpRequest = "/group_edit";
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("groupDto", groupDto))
                .andExpect(status().isOk())
                .andExpect(view().name(PATH_HTML_GROUP_EDIT))
                .andExpect(content().string(allOf(containsString("<div>" + EXPECTED_ERROR_MESSAGE + "</div>"))))
                .andExpect(model().attributeHasFieldErrorCode("groupDto", "yearEntry", "Min"))
                .andExpect(model().attributeHasFieldErrorCode("groupDto", "title", "Size"))
                .andDo(print());
    }

    @Test
    @DataSet(value = "group/groups.yml", 
             cleanBefore = true, 
             skipCleaningFor = "flyway_schema_history")
    void whenUpdateGroupWithNotCorrectTitle_thenExpectError() throws Exception {
        GroupDto groupDto = GROUP_DTO4;
        groupDto.setId(1);
        groupDto.setTitle("");
        String httpRequest = "/group_edit";
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("groupDto", groupDto))
                .andExpect(status().isOk())
                .andExpect(view().name(PATH_HTML_GROUP_EDIT))
                .andExpect(content().string(allOf(containsString("<div>" + EXPECTED_ERROR_MESSAGE + "</div>"))))
                .andExpect(model().attributeHasFieldErrorCode("groupDto", "title", "NotBlank"))
                .andDo(print());
    }
}
