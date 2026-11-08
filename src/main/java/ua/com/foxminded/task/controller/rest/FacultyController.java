package ua.com.foxminded.task.controller.rest;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.service.FacultyService;

@RestController("facultyRestController")
@RequestMapping("/api")
@Tag(name = "Faculty", description = "Faculty management System")
public class FacultyController {

    private Logger logger;
    private FacultyService facultyService;

    @Autowired
    public FacultyController(Logger logger, FacultyService facultyService) {
        this.logger = logger;
        this.facultyService = facultyService;
    }

    @GetMapping(path = "/faculties", produces = "application/json")
    @Operation(description = "View a list of available faculties")
    public List<FacultyDto> getEntities() {
        logger.debug("getEntities()");
        return facultyService.findAllDto();
    }

    @GetMapping(path = "/faculties/{id}", produces = "application/json")
    @Operation(description = "View the faculty by id")
    public FacultyDto getEntityById(@PathVariable("id") int id) {
        logger.debug("getEntityById()");
        return facultyService.findByIdDto(id);
    }

    @PostMapping(path = "/faculties", produces = "application/json")
    @Operation(description = "Create (if id=0) or update the faculty")
    public FacultyDto saveEntity(@Valid @RequestBody FacultyDto facultyDto) {
        logger.debug("saveEntity()");
        if (facultyDto.getId() != 0) {
            facultyDto = facultyService.update(facultyDto);
        } else {
            facultyDto = facultyService.create(facultyDto);
        }
        return facultyDto;
    }
}
