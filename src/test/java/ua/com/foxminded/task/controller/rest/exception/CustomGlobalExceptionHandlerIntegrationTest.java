package ua.com.foxminded.task.controller.rest.exception;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ua.com.foxminded.task.config.TestMvcConfig;
import ua.com.foxminded.task.controller.rest.GroupController;
import ua.com.foxminded.task.controller.rest.exception.CustomGlobalExceptionHandler;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;

@WebMvcTest(GroupController.class)
@Import(TestMvcConfig.class)
public class CustomGlobalExceptionHandlerIntegrationTest {
    
    private MockMvc mockMvc;
    
    @MockBean
    private GroupController groupController;
    
    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(groupController)
             .setControllerAdvice(new CustomGlobalExceptionHandler())
             .build();
    }
    
    @Test
    public void whenHandlerEntityExistException_thenGetStatus() throws Exception {
        doThrow(EntityAlreadyExistsException.class).when(groupController).getGroups();
        
        mockMvc.perform(get("/api/groups"))
           .andExpect(status().isConflict())
           .andExpect(status().reason(containsString("Record was not created! The record already exists!")))
           .andDo(print());
    }
    
    @Test
    public void whenEntityNotValidException_thenGetStatus() throws Exception {
        doThrow(EntityNotValidException.class).when(groupController).getGroupById(1);;
        
        mockMvc.perform(get("/api/groups/1"))
           .andExpect(status().isBadRequest())
           .andExpect(status().reason(containsString("Record was not updated/created! The data is not valid!")))
           .andDo(print());
    }
    
    @Test
    public void whenNoEntityFoundException_thenGetStatus() throws Exception {
        doThrow(EntityNotFoundException.class).when(groupController).getGroupById(1);
        
        mockMvc.perform(get("/api/groups/1"))
           .andExpect(status().isNotFound())
           .andExpect(status().reason(containsString("Record not found!")))
           .andDo(print());
    }
    
}
