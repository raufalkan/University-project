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
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.domain.repository.dto.FacultyDtoModelRepository;
import ua.com.foxminded.task.service.FacultyService;

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
public class FacultyControllerTest {

    private MockMvc mockMvc;

    private FacultyController facultyController;

    @MockBean
    private FacultyService facultyService;
    
    @MockBean
    private Logger logger;
    
    @MockBean
    private BindingResult bindingResult;

    private static final String PATH_HTML_FACULTY = "faculty/faculty";
    private static final String PATH_HTML_FACULTIES = "faculty/faculties";
    private static final String PATH_HTML_FACULTY_EDIT = "faculty/faculty_edit";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String ATTRIBUTE_HTML_FACULTY = "facultyDto";
    private static final String ATTRIBUTE_HTML_FACULTIES = "faculties";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";

    @BeforeEach
    public void init() {
        facultyController = new FacultyController(logger, facultyService);
        mockMvc = MockMvcBuilders.standaloneSetup(facultyController).build();
    }
    
    @Test
    void whenRetriveAllEntity_thenExpectListOfEntities() throws Exception {
        List<FacultyDto> facultyDtos = FacultyDtoModelRepository.getModels();
        String expectedTitle = "Faculties";
        String httpRequest = "/faculties";

        when(facultyService.findAllDto()).thenReturn(facultyDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_FACULTIES, equalTo(facultyDtos)))
                .andExpect(forwardedUrl(PATH_HTML_FACULTIES))
                .andDo(print());
    }
    
    @Test
    void whenRetriveTheEntity_thenExpectEntityById() throws Exception {
        FacultyDto facultyDto = FacultyDtoModelRepository.getModel1();
        int id = 1;
        String httpRequest = "/faculty?id=" + id;
        String expectedTitle = "Faculty";

        when(facultyService.findByIdDto(id)).thenReturn(facultyDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_FACULTY, equalTo(facultyDto)))
                .andExpect(forwardedUrl(PATH_HTML_FACULTY))
                .andDo(print());
    }

    @Test
    void whenInvokeByBlankId_thenErrorMessage() throws Exception {
        String expectedErrorMessage = "You id is blank";
        String httpRequest = "/faculty?id=";

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_FACULTY))
                .andDo(print());
    }
    
    @Test
    void whenInvokeNoFoundEntities_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Faculty by id#" + id + " not found!";
        String httpRequest = "/faculty?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(facultyService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_FACULTY))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEntitiesWithIncorrectNumberFormatId_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Faculty id# must be numeric!";
        String httpRequest = "/faculty?id=" + id;
        
        doThrow(NumberFormatException.class).when(facultyService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_FACULTY))
                .andDo(print());
    }
    
    @Test
    void whenRetriveEditExistsEntity_thenExpectFormWithEntityField() throws Exception {
        FacultyDto facultyDto = FacultyDtoModelRepository.getModel1();
        int id = 1;
        String httpRequest = "/faculty_edit?id=" + id;
        String expectedTitle = "Faculty edit";

        when(facultyService.findByIdDto(id)).thenReturn(facultyDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_FACULTY, equalTo(facultyDto)))
                .andExpect(forwardedUrl(PATH_HTML_FACULTY_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEditEntityWithNoEntityNumber_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Problem with finding faculty";
        int id = 1;
        String httpRequest = "/faculty_edit?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(facultyService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_FACULTY_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenSubmitEditFormEntityWithId_thenUpdateEntity() throws Exception {
        FacultyDto facultyDto = FacultyDtoModelRepository.getModel1();
        facultyDto.setId(1);
        String expectedTitle = "Faculty edit";
        String expectedSuccessMessage = "Record faculty was updated!";
        Model model = new ExtendedModelMap();

        when(facultyService.update(facultyDto)).thenReturn(facultyDto);
        when(bindingResult.hasErrors()).thenReturn(false);

        String actuallyView = facultyController.editPostEntity(facultyDto, bindingResult, model);
        
        assertThat(PATH_HTML_FACULTY).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(facultyDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_FACULTY));
    }

    @Test
    void whenSubmitEditFormEntityWithoutId_thenCreateEntity() throws Exception {
        FacultyDto facultyDto = FacultyDtoModelRepository.getModel6();
        String expectedTitle = "Faculty edit";
        String expectedSuccessMessage = "Record faculty was created!";
        Model model = new ExtendedModelMap();

        when(facultyService.create(facultyDto)).thenReturn(facultyDto);

        String actuallyView = facultyController.editPostEntity(facultyDto, bindingResult, model);

        assertThat(PATH_HTML_FACULTY).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(facultyDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_FACULTY));
    }
    
    @Test
    void whenInvokeCreateExistsEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record faculty was not created! The record already exists!";
        FacultyDto facultyDto = FacultyDtoModelRepository.getModel1();
        Model model = new ExtendedModelMap();

        doThrow(EntityAlreadyExistsException.class).when(facultyService).create(facultyDto);
      
        String actuallyView = facultyController.editPostEntity(facultyDto, bindingResult, model);

        assertThat(PATH_HTML_FACULTY_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(facultyDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_FACULTY));
    }
  
    @Test
    void whenInvokeEditNotFoundEntity_thenExpectErrorMessage() throws Exception {
        FacultyDto facultyDto = FacultyDtoModelRepository.getModel1();
        facultyDto.setId(1);
        String expectedErrorMessage = "Faculty " + facultyDto + " not found!";
        Model model = new ExtendedModelMap();

        doThrow(EntityNotFoundException.class).when(facultyService).update(facultyDto);
      
        String actuallyView = facultyController.editPostEntity(facultyDto, bindingResult, model);

        assertThat(PATH_HTML_FACULTY_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(facultyDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_FACULTY));
    }
  
    @Test
    void whenInvokeEditNotValidEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record faculty was not updated/created! The data is not valid!";
        FacultyDto facultyDto = FacultyDtoModelRepository.getModel1();
        facultyDto.setId(1);
        Model model = new ExtendedModelMap();

        doThrow(EntityNotValidException.class).when(facultyService).update(facultyDto);
      
        String actuallyView = facultyController.editPostEntity(facultyDto, bindingResult, model);

        assertThat(PATH_HTML_FACULTY_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(facultyDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_FACULTY));
    }
    
}
