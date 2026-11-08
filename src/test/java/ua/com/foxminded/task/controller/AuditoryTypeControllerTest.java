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
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.domain.repository.dto.AuditoryTypeDtoModelRepository;
import ua.com.foxminded.task.service.AuditoryTypeService;

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
public class AuditoryTypeControllerTest {

    private MockMvc mockMvc;
    private AuditoryTypeController auditoryTypeController;

    @MockBean
    private AuditoryTypeService auditoryTypeService;
    
    @MockBean
    private Logger logger;
    
    @MockBean
    private BindingResult bindingResult;

    private static final String PATH_HTML_AUDITORYTYPE = "auditorytype/auditorytype";
    private static final String PATH_HTML_AUDITORYTYPES = "auditorytype/auditorytypes";
    private static final String PATH_HTML_AUDITORYTYPE_EDIT = "auditorytype/auditorytype_edit";
    private static final String ATTRIBUTE_HTML_AUDITORYTYPE = "auditoryTypeDto";
    private static final String ATTRIBUTE_HTML_AUDITORYTYPES = "auditorytypes";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    
    @BeforeEach
    public void init() {
        auditoryTypeController = new AuditoryTypeController(logger, auditoryTypeService);
        mockMvc = MockMvcBuilders.standaloneSetup(auditoryTypeController).build();
    }

    @Test
    void whenRetrieveAllEntity_thenExpectListOfEntities() throws Exception {
        List<AuditoryTypeDto> auditoryTypeDtos = AuditoryTypeDtoModelRepository.getModels();
        String expectedTitle = "Auditory types";
        String httpRequest = "/auditorytypes";

        when(auditoryTypeService.findAllDto()).thenReturn(auditoryTypeDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_AUDITORYTYPES, equalTo(auditoryTypeDtos)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORYTYPES))
                .andDo(print());
    }
    
    @Test
    void whenRetrieveTheEntity_thenExpectEntityById() throws Exception {
        AuditoryTypeDto auditoryTypeDto = AuditoryTypeDtoModelRepository.getModel1();
        int id = 1;
        String httpRequest = "/auditorytype?id=" + id;
        String expectedTitle = "Auditory type";

        when(auditoryTypeService.findByIdDto(id)).thenReturn(auditoryTypeDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_AUDITORYTYPE, equalTo(auditoryTypeDto)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORYTYPE))
                .andDo(print());
    }

    @Test
    void whenInvokeByBlankId_thenErrorMessage() throws Exception {
        String expectedErrorMessage = "You id is blank";
        String httpRequest = "/auditorytype?id=";

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORYTYPE))
                .andDo(print());
    }
    
    @Test
    void whenInvokeNoFoundEntities_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Auditory type by id#" + id + " not found!";
        String httpRequest = "/auditorytype?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(auditoryTypeService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORYTYPE))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEntitiesWithIncorrectNumberFormatId_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Auditory type id# must be numeric!";
        String httpRequest = "/auditorytype?id=" + id;
        
        doThrow(NumberFormatException.class).when(auditoryTypeService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORYTYPE))
                .andDo(print());
    }
    
    @Test
    void whenRetrieveEditExistsEntity_thenExpectFormWithEntityField() throws Exception {
        AuditoryTypeDto auditoryTypeDto = AuditoryTypeDtoModelRepository.getModel1();
        int id = 1;
        String httpRequest = "/auditorytype_edit?id=" + id;
        String expectedTitle = "Auditory type edit";

        when(auditoryTypeService.findByIdDto(id)).thenReturn(auditoryTypeDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_AUDITORYTYPE, equalTo(auditoryTypeDto)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORYTYPE_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEditEntityWithNoEntityNumber_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Problem with finding auditory type";
        int id = 1;
        String httpRequest = "/auditorytype_edit?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(auditoryTypeService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_AUDITORYTYPE_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenSubmitEditFormEntityWithId_thenUpdateEntity() throws Exception {
        AuditoryTypeDto auditoryTypeDto = AuditoryTypeDtoModelRepository.getModel1();
        auditoryTypeDto.setId(1);
        String expectedTitle = "Auditory type edit";
        String expectedSuccessMessage = "Record auditory type was updated!";
        Model model = new ExtendedModelMap();

        when(auditoryTypeService.update(auditoryTypeDto)).thenReturn(auditoryTypeDto);
        when(bindingResult.hasErrors()).thenReturn(false);

        String actuallyView = auditoryTypeController.editPostEntity(auditoryTypeDto, bindingResult, model);
        
        assertThat(PATH_HTML_AUDITORYTYPE).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(auditoryTypeDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_AUDITORYTYPE));
    }

    @Test
    void whenSubmitEditFormEntityWithoutId_thenCreateEntity() throws Exception {
        AuditoryTypeDto auditoryTypeDto = AuditoryTypeDtoModelRepository.getModel6();
        String expectedTitle = "Auditory type edit";
        String expectedSuccessMessage = "Record auditory type was created!";
        Model model = new ExtendedModelMap();

        when(auditoryTypeService.create(auditoryTypeDto)).thenReturn(auditoryTypeDto);

        String actuallyView = auditoryTypeController.editPostEntity(auditoryTypeDto, bindingResult, model);

        assertThat(PATH_HTML_AUDITORYTYPE).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(auditoryTypeDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_AUDITORYTYPE));
    }
    
    @Test
    void whenInvokeCreateExistsEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record auditory type was not created! The record already exists!";
        AuditoryTypeDto auditoryTypeDto = AuditoryTypeDtoModelRepository.getModel1();
        Model model = new ExtendedModelMap();

        doThrow(EntityAlreadyExistsException.class).when(auditoryTypeService).create(auditoryTypeDto);
      
        String actuallyView = auditoryTypeController.editPostEntity(auditoryTypeDto, bindingResult, model);

        assertThat(PATH_HTML_AUDITORYTYPE_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(auditoryTypeDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_AUDITORYTYPE));
    }
  
    @Test
    void whenInvokeEditNotFoundEntity_thenExpectErrorMessage() throws Exception {
        AuditoryTypeDto auditoryTypeDto = AuditoryTypeDtoModelRepository.getModel1();
        auditoryTypeDto.setId(1);
        String expectedErrorMessage = "Auditory type " + auditoryTypeDto + " not found!";
        Model model = new ExtendedModelMap();

        doThrow(EntityNotFoundException.class).when(auditoryTypeService).update(auditoryTypeDto);
      
        String actuallyView = auditoryTypeController.editPostEntity(auditoryTypeDto, bindingResult, model);

        assertThat(PATH_HTML_AUDITORYTYPE_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(auditoryTypeDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_AUDITORYTYPE));
    }
  
    @Test
    void whenInvokeEditNotValidEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record auditory type was not updated/created! The data is not valid!";
        AuditoryTypeDto auditoryTypeDto = AuditoryTypeDtoModelRepository.getModel1();
        auditoryTypeDto.setId(1);
        Model model = new ExtendedModelMap();

        doThrow(EntityNotValidException.class).when(auditoryTypeService).update(auditoryTypeDto);
      
        String actuallyView = auditoryTypeController.editPostEntity(auditoryTypeDto, bindingResult, model);

        assertThat(PATH_HTML_AUDITORYTYPE_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(auditoryTypeDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_AUDITORYTYPE));
    }
    
}
