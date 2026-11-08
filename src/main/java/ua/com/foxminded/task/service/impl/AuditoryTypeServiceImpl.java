package ua.com.foxminded.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ua.com.foxminded.task.dao.AuditoryTypeRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.AuditoryType;
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.service.AuditoryTypeService;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

@Service
public class AuditoryTypeServiceImpl implements AuditoryTypeService {

    private AuditoryTypeRepository auditoryTypeRepository;
    private Logger logger;

    @Autowired
    public AuditoryTypeServiceImpl(Logger logger, AuditoryTypeRepository auditoryTypeRepository) {
        this.logger = logger;
        this.auditoryTypeRepository = auditoryTypeRepository;
    }

    public AuditoryType findById(int id) {
        logger.debug("findById() [id:{}]", id);
        return auditoryTypeRepository.getOne(id);
    }

    @Override
    public AuditoryTypeDto findByIdDto(int id) {
        logger.debug("findById() [id:{}]", id);
        AuditoryType auditoryType = findById(id);
        return ConverterToDtoService.convert(auditoryType);
    }

    @Override
    public List<AuditoryTypeDto> findAllDto() {
        logger.debug("findAllDto()");
        return auditoryTypeRepository.findAll().stream().map(ConverterToDtoService::convert).collect(Collectors.toList());
    }

    @Override
    public AuditoryTypeDto create(AuditoryTypeDto auditoryTypeDto) {
        logger.debug("create() [auditoryTypeDto:{}]", auditoryTypeDto);
        if (auditoryTypeDto.getId() != 0) {
            logger.warn("create() [auditoryTypeDto:{}]", auditoryTypeDto);
            throw new EntityAlreadyExistsException("create() auditoryTypeDto: " + auditoryTypeDto);
        }
        AuditoryType auditoryType = retriveEntityFromDto(auditoryTypeDto);
        try {
            auditoryType = auditoryTypeRepository.saveAndFlush(auditoryType);
        } catch (DataIntegrityViolationException e) {
            logger.warn("create() [auditoryType:{}], exception:{}", auditoryType, e);
            throw new EntityNotValidException("create() auditoryType: " + auditoryType, e);
        }
        return ConverterToDtoService.convert(auditoryType);
    }

    @Override
    public AuditoryTypeDto update(AuditoryTypeDto auditoryTypeDto) {
        logger.debug("update() [auditoryTypeDto:{}]", auditoryTypeDto);
        int auditoryTypeId = auditoryTypeDto.getId();
        if (!auditoryTypeRepository.existsById(auditoryTypeId)) {
            throw new EntityNotFoundException("Auditory Type not exist!");
        }
        AuditoryType auditoryType = retriveEntityFromDto(auditoryTypeDto);
        try {
            auditoryType = auditoryTypeRepository.saveAndFlush(auditoryType);
        } catch (DataIntegrityViolationException e) {
            logger.warn("update() [auditoryType:{}], exception:{}", auditoryType, e);
            throw new EntityNotValidException("update() auditoryType: " + auditoryType, e);
        }
        return ConverterToDtoService.convert(auditoryType);
    }

    @Override
    public AuditoryType findByType(String type) {
        logger.debug("findByType() [type:{}]", type);
        return auditoryTypeRepository.findByType(type);
    }

    private AuditoryType retriveEntityFromDto(AuditoryTypeDto auditoryTypeDto) {
        AuditoryType auditoryType = (auditoryTypeDto.getId() != 0) ? auditoryTypeRepository.getOne(auditoryTypeDto.getId()) : new AuditoryType();
        auditoryType.setType(auditoryTypeDto.getType());
        return auditoryType;
    }

}
