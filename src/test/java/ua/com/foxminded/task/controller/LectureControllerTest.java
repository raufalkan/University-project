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
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.domain.repository.dto.LectureDtoModelRepository;
import ua.com.foxminded.task.service.LectureService;

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
public class LectureControllerTest {

    private MockMvc mockMvc;
    private LectureController lectureController;

    @MockBean
    private LectureService lectureService;
    
    @MockBean
    private Logger logger;
    
    @MockBean
    private BindingResult bindingResult;

    private static final String PATH_HTML_LECTURE = "lecture/lecture";
    private static final String PATH_HTML_LECTURIES = "lecture/lecturies";
    private static final String PATH_HTML_LECTURE_EDIT = "lecture/lecture_edit";
    private static final String ATTRIBUTE_HTML_LECTURE = "lectureDto";
    private static final String ATTRIBUTE_HTML_LECTURIES = "lecturies";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private static final String ATTRIBUTE_HTML_TITLE = "title";

    @BeforeEach
    public void init() {
        lectureController = new LectureController(logger, lectureService);
        mockMvc = MockMvcBuilders.standaloneSetup(lectureController).build();
    }

    @Test
    void whenRetrieveAllEntity_thenExpectListOfEntities() throws Exception {
        List<LectureDto> lectureDtos = LectureDtoModelRepository.getModels();
        String expectedTitle = "Lecture";
        String httpRequest = "/lecturies";

        when(lectureService.findAllDto()).thenReturn(lectureDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_LECTURIES, equalTo(lectureDtos)))
                .andExpect(forwardedUrl(PATH_HTML_LECTURIES))
                .andDo(print());
    }
    
    @Test
    void whenRetrieveTheEntity_thenExpectEntityById() throws Exception {
        LectureDto lectureDto = LectureDtoModelRepository.getModel1();
        int id = 1;
        String httpRequest = "/lecture?id=" + id;
        String expectedTitle = "Lecture";

        when(lectureService.findByIdDto(id)).thenReturn(lectureDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_LECTURE, equalTo(lectureDto)))
                .andExpect(forwardedUrl(PATH_HTML_LECTURE))
                .andDo(print());
    }

    @Test
    void whenInvokeByBlankId_thenErrorMessage() throws Exception {
        String expectedErrorMessage = "You id is blank";
        String httpRequest = "/lecture?id=";

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_LECTURE))
                .andDo(print());
    }
    
    @Test
    void whenInvokeNoFoundEntities_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Lecture by id#" + id + " not found!";
        String httpRequest = "/lecture?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(lectureService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_LECTURE))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEntitiesWithIncorrectNumberFormatId_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Lecture id# must be numeric!";
        String httpRequest = "/lecture?id=" + id;
        
        doThrow(NumberFormatException.class).when(lectureService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_LECTURE))
                .andDo(print());
    }
    
    @Test
    void whenRetrieveEditExistsEntity_thenExpectFormWithEntityField() throws Exception {
        LectureDto lectureDto = LectureDtoModelRepository.getModel1();
        int id = 1;
        String httpRequest = "/lecture_edit?id=" + id;
        String expectedTitle = "Lecture edit";

        when(lectureService.findByIdDto(id)).thenReturn(lectureDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_LECTURE, equalTo(lectureDto)))
                .andExpect(forwardedUrl(PATH_HTML_LECTURE_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEditEntityWithNoEntityNumber_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Problem with finding lecture";
        int id = 1;
        String httpRequest = "/lecture_edit?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(lectureService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_LECTURE_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenSubmitEditFormEntityWithId_thenUpdateEntity() throws Exception {
        LectureDto lectureDto = LectureDtoModelRepository.getModel1();
        lectureDto.setId(1);
        String expectedTitle = "Lecture edit";
        String expectedSuccessMessage = "Record lecture was updated!";
        Model model = new ExtendedModelMap();

        when(lectureService.update(lectureDto)).thenReturn(lectureDto);
        when(bindingResult.hasErrors()).thenReturn(false);

        String actuallyView = lectureController.editPostEntity(lectureDto, bindingResult, model);
        
        assertThat(PATH_HTML_LECTURE).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(lectureDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_LECTURE));
    }

    @Test
    void whenSubmitEditFormEntityWithoutId_thenCreateEntity() throws Exception {
        LectureDto lectureDto = LectureDtoModelRepository.getModel6();
        String expectedTitle = "Lecture edit";
        String expectedSuccessMessage = "Record lecture was created!";
        Model model = new ExtendedModelMap();

        when(lectureService.create(lectureDto)).thenReturn(lectureDto);

        String actuallyView = lectureController.editPostEntity(lectureDto, bindingResult, model);

        assertThat(PATH_HTML_LECTURE).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(lectureDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_LECTURE));
    }
    
    @Test
    void whenInvokeCreateExistsEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record lecture was not created! The record already exists!";
        LectureDto lectureDto = LectureDtoModelRepository.getModel1();
        Model model = new ExtendedModelMap();

        doThrow(EntityAlreadyExistsException.class).when(lectureService).create(lectureDto);
      
        String actuallyView = lectureController.editPostEntity(lectureDto, bindingResult, model);

        assertThat(PATH_HTML_LECTURE_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(lectureDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_LECTURE));
    }
  
    @Test
    void whenInvokeEditNotFoundEntity_thenExpectErrorMessage() throws Exception {
        LectureDto lectureDto = LectureDtoModelRepository.getModel1();
        lectureDto.setId(1);
        String expectedErrorMessage = "Lecture " + lectureDto + " not found!";
        Model model = new ExtendedModelMap();

        doThrow(EntityNotFoundException.class).when(lectureService).update(lectureDto);
      
        String actuallyView = lectureController.editPostEntity(lectureDto, bindingResult, model);

        assertThat(PATH_HTML_LECTURE_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(lectureDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_LECTURE));
    }
  
    @Test
    void whenInvokeEditNotValidEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record lecture was not updated/created! The data is not valid!";
        LectureDto lectureDto = LectureDtoModelRepository.getModel1();
        lectureDto.setId(1);
        Model model = new ExtendedModelMap();

        doThrow(EntityNotValidException.class).when(lectureService).update(lectureDto);
      
        String actuallyView = lectureController.editPostEntity(lectureDto, bindingResult, model);

        assertThat(PATH_HTML_LECTURE_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(lectureDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_LECTURE));
    }
    
}
