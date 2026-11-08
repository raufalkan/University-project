package ua.com.foxminded.task.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.domain.repository.dto.SubjectDtoModelRepository;
import ua.com.foxminded.task.service.SubjectService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class SubjectControllerTest {

    private MockMvc mockMvc;
    private SubjectController subjectController;

    @MockBean
    private SubjectService subjectService;
    
    @MockBean
    private Logger logger;
    
    @MockBean
    private BindingResult bindingResult;

    private static final String PATH_HTML_SUBJECT = "subject/subject";
    private static final String PATH_HTML_SUBJECTS = "subject/subjects";
    private static final String PATH_HTML_SUBJECT_EDIT = "subject/subject_edit";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String ATTRIBUTE_HTML_SUBJECT = "subjectDto";
    private static final String ATTRIBUTE_HTML_SUBJECTS = "subjects";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";

    @BeforeEach
    public void init() {
        subjectController = new SubjectController(logger, subjectService);
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
    }
    
    @Test
    void whenRetrieveAllEntity_thenExpectListOfEntities() throws Exception {
        List<SubjectDto> subjectDtos = SubjectDtoModelRepository.getModels1();
        String expectedTitle = "Subject";
        String httpRequest = "/subjects";

        when(subjectService.findAllDto()).thenReturn(subjectDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_SUBJECTS, equalTo(subjectDtos)))
                .andExpect(forwardedUrl(PATH_HTML_SUBJECTS))
                .andDo(print());
    }
    
    @Test
    void whenRetriveTheEntity_thenExpectEntityById() throws Exception {
        SubjectDto subjectDto = SubjectDtoModelRepository.getModel1();
        int id = 1;
        String httpRequest = "/subject?id=" + id;
        String expectedTitle = "Subject";

        when(subjectService.findByIdDto(id)).thenReturn(subjectDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_SUBJECT, equalTo(subjectDto)))
                .andExpect(forwardedUrl(PATH_HTML_SUBJECT))
                .andDo(print());
    }

    @Test
    void whenInvokeByBlankId_thenErrorMessage() throws Exception {
        String expectedErrorMessage = "You id is blank";
        String httpRequest = "/subject?id=";

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_SUBJECT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeNoFoundEntities_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Subject by id#" + id + " not found!";
        String httpRequest = "/subject?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(subjectService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_SUBJECT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEntitiesWithIncorrectNumberFormatId_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Subject id# must be numeric!";
        String httpRequest = "/subject?id=" + id;
        
        doThrow(NumberFormatException.class).when(subjectService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_SUBJECT))
                .andDo(print());
    }
    
    @Test
    void whenRetriveEditExistsEntity_thenExpectFormWithEntityField() throws Exception {
        SubjectDto subjectDto = SubjectDtoModelRepository.getModel1();
        int id = 1;
        String httpRequest = "/subject_edit?id=" + id;
        String expectedTitle = "Subject edit";

        when(subjectService.findByIdDto(id)).thenReturn(subjectDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_SUBJECT, equalTo(subjectDto)))
                .andExpect(forwardedUrl(PATH_HTML_SUBJECT_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEditEntityWithNoEntityNumber_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Problem with finding subject";
        int id = 1;
        String httpRequest = "/subject_edit?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(subjectService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_SUBJECT_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenSubmitEditFormEntityWithId_thenUpdateEntity() throws Exception {
        SubjectDto subjectDto = SubjectDtoModelRepository.getModel1();
        subjectDto.setId(1);
        String expectedTitle = "Subject edit";
        String expectedSuccessMessage = "Record subject was updated!";
        Model model = new ExtendedModelMap();

        when(subjectService.update(subjectDto)).thenReturn(subjectDto);
        when(bindingResult.hasErrors()).thenReturn(false);

        String actuallyView = subjectController.editPostEntity(subjectDto, bindingResult, model);
        
        assertThat(PATH_HTML_SUBJECT).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(subjectDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUBJECT));
    }

    @Test
    void whenSubmitEditFormEntityWithoutId_thenCreateEntity() throws Exception {
        SubjectDto subjectDto = SubjectDtoModelRepository.getModel4();
        String expectedTitle = "Subject edit";
        String expectedSuccessMessage = "Record subject was created!";
        Model model = new ExtendedModelMap();

        when(subjectService.create(subjectDto)).thenReturn(subjectDto);

        String actuallyView = subjectController.editPostEntity(subjectDto, bindingResult, model);

        assertThat(PATH_HTML_SUBJECT).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(subjectDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUBJECT));
    }
    
    @Test
    void whenInvokeCreateExistsEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record subject was not created! The record already exists!";
        SubjectDto subjectDto = SubjectDtoModelRepository.getModel1();
        Model model = new ExtendedModelMap();

        doThrow(EntityAlreadyExistsException.class).when(subjectService).create(subjectDto);
      
        String actuallyView = subjectController.editPostEntity(subjectDto, bindingResult, model);

        assertThat(PATH_HTML_SUBJECT_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(subjectDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUBJECT));
    }
  
    @Test
    void whenInvokeEditNotFoundEntity_thenExpectErrorMessage() throws Exception {
        SubjectDto subjectDto = SubjectDtoModelRepository.getModel1();
        subjectDto.setId(1);
        String expectedErrorMessage = "Subject " + subjectDto + " not found!";
        Model model = new ExtendedModelMap();

        doThrow(EntityNotFoundException.class).when(subjectService).update(subjectDto);
      
        String actuallyView = subjectController.editPostEntity(subjectDto, bindingResult, model);

        assertThat(PATH_HTML_SUBJECT_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(subjectDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUBJECT));
    }
  
    @Test
    void whenInvokeEditNotValidEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record subject was not updated/created! The data is not valid!";
        SubjectDto subjectDto = SubjectDtoModelRepository.getModel1();
        subjectDto.setId(1);
        Model model = new ExtendedModelMap();

        doThrow(EntityNotValidException.class).when(subjectService).update(subjectDto);
      
        String actuallyView = subjectController.editPostEntity(subjectDto, bindingResult, model);

        assertThat(PATH_HTML_SUBJECT_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(subjectDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUBJECT));
    }
    
}
