package ua.com.foxminded.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ua.com.foxminded.task.dao.SubjectRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Subject;
import ua.com.foxminded.task.domain.dto.SubjectDto;
import ua.com.foxminded.task.service.SubjectService;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

@Service
public class SubjectServiceImpl implements SubjectService {

    private SubjectRepository subjectRepository;
    private Logger logger;

    @Autowired
    public SubjectServiceImpl(Logger logger, SubjectRepository subjectRepository) {
        this.logger = logger;
        this.subjectRepository = subjectRepository;
    }

    public Subject findById(int id) {
        logger.debug("findById() [id:{}]", id);
        return subjectRepository.getOne(id);
    }

    @Override
    public SubjectDto findByIdDto(int id) {
        logger.debug("findById() [id:{}]", id);
        Subject subject = findById(id);
        return ConverterToDtoService.convert(subject);
    }

    @Override
    public List<SubjectDto> findAllDto() {
        logger.debug("findAllDto()");
        return subjectRepository.findAll().stream().map(ConverterToDtoService::convert).collect(Collectors.toList());
    }

    @Override
    public SubjectDto create(SubjectDto subjectDto) {
        logger.debug("create() [subjectDto:{}]", subjectDto);
        if (subjectDto.getId() != 0) {
            logger.warn("create() [subjectDto:{}]", subjectDto);
            throw new EntityAlreadyExistsException("create() subjectDto: " + subjectDto);
        }
        Subject subject = retriveEntityFromDto(subjectDto);
        try {
            subject = subjectRepository.saveAndFlush(subject);
        } catch (DataIntegrityViolationException e) {
            logger.warn("create() [subject:{}], exception:{}", subject, e);
            throw new EntityNotValidException("create() subject: " + subject, e);
        }
        return ConverterToDtoService.convert(subject);
    }

    @Override
    public SubjectDto update(SubjectDto subjectDto) {
        logger.debug("update() [subjectDto:{}]", subjectDto);
        int subjectId = subjectDto.getId();
        if (!subjectRepository.existsById(subjectId)) {
            throw new EntityNotFoundException("Subject not exist!");
        }
        Subject subject = retriveEntityFromDto(subjectDto);
        try {
            subject = subjectRepository.saveAndFlush(subject);
        } catch (DataIntegrityViolationException e) {
            logger.warn("update() [subject:{}], exception:{}", subject, e);
            throw new EntityNotValidException("update() subject: " + subject, e);
        }
        return ConverterToDtoService.convert(subject);
    }

    @Override
    public Subject findByTitle(String title) {
        logger.debug("findByTitle() [title:{}]", title);
        return subjectRepository.findByTitle(title);
    }

    private Subject retriveEntityFromDto(SubjectDto subjectDto) {
        Subject subject = (subjectDto.getId() != 0) ? subjectRepository.getOne(subjectDto.getId()) : new Subject();
        subject.setTitle(subjectDto.getTitle());
        return subject;
    }
}
