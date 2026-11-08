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
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.service.AuditoryTypeService;

@RestController("auditoryTypeRestController")
@RequestMapping("/api")
@Tag(name = "Auditory type", description = "Auditory type management System")
public class AuditoryTypeController {

    private Logger logger;
    private AuditoryTypeService auditoryTypeService;

    @Autowired
    public AuditoryTypeController(Logger logger, AuditoryTypeService auditoryTypeService) {
        this.logger = logger;
        this.auditoryTypeService = auditoryTypeService;
    }

    @GetMapping(path = "/auditory-types", produces = "application/json")
    @Operation(description = "View a list of available auditory types")
    public List<AuditoryTypeDto> getEntities() {
        logger.debug("getEntities()");
        return auditoryTypeService.findAllDto();
    }

    @GetMapping(path = "/auditory-types/{id}", produces = "application/json")
    @Operation(description = "View the auditory type by id")
    public AuditoryTypeDto getEntityById(@PathVariable("id") int id) {
        logger.debug("getEntityById()");
        return auditoryTypeService.findByIdDto(id);
    }

    @PostMapping(path = "/auditory-types", produces = "application/json")
    @Operation(description = "Create (if id=0) or update the auditory types")
    public AuditoryTypeDto saveEntity(@Valid @RequestBody AuditoryTypeDto auditoryTypeDto) {
        logger.debug("saveEntity()");
        if (auditoryTypeDto.getId() != 0) {
            auditoryTypeDto = auditoryTypeService.update(auditoryTypeDto);
        } else {
            auditoryTypeDto = auditoryTypeService.create(auditoryTypeDto);
        }
        return auditoryTypeDto;
    }
}
