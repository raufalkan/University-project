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
import ua.com.foxminded.task.domain.dto.DepartmentDto;
import ua.com.foxminded.task.service.DepartmentService;

@RestController("departmentRestController")
@RequestMapping("/api")
@Tag(name = "Department", description = "Department management System")
public class DepartmentController {

    private Logger logger;
    private DepartmentService departmentService;

    @Autowired
    public DepartmentController(Logger logger, DepartmentService departmentService) {
        this.logger = logger;
        this.departmentService = departmentService;
    }

    @GetMapping(path = "/departments", produces = "application/json")
    @Operation(description = "View a list of available departments")
    public List<DepartmentDto> getEntities() {
        logger.debug("getEntities()");
        return departmentService.findAllDto();
    }

    @GetMapping(path = "/departments/{id}", produces = "application/json")
    @Operation(description = "View the department by id")
    public DepartmentDto getEntityById(@PathVariable("id") int id) {
        logger.debug("getEntityById()");
        return departmentService.findByIdDto(id);
    }

    @PostMapping(path = "/departments", produces = "application/json")
    @Operation(description = "Create (if id=0) or update the department")
    public DepartmentDto saveEntity(@Valid @RequestBody DepartmentDto departmentDto) {
        logger.debug("saveEntity()");
        if (departmentDto.getId() != 0) {
            departmentDto = departmentService.update(departmentDto);
        } else {
            departmentDto = departmentService.create(departmentDto);
        }
        return departmentDto;
    }
}
