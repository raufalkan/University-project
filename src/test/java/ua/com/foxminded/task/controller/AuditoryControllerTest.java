package ua.com.foxminded.task.controller;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityNotFoundException;

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
import ua.com.foxminded.task.domain.dto.AuditoryDto;
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.domain.repository.dto.AuditoryDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.AuditoryTypeDtoModelRepository;
import ua.com.foxminded.task.service.AuditoryService;
import ua.com.foxminded.task.service.AuditoryTypeService;

@ExtendWith(SpringExtension.class)
public class AuditoryControllerTest {

    private MockMvc mockMvc;
    private AuditoryController auditoryController;

    @MockBean
    private AuditoryTypeService auditoryTypeService;

    @MockBean
    private AuditoryService auditoryService;
    
    @MockBean
    private Logger logger;
    
    @MockBean
    private BindingResult bindingResult;

    private static final String PATH_HTML_AUDITORY = "auditory/auditory";
    private static final String PATH_HTML_AUDITORIES = "auditory/auditories";
    private static final String PATH_HTML_AUDITORY_EDIT = "auditory/auditory_edit";
    private static final String ATTRIBUTE_HTML_AUDITORY = "auditoryDto";
    private static final String ATTRIBUTE_HTML_AUDITORIES = "auditories";
    private static final String ATTRIBUTE_HTML_AUDITORYTYPE = "auditorytypes";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    
    @BeforeEach
    public void init() {
        auditoryController = new AuditoryController(logger, auditoryService, auditoryTypeService);
        mockMvc = MockMvcBuilders.standaloneSetup(auditoryController).build();
    }

    @Test
    void whenRetrieveAllEntity_thenExpectListOfEntities() throws Exception {
        List<AuditoryDto> auditoryDtos = AuditoryDtoModelRepository.getModels();
        String expectedTitle = "Auditories";
        String httpRequest = "/auditories";

        when(auditoryService.findAllDto()).thenReturn(auditoryDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_AUDITORIES, equalTo(auditoryDtos)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORIES))
                .andDo(print());
    }
    
    @Test
    void whenRetrieveTheEntity_thenExpectEntityById() throws Exception {
        AuditoryDto auditoryDto = AuditoryDtoModelRepository.getModel1();
        int id = 1;
        String httpRequest = "/auditory?id=" + id;
        String expectedTitle = "Auditory";

        when(auditoryService.findByIdDto(id)).thenReturn(auditoryDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_AUDITORY, equalTo(auditoryDto)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORY))
                .andDo(print());
    }

    @Test
    void whenInvokeByBlankId_thenErrorMessage() throws Exception {
        String expectedErrorMessage = "You id is blank";
        String httpRequest = "/auditory?id=";

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORY))
                .andDo(print());
    }
    
    @Test
    void whenInvokeNoFoundEntities_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Auditory by id#" + id + " not found!";
        String httpRequest = "/auditory?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(auditoryService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORY))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEntitiesWithIncorrectNumberFormatId_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Auditory id# must be numeric!";
        String httpRequest = "/auditory?id=" + id;
        
        doThrow(NumberFormatException.class).when(auditoryService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORY))
                .andDo(print());
    }
    
    @Test
    void whenRetrieveEditExistsEntity_thenExpectFormWithEntityField() throws Exception {
        AuditoryDto auditoryDto = AuditoryDtoModelRepository.getModel1();
        List<AuditoryTypeDto> auditoryTypeDtos = AuditoryTypeDtoModelRepository.getModels();
        int id = 1;
        String httpRequest = "/auditory_edit?id=" + id;
        String expectedTitle = "Auditory edit";

        when(auditoryService.findByIdDto(id)).thenReturn(auditoryDto);
        when(auditoryTypeService.findAllDto()).thenReturn(auditoryTypeDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_AUDITORY, equalTo(auditoryDto)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_AUDITORYTYPE, equalTo(auditoryTypeDtos)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORY_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEditEntityWithNoEntityNumber_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Problem with finding auditory";
        List<AuditoryTypeDto> auditoryTypeDtos = AuditoryTypeDtoModelRepository.getModels();
        int id = 1;
        String httpRequest = "/auditory_edit?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(auditoryService).findByIdDto(id);
        when(auditoryTypeService.findAllDto()).thenReturn(auditoryTypeDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORY_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenSubmitEditFormEntityWithId_thenUpdateEntity() throws Exception {
        AuditoryDto auditoryDto = AuditoryDtoModelRepository.getModel1();
        auditoryDto.setId(1);
        String expectedTitle = "Auditory edit";
        String expectedSuccessMessage = "Record auditory was updated!";
        Model model = new ExtendedModelMap();

        when(auditoryService.update(auditoryDto)).thenReturn(auditoryDto);
        when(bindingResult.hasErrors()).thenReturn(false);

        String actuallyView = auditoryController.editPostEntity(auditoryDto, bindingResult, model);
        
        assertThat(PATH_HTML_AUDITORY).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(auditoryDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_AUDITORY));
    }

    @Test
    void whenSubmitEditFormEntityWithoutId_thenCreateEntity() throws Exception {
        AuditoryDto auditoryDto = AuditoryDtoModelRepository.getModel6();
        String expectedTitle = "Auditory edit";
        String expectedSuccessMessage = "Record auditory was created";
        Model model = new ExtendedModelMap();

        when(auditoryService.create(auditoryDto)).thenReturn(auditoryDto);

        String actuallyView = auditoryController.editPostEntity(auditoryDto, bindingResult, model);

        assertThat(PATH_HTML_AUDITORY).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(auditoryDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_AUDITORY));
    }
    
    @Test
    void whenInvokeCreateExistsEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record auditory was not created! The record already exists!";
        AuditoryDto auditoryDto = AuditoryDtoModelRepository.getModel1();
        Model model = new ExtendedModelMap();

        doThrow(EntityAlreadyExistsException.class).when(auditoryService).create(auditoryDto);
      
        String actuallyView = auditoryController.editPostEntity(auditoryDto, bindingResult, model);

        assertThat(PATH_HTML_AUDITORY_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(auditoryDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_AUDITORY));
    }
  
    @Test
    void whenInvokeEditNotFoundEntity_thenExpectErrorMessage() throws Exception {
        AuditoryDto auditoryDto = AuditoryDtoModelRepository.getModel1();
        auditoryDto.setId(1);
        String expectedErrorMessage = "Auditory " + auditoryDto + " not found!";
        Model model = new ExtendedModelMap();

        doThrow(EntityNotFoundException.class).when(auditoryService).update(auditoryDto);
      
        String actuallyView = auditoryController.editPostEntity(auditoryDto, bindingResult, model);

        assertThat(PATH_HTML_AUDITORY_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(auditoryDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_AUDITORY));
    }
  
    @Test
    void whenInvokeEditNotValidEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record auditory was not updated/created! The data is not valid!";
        AuditoryDto auditoryDto = AuditoryDtoModelRepository.getModel1();
        auditoryDto.setId(1);
        Model model = new ExtendedModelMap();

        doThrow(EntityNotValidException.class).when(auditoryService).update(auditoryDto);
      
        String actuallyView = auditoryController.editPostEntity(auditoryDto, bindingResult, model);

        assertThat(PATH_HTML_AUDITORY_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(auditoryDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_AUDITORY));
    }
    
}
