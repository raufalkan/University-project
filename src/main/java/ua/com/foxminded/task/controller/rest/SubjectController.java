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
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.service.SubjectService;

@RestController("subjectRestController")
@RequestMapping("/api")
@Tag(name = "Subject", description = "Subject management System")
public class SubjectController {

    private Logger logger;
    private SubjectService subjectService;

    @Autowired
    public SubjectController(Logger logger, SubjectService subjectService) {
        this.logger = logger;
        this.subjectService = subjectService;
    }

    @GetMapping(path = "/subjects", produces = "application/json")
    @Operation(description = "View a list of available subjects")
    public List<SubjectDto> getEntities() {
        logger.debug("getEntities()");
        return subjectService.findAllDto();
    }

    @GetMapping(path = "/subjects/{id}", produces = "application/json")
    @Operation(description = "View the subject by id")
    public SubjectDto getEntityById(@PathVariable("id") int id) {
        logger.debug("getEntityById()");
        return subjectService.findByIdDto(id);
    }

    @PostMapping(path = "/subjects", produces = "application/json")
    @Operation(description = "Create (if id=0) or update the subject")
    public SubjectDto saveEntity(@Valid @RequestBody SubjectDto subjectDto) {
        logger.debug("saveEntity()");
        if (subjectDto.getId() != 0) {
            subjectDto = subjectService.update(subjectDto);
        } else {
            subjectDto = subjectService.create(subjectDto);
        }
        return subjectDto;
    }
}
