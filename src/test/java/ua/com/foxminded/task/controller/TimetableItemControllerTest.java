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
import ua.com.foxminded.task.domain.dto.AuditoryDto;
import ua.com.foxminded.task.domain.dto.GroupDto;
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.domain.dto.TeacherDto;
import ua.com.foxminded.task.domain.dto.TimetableItemDto;
import ua.com.foxminded.task.domain.repository.dto.AuditoryDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.GroupDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.LectureDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.SubjectDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.TeacherDtoModelRepository;
import ua.com.foxminded.task.domain.repository.dto.TimetableItemDtoModelRepository;
import ua.com.foxminded.task.service.AuditoryService;
import ua.com.foxminded.task.service.GroupService;
import ua.com.foxminded.task.service.LectureService;
import ua.com.foxminded.task.service.StudentService;
import ua.com.foxminded.task.service.SubjectService;
import ua.com.foxminded.task.service.TeacherService;
import ua.com.foxminded.task.service.TimetableItemService;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
public class TimetableItemControllerTest {

    private MockMvc mockMvc;
    private TimetableItemController timetableItemController;

    @MockBean
    private TimetableItemService timetableItemService;

    @MockBean
    private SubjectService subjectService;

    @MockBean
    private AuditoryService auditoryService;

    @MockBean
    private GroupService groupService;

    @MockBean
    private LectureService lectureService;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private StudentService studentService;

    @MockBean
    private Logger logger;

    @MockBean
    private BindingResult bindingResult;

    private static final String PATH_HTML_TIMETABLEITEM = "timetable-item/timetable-item";
    private static final String PATH_HTML_TIMETABLEITEMS = "timetable-item/timetable-items";
    private static final String PATH_HTML_TIMETABLEITEM_EDIT = "timetable-item/timetable-item_edit";
    private static final String ATTRIBUTE_HTML_TIMETABLEITEM = "timetableItemDto";
    private static final String ATTRIBUTE_HTML_TIMETABLEITEM_FILTERS = "timetableFiltersDto";
    private static final String ATTRIBUTE_HTML_TIMETABLEITEMS = "timetableItems";
    private static final String ATTRIBUTE_HTML_SUBJECTS = "subjects";
    private static final String ATTRIBUTE_HTML_AUDITORIES = "auditories";
    private static final String ATTRIBUTE_HTML_GROUPS = "allGroups";
    private static final String ATTRIBUTE_HTML_LECTURIES = "lecturies";
    private static final String ATTRIBUTE_HTML_TEACHERS = "teachers";
    private static final String ATTRIBUTE_HTML_ERROR_MESSAGE = "errorMessage";
    private static final String ATTRIBUTE_HTML_SUCCESS_MESSAGE = "successMessage";
    private static final String ATTRIBUTE_HTML_TITLE = "title";
    
    @BeforeEach
    public void init() {
        timetableItemController = new TimetableItemController(logger,
                                                              timetableItemService,
                                                              subjectService,
                                                              auditoryService,
                                                              groupService,
                                                              lectureService,
                                                              teacherService,
                                                              studentService);
        mockMvc = MockMvcBuilders.standaloneSetup(timetableItemController).build();
    }

    @Test
    void whenRetrieveAllEntity_thenExpectListOfEntities() throws Exception {
        List<TimetableItemDto> timetableItemDtos = TimetableItemDtoModelRepository.getModels();
        String expectedTitle = "Timetable item";
        String httpRequest = "/timetable-items";

        when(timetableItemService.findAllByFilters(any())).thenReturn(timetableItemDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_TIMETABLEITEMS, equalTo(timetableItemDtos)))
                .andExpect(forwardedUrl(PATH_HTML_TIMETABLEITEMS))
                .andDo(print());
    }
    
    @Test
    void whenRetrieveTheEntity_thenExpectEntityById() throws Exception {
        TimetableItemDto timetableItemDto = TimetableItemDtoModelRepository.getModel1();
        int id = 1;
        String httpRequest = "/timetable-item?id=" + id;
        String expectedTitle = "Timetable item";

        when(timetableItemService.findByIdDto(id)).thenReturn(timetableItemDto);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_TIMETABLEITEM, equalTo(timetableItemDto)))
                .andExpect(forwardedUrl(PATH_HTML_TIMETABLEITEM))
                .andDo(print());
    }

    @Test
    void whenInvokeByBlankId_thenErrorMessage() throws Exception {
        String expectedErrorMessage = "You id is blank";
        String httpRequest = "/timetable-item?id=";

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_TIMETABLEITEM))
                .andDo(print());
    }
    
    @Test
    void whenInvokeNoFoundEntities_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Timetable item by id#" + id + " not found!";
        String httpRequest = "/timetable-item?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(timetableItemService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_TIMETABLEITEM))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEntitiesWithIncorrectNumberFormatId_thenErrorMessage() throws Exception {
        int id = 1;
        String expectedErrorMessage = "Timetable item id# must be numeric!";
        String httpRequest = "/timetable-item?id=" + id;
        
        doThrow(NumberFormatException.class).when(timetableItemService).findByIdDto(id);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_TIMETABLEITEM))
                .andDo(print());
    }
    
    @Test
    void whenRetrieveEditExistsEntity_thenExpectFormWithEntityField() throws Exception {
        TimetableItemDto timetableItemDto = TimetableItemDtoModelRepository.getModel1();
        List<SubjectDto> subjectDtos = SubjectDtoModelRepository.getModels1();
        List<AuditoryDto> auditoryDtos = AuditoryDtoModelRepository.getModels();
        List<GroupDto> groupDtos = GroupDtoModelRepository.getModels();
        List<LectureDto> lectureDtos = LectureDtoModelRepository.getModels();
        List<TeacherDto> teacherDtos = TeacherDtoModelRepository.getModels1();
        int id = 1;
        String httpRequest = "/timetable-item_edit?id=" + id;
        String expectedTitle = "Timetable item edit";

        when(timetableItemService.findByIdDto(id)).thenReturn(timetableItemDto);
        when(subjectService.findAllDto()).thenReturn(subjectDtos);
        when(auditoryService.findAllDto()).thenReturn(auditoryDtos);
        when(groupService.findAllDto()).thenReturn(groupDtos);
        when(lectureService.findAllDto()).thenReturn(lectureDtos);
        when(teacherService.findAllDto()).thenReturn(teacherDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_TITLE, equalTo(expectedTitle)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_TIMETABLEITEM, equalTo(timetableItemDto)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_SUBJECTS, equalTo(subjectDtos)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_AUDITORIES, equalTo(auditoryDtos)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_GROUPS, equalTo(groupDtos)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_LECTURIES, equalTo(lectureDtos)))
                .andExpect(model().attribute(ATTRIBUTE_HTML_TEACHERS, equalTo(teacherDtos)))
                .andExpect(forwardedUrl(PATH_HTML_TIMETABLEITEM_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenInvokeEditEntityWithNoEntityNumber_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Problem with finding timetable item";
        List<SubjectDto> subjectDtos = SubjectDtoModelRepository.getModels1();
        List<AuditoryDto> auditoryDtos = AuditoryDtoModelRepository.getModels();
        List<GroupDto> groupDtos = GroupDtoModelRepository.getModels();
        List<LectureDto> lectureDtos = LectureDtoModelRepository.getModels();
        List<TeacherDto> teacherDtos = TeacherDtoModelRepository.getModels1();
        int id = 1;
        String httpRequest = "/timetable-item_edit?id=" + id;
        
        doThrow(EntityNotFoundException.class).when(timetableItemService).findByIdDto(id);
        when(subjectService.findAllDto()).thenReturn(subjectDtos);
        when(auditoryService.findAllDto()).thenReturn(auditoryDtos);
        when(groupService.findAllDto()).thenReturn(groupDtos);
        when(lectureService.findAllDto()).thenReturn(lectureDtos);
        when(teacherService.findAllDto()).thenReturn(teacherDtos);

        this.mockMvc.perform(get(httpRequest).accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(status().isOk())
                .andExpect(model().attribute(ATTRIBUTE_HTML_ERROR_MESSAGE, equalTo(expectedErrorMessage)))
                .andExpect(forwardedUrl(PATH_HTML_TIMETABLEITEM_EDIT))
                .andDo(print());
    }
    
    @Test
    void whenSubmitEditFormEntityWithId_thenUpdateEntity() throws Exception {
        TimetableItemDto timetableItemDto = TimetableItemDtoModelRepository.getModel1();
        timetableItemDto.setId(1);
        String expectedTitle = "Timetable item edit";
        String expectedSuccessMessage = "Record timetable item was updated!";
        Model model = new ExtendedModelMap();

        when(timetableItemService.update(timetableItemDto)).thenReturn(timetableItemDto);
        when(bindingResult.hasErrors()).thenReturn(false);

        String actuallyView = timetableItemController.editPostEntity(timetableItemDto, bindingResult, model);
        
        assertThat(PATH_HTML_TIMETABLEITEM).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(timetableItemDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TIMETABLEITEM));
    }

    @Test
    void whenSubmitEditFormEntityWithoutId_thenCreateEntity() throws Exception {
        TimetableItemDto timetableItemDto = TimetableItemDtoModelRepository.getModel6();
        String expectedTitle = "Timetable item edit";
        String expectedSuccessMessage = "Record timetable item was created";
        Model model = new ExtendedModelMap();

        when(timetableItemService.create(timetableItemDto)).thenReturn(timetableItemDto);

        String actuallyView = timetableItemController.editPostEntity(timetableItemDto, bindingResult, model);

        assertThat(PATH_HTML_TIMETABLEITEM).isEqualTo(actuallyView);
        assertThat(expectedTitle).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TITLE));
        assertThat(expectedSuccessMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_SUCCESS_MESSAGE));
        assertThat(timetableItemDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TIMETABLEITEM));
    }
    
    @Test
    void whenInvokeCreateExistsEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record timetable item was not created! The record already exists!";
        TimetableItemDto timetableItemDto = TimetableItemDtoModelRepository.getModel1();
        Model model = new ExtendedModelMap();

        doThrow(EntityAlreadyExistsException.class).when(timetableItemService).create(timetableItemDto);
      
        String actuallyView = timetableItemController.editPostEntity(timetableItemDto, bindingResult, model);

        assertThat(PATH_HTML_TIMETABLEITEM_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(timetableItemDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TIMETABLEITEM));
    }
  
    @Test
    void whenInvokeEditNotFoundEntity_thenExpectErrorMessage() throws Exception {
        TimetableItemDto timetableItemDto = TimetableItemDtoModelRepository.getModel1();
        timetableItemDto.setId(1);
        String expectedErrorMessage = "Timetable item " + timetableItemDto + " not found!";
        Model model = new ExtendedModelMap();

        doThrow(EntityNotFoundException.class).when(timetableItemService).update(timetableItemDto);
      
        String actuallyView = timetableItemController.editPostEntity(timetableItemDto, bindingResult, model);

        assertThat(PATH_HTML_TIMETABLEITEM_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(timetableItemDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TIMETABLEITEM));
    }
  
    @Test
    void whenInvokeEditNotValidEntity_thenExpectErrorMessage() throws Exception {
        String expectedErrorMessage = "Record timetable item was not updated/created! The data is not valid!";
        TimetableItemDto timetableItemDto = TimetableItemDtoModelRepository.getModel1();
        timetableItemDto.setId(1);
        Model model = new ExtendedModelMap();

        doThrow(EntityNotValidException.class).when(timetableItemService).update(timetableItemDto);
      
        String actuallyView = timetableItemController.editPostEntity(timetableItemDto, bindingResult, model);

        assertThat(PATH_HTML_TIMETABLEITEM_EDIT).isEqualTo(actuallyView);
        assertThat(expectedErrorMessage).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_ERROR_MESSAGE));
        assertThat(timetableItemDto).isEqualTo(model.getAttribute(ATTRIBUTE_HTML_TIMETABLEITEM));
    }
    
}
