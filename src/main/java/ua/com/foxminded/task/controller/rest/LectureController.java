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
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.service.LectureService;

@RestController("lectureRestController")
@RequestMapping("/api")
@Tag(name = "Lecture", description = "Lecture management System")
public class LectureController {

    private Logger logger;
    private LectureService lectureService;

    @Autowired
    public LectureController(Logger logger, LectureService lectureService) {
        this.logger = logger;
        this.lectureService = lectureService;
    }

    @GetMapping(path = "/lecturies", produces = "application/json")
    @Operation(description = "View a list of available lecturies")
    public List<LectureDto> getEntities() {
        logger.debug("getEntities()");
        return lectureService.findAllDto();
    }

    @GetMapping(path = "/lecturies/{id}", produces = "application/json")
    @Operation(description = "View the lecture by id")
    public LectureDto getEntityById(@PathVariable("id") int id) {
        logger.debug("getEntityById()");
        return lectureService.findByIdDto(id);
    }

    @PostMapping(path = "/lecturies", produces = "application/json")
    @Operation(description = "Create (if id=0) or update the lecture")
    public LectureDto saveEntity(@Valid @RequestBody LectureDto lectureDto) {
        logger.debug("saveEntity()");
        if (lectureDto.getId() != 0) {
            lectureDto = lectureService.update(lectureDto);
        } else {
            lectureDto = lectureService.create(lectureDto);
        }
        return lectureDto;
    }
}
