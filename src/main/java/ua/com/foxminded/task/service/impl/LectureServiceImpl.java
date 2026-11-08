package ua.com.foxminded.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ua.com.foxminded.task.dao.LectureRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Lecture;
import ua.com.foxminded.task.domain.dto.LectureDto;
import ua.com.foxminded.task.service.LectureService;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

@Service
public class LectureServiceImpl implements LectureService {

    private LectureRepository lectureRepository;
    private Logger logger;

    @Autowired
    public LectureServiceImpl(Logger logger, LectureRepository lectureRepository) {
        this.logger = logger;
        this.lectureRepository = lectureRepository;
    }

    public Lecture findById(int id) {
        logger.debug("findById() [id:{}]", id);
        return lectureRepository.getOne(id);
    }

    @Override
    public LectureDto findByIdDto(int id) {
        logger.debug("findById() [id:{}]", id);
        Lecture lecture = findById(id);
        return ConverterToDtoService.convert(lecture);
    }

    @Override
    public List<LectureDto> findAllDto() {
        logger.debug("findAllDto()");
        return lectureRepository.findAll().stream().map(ConverterToDtoService::convert).collect(Collectors.toList());
    }

    @Override
    public LectureDto create(LectureDto lectureDto) {
        logger.debug("create() [lectureDto:{}]", lectureDto);
        if (lectureDto.getId() != 0) {
            logger.warn("create() [lectureDto:{}]", lectureDto);
            throw new EntityAlreadyExistsException("create() lectureDto: " + lectureDto);
        }
        Lecture lecture = retriveEntityFromDto(lectureDto);
        try {
            lecture = lectureRepository.saveAndFlush(lecture);
        } catch (DataIntegrityViolationException e) {
            logger.warn("create() [lecture:{}], exception:{}", lecture, e);
            throw new EntityNotValidException("create() lecture: " + lecture, e);
        }
        return ConverterToDtoService.convert(lecture);
    }

    @Override
    public LectureDto update(LectureDto lectureDto) {
        logger.debug("update() [lectureDto:{}]", lectureDto);
        int lectureId = lectureDto.getId();
        if (!lectureRepository.existsById(lectureId)) {
            throw new EntityNotFoundException("Lecture not exist!");
        }
        Lecture lecture = retriveEntityFromDto(lectureDto);
        try {
            lecture = lectureRepository.saveAndFlush(lecture);
        } catch (DataIntegrityViolationException e) {
            logger.warn("update() [lecture:{}], exception:{}", lecture, e);
            throw new EntityNotValidException("update() lecture: " + lecture, e);
        }
        return ConverterToDtoService.convert(lecture);
    }

    @Override
    public Lecture findByNumber(String number) {
        logger.debug("findByNumber() [number:{}]", number);
        return lectureRepository.findByNumber(number);
    }

    private Lecture retriveEntityFromDto(LectureDto lectureDto) {
        Lecture lecture = (lectureDto.getId() != 0) ? lectureRepository.getOne(lectureDto.getId()) : new Lecture();
        lecture.setNumber(lectureDto.getNumber());
        lecture.setStartTime(lectureDto.getStartTime());
        lecture.setEndTime(lectureDto.getEndTime());
        return lecture;
    }
}
