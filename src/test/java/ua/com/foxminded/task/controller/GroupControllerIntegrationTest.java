package ua.com.foxminded.task.controller;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import ua.com.foxminded.task.config.TestMvcConfig;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.repository.dto.GroupDtoModelRepository;
import ua.com.foxminded.task.service.GroupService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

@WebMvcTest(GroupController.class)
@Import(TestMvcConfig.class)
public class GroupControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GroupService groupService;
    @MockBean
    @Qualifier("uniqueValidationCommandMap")
    private Map<String, Command> uniqueValidationCommandMap;

    private static final String PATH_HTML_GROUP = "group/group";
    private static final String PATH_HTML_GROUPS = "group/groups";
    private static final String PATH_HTML_GROUP_EDIT = "group/group_edit";
    private static final String ATTRIBUTE_HTML_GROUP = "groupDto";
    private static final String ATTRIBUTE_HTML_GROUPS = "groups";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String EXPECTED_ERROR_MESSAGE = "You enter incorrect data!";
    private static GroupDto GROUP_DTO1 = GroupDtoModelRepository.getModel1();
    private static GroupDto GROUP_DTO2 = GroupDtoModelRepository.getModel2();
    private static GroupDto GROUP_DTO3 = GroupDtoModelRepository.getModel3();
    private static GroupDto GROUP_DTO4 = GroupDtoModelRepository.getModel4();

    @Test
    void whenRetriveAllGroups_thenExpectListOfGroups() throws Exception {
        String expectedTitle = "Groups";
        List<GroupDto> groupDtos = Arrays.asList(GROUP_DTO1, GROUP_DTO2, GROUP_DTO3);
        
        when(groupService.findAllDto()).thenReturn(groupDtos);
        
        this.mockMvc.perform(get("/groups").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUPS, equalTo(groupDtos)))
                .andExpect(view().name(PATH_HTML_GROUPS))
                .andDo(print());
    }

    @Test
    void whenRetriveTheGroup_thenExpectGroupById() throws Exception {
        String expectedTitle = "Group";
        GroupDto groupDto = GroupDtoModelRepository.getModelWithId();
        int id = groupDto.getId();
        String httpRequest = "/group?id=" + id;

        when(groupService.findByIdDto(id)).thenReturn(groupDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUP, equalTo(groupDto)))
                .andExpect(view().name(PATH_HTML_GROUP))
                .andDo(print());
    }

    @Test
    void whenRetriveEditExistsGroup_thenExpectFormWithGroupField() throws Exception {
        GroupDto groupDto = GroupDtoModelRepository.getModelWithId();
        int id = groupDto.getId();
        String expectedTitle = "Group edit";
        String httpRequest = "/group_edit?id=" + id;

        when(groupService.findByIdDto(id)).thenReturn(groupDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUP, equalTo(groupDto)))
                .andExpect(view().name(PATH_HTML_GROUP_EDIT))
                .andDo(print());
    }

    @Test
    void whenSubmitEditFormGroupWithId_thenUpdateGroup() throws Exception {
        GroupDto groupDto = GroupDtoModelRepository.getModelWithId();
        String httpRequest = "/group_edit";
        String expectedTitle = "Group edit";
        String expectedSuccessMessage = "Record group was updated!";

        when(groupService.update(groupDto)).thenReturn(groupDto);

        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("groupDto", groupDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, equalTo(expectedSuccessMessage)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUP, equalTo(groupDto)))
                .andExpect(view().name(PATH_HTML_GROUP))
                .andDo(print());
    }

    @Test
    void whenSubmitEditFormGroupWithoutId_thenCreateGroup() throws Exception {
        GroupDto groupDto = GroupDtoModelRepository.getModel6();
        String httpRequest = "/group_edit";
        String expectedTitle = "Group edit";
        String expectedSuccessMessage = "Record group was created!";

        when(groupService.create(groupDto)).thenReturn(groupDto);

        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("groupDto", groupDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE, equalTo(expectedSuccessMessage)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUP, equalTo(groupDto)))
                .andExpect(view().name(PATH_HTML_GROUP))
                .andDo(print());
    }

    @Test
    void whenUpdateGroupWithNotCorrectValues_thenExpectError() throws Exception {
        GroupDto groupDto = GROUP_DTO4;
        groupDto.setId(1);
        groupDto.setYearEntry(99999);
        String httpRequest = "/group_edit";
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("groupDto", groupDto))
                .andExpect(status().isOk())
                .andExpect(view().name(PATH_HTML_GROUP_EDIT))
                .andExpect(content().string(allOf(containsString("<div>" + EXPECTED_ERROR_MESSAGE + "</div>"))))
                .andExpect(model().attributeHasFieldErrorCode("groupDto", "yearEntry", "Max"))
                .andDo(print());
    }

    @Test
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
    void whenUpdateGroupWithNotCorrectTitle_thenExpectError() throws Exception {
        String expectedTitle = "Group edit";
        GroupDto groupDto = GROUP_DTO4;
        groupDto.setId(1);
        groupDto.setTitle("");
        String httpRequest = "/group_edit";
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("groupDto", groupDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(EXPECTED_ERROR_MESSAGE)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUP, equalTo(groupDto)))
                .andExpect(view().name(PATH_HTML_GROUP_EDIT))
                .andExpect(model().attributeHasFieldErrorCode("groupDto", "title", "NotBlank"))
                .andDo(print());
    }
    
    @Test
    void whenCreateExistsGroup_thenExpectErrorMessage() throws Exception {
        GroupDto groupDto = GroupDtoModelRepository.getModel1();
        String httpRequest = "/group_edit";
        String expectedErrorMessage = "Record group was not created! The record already exists!";
      
        doThrow(EntityAlreadyExistsException.class).when(groupService).create(groupDto);
      
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("groupDto", groupDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(view().name(PATH_HTML_GROUP_EDIT))
                .andDo(print());
    }
  
    @Test
    void whenUpdateNotFountEntity_thenExpectErrorMessage() throws Exception {
        GroupDto groupDto = GroupDtoModelRepository.getModelWithId();
        String httpRequest = "/group_edit";
        String expectedErrorMessage = "Group " + groupDto + " not found!";
      
        doThrow(EntityNotFoundException.class).when(groupService).update(groupDto);
      
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("groupDto", groupDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(view().name(PATH_HTML_GROUP_EDIT))
                .andDo(print());
    }
  
    @Test
    void whenCreateNotValidEntity_thenExpectErrorMessage() throws Exception {
        GroupDto groupDto = GroupDtoModelRepository.getModel1();
        String httpRequest = "/group_edit";
        String expectedErrorMessage = "Record group was not updated/created! The data is not valid!";
      
        doThrow(EntityNotValidException.class).when(groupService).create(groupDto);
      
        this.mockMvc.perform(post(httpRequest).accept(MediaType.TEXT_HTML_VALUE).flashAttr("groupDto", groupDto))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(view().name(PATH_HTML_GROUP_EDIT))
                .andDo(print());
    }
}
