package ua.com.foxminded.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ua.com.foxminded.task.dao.FacultyRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Faculty;
import ua.com.foxminded.task.domain.dto.FacultyDto;
import ua.com.foxminded.task.service.FacultyService;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

@Service
public class FacultyServiceImpl implements FacultyService {

    private FacultyRepository facultyRepository;
    private Logger logger;

    @Autowired
    public FacultyServiceImpl(Logger logger, FacultyRepository facultyRepository) {
        this.logger = logger;
        this.facultyRepository = facultyRepository;
    }

    public Faculty findById(int id) {
        logger.debug("findById() [id:{}]", id);
        return facultyRepository.getOne(id);
    }

    @Override
    public FacultyDto findByIdDto(int id) {
        logger.debug("findById() [id:{}]", id);
        Faculty faculty = findById(id);
        return ConverterToDtoService.convert(faculty);
    }

    @Override
    public List<FacultyDto> findAllDto() {
        logger.debug("findAllDto()");
        return facultyRepository.findAll().stream().map(ConverterToDtoService::convert).collect(Collectors.toList());
    }

    @Override
    public FacultyDto create(FacultyDto facultyDto) {
        logger.debug("create() [facultyDto:{}]", facultyDto);
        if (facultyDto.getId() != 0) {
            logger.warn("create() [facultyDto:{}]", facultyDto);
            throw new EntityAlreadyExistsException("create() facultyDto: " + facultyDto);
        }
        Faculty faculty = retriveEntityFromDto(facultyDto);
        try {
            faculty = facultyRepository.saveAndFlush(faculty);
        } catch (DataIntegrityViolationException e) {
            logger.warn("create() [faculty:{}], exception:{}", faculty, e);
            throw new EntityNotValidException("create() faculty: " + faculty, e);
        }
        return ConverterToDtoService.convert(faculty);
    }

    @Override
    public FacultyDto update(FacultyDto facultyDto) {
        logger.debug("update() [facultyDto:{}]", facultyDto);
        int facultyId = facultyDto.getId();
        if (!facultyRepository.existsById(facultyId)) {
            throw new EntityNotFoundException("Faculty not exist!");
        }
        Faculty faculty = retriveEntityFromDto(facultyDto);
        try {
            faculty = facultyRepository.saveAndFlush(faculty);
        } catch (DataIntegrityViolationException e) {
            logger.warn("update() [faculty:{}], exception:{}", faculty, e);
            throw new EntityNotValidException("update() faculty: " + faculty, e);
        }
        return ConverterToDtoService.convert(faculty);
    }

    @Override
    public Faculty findByTitle(String title) {
        logger.debug("findByTitle() [title:{}]", title);
        return facultyRepository.findByTitle(title);
    }

    private Faculty retriveEntityFromDto(FacultyDto facultyDto) {
        Faculty faculty = (facultyDto.getId() != 0) ? facultyRepository.getOne(facultyDto.getId()) : new Faculty();
        faculty.setTitle(facultyDto.getTitle());
        return faculty;
    }
}
