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
import ua.com.foxminded.task.domain.dto.DepartmentDto;
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.domain.repository.dto.DepartmentDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.FacultyDtoModelRepository;
import ua.com.foxminded.task.service.DepartmentService;
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
public class DepartmentControllerTest {

    private MockMvc mockMvc;
    private DepartmentController departmentController;

    @MockBean
    private FacultyService facultyService;

    @MockBean
    private DepartmentService departmentService;
    
    @MockBean
    private Logger logger;
    
    @MockBean
    private BindingResult bindingResult;

    private static final String PATH_HTML_DEPARTMENT = "department/department";
    private static final String PATH_HTML_DEPARTMENTS = "department/departments";
    private static final String PATH_HTML_DEPARTMENT_EDIT = "department/department_edit";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    private static final String ATTRIBUTE_HTML_DEPARTMENT = "departmentDto";
    private static final String ATTRIBUTE_HTML_DEPARTMENTS = "departments";
    private static final String ATTRIBUTE_HTML_FACULTIES = "faculties";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";

    @BeforeEach
    public void init() {
        departmentController = new DepartmentController(logger, departmentService, facultyService);
        mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
    }
    
    @Test
    void whenRetrieveAllEntity_thenExpectListOfEntities() throws Exception {
        List<DepartmentDto> departmentDtos = DepartmentDtoModelRepository.getModels();
        String expectedTitle = "Departments";
        String httpRequest = "/departments";

        when(departmentService.findAllDto()).thenReturn(departmentDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_DEPARTMENTS, equalTo(departmentDtos)))
                .andExpect(forwardedUrl(PATH_HTML_DEPARTMENTS))
                .andDo(print());
    }
    
    @Test
    void whenRetrieveTheEntity_thenExpectEntityById() throws Exception {
        DepartmentDto departmentDto = DepartmentDtoModelRepository.getModel1();
        int id = 1;
        String httpRequest = "/department?id=" + id;
        String expectedTitle = "Department";

        when(departmentService.findByIdDto(id)).thenReturn(departmentDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_DEPARTMENT, equalTo(departmentDto)))
                .andExpect(forwardedUrl(PATH_HTML_DEPARTMENT))
                .andDo(print());
    }

    @Test
    void whenInvokeByBlankId_thenErrorMessage() throws Exception {
        String expectedErrorMessage = "You id is blank";
        String httpRequest = "/department?id=";

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_DEPARTMENT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeNoFoundEntities_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Department by id#" + id + " not found!";
        String httpRequest = "/department?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(departmentService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_DEPARTMENT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEntitiesWithIncorrectNumberFormatId_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Department id# must be numeric!";
        String httpRequest = "/department?id=" + id;
        
        doThrow(NumberFormatException.class).when(departmentService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_DEPARTMENT))
                .andDo(print());
    }
    
    @Test
    void whenRetriveEditExistsEntity_thenExpectFormWithEntityField() throws Exception {
        DepartmentDto departmentDto = DepartmentDtoModelRepository.getModel1();
        List<FacultyDto> facultyDtos = FacultyDtoModelRepository.getModels();
        int id = 1;
        String httpRequest = "/department_edit?id=" + id;
        String expectedTitle = "Department edit";

        when(departmentService.findByIdDto(id)).thenReturn(departmentDto);
        when(facultyService.findAllDto()).thenReturn(facultyDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_DEPARTMENT, equalTo(departmentDto)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_FACULTIES, equalTo(facultyDtos)))
                .andExpect(forwardedUrl(PATH_HTML_DEPARTMENT_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEditEntityWithNoEntityNumber_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Problem with finding department";
        List<FacultyDto> facultyDtos = FacultyDtoModelRepository.getModels();
        int id = 1;
        String httpRequest = "/department_edit?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(departmentService).findByIdDto(id);
        when(facultyService.findAllDto()).thenReturn(facultyDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_DEPARTMENT_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenSubmitEditFormEntityWithId_thenUpdateEntity() throws Exception {
        DepartmentDto departmentDto = DepartmentDtoModelRepository.getModel1();
        departmentDto.setId(1);
        String expectedTitle = "Department edit";
        String expectedSuccessMessage = "Record department was updated!";
        Model model = new ExtendedModelMap();

        when(departmentService.update(departmentDto)).thenReturn(departmentDto);
        when(bindingResult.hasErrors()).thenReturn(false);

        String actuallyView = departmentController.editPostEntity(departmentDto, bindingResult, model);
        
        assertThat(PATH_HTML_DEPARTMENT).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(departmentDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_DEPARTMENT));
    }

    @Test
    void whenSubmitEditFormEntityWithoutId_thenCreateEntity() throws Exception {
        DepartmentDto departmentDto = DepartmentDtoModelRepository.getModel5();
        String expectedTitle = "Department edit";
        String expectedSuccessMessage = "Record department was created";
        Model model = new ExtendedModelMap();

        when(departmentService.create(departmentDto)).thenReturn(departmentDto);

        String actuallyView = departmentController.editPostEntity(departmentDto, bindingResult, model);

        assertThat(PATH_HTML_DEPARTMENT).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(departmentDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_DEPARTMENT));
    }
    
    @Test
    void whenInvokeCreateExistsEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record department was not created! The record already exists!";
        DepartmentDto departmentDto = DepartmentDtoModelRepository.getModel1();
        Model model = new ExtendedModelMap();

        doThrow(EntityAlreadyExistsException.class).when(departmentService).create(departmentDto);
      
        String actuallyView = departmentController.editPostEntity(departmentDto, bindingResult, model);

        assertThat(PATH_HTML_DEPARTMENT_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(departmentDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_DEPARTMENT));
    }
  
    @Test
    void whenInvokeEditNotFoundEntity_thenExpectErrorMessage() throws Exception {
        DepartmentDto departmentDto = DepartmentDtoModelRepository.getModel1();
        departmentDto.setId(1);
        String expectedErrorMessage = "Department " + departmentDto + " not found!";
        Model model = new ExtendedModelMap();

        doThrow(EntityNotFoundException.class).when(departmentService).update(departmentDto);
      
        String actuallyView = departmentController.editPostEntity(departmentDto, bindingResult, model);

        assertThat(PATH_HTML_DEPARTMENT_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(departmentDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_DEPARTMENT));
    }
  
    @Test
    void whenInvokeEditNotValidEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record department was not updated/created! The data is not valid!";
        DepartmentDto departmentDto = DepartmentDtoModelRepository.getModel1();
        departmentDto.setId(1);
        Model model = new ExtendedModelMap();

        doThrow(EntityNotValidException.class).when(departmentService).update(departmentDto);
      
        String actuallyView = departmentController.editPostEntity(departmentDto, bindingResult, model);

        assertThat(PATH_HTML_DEPARTMENT_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(departmentDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_DEPARTMENT));
    }
    
}
