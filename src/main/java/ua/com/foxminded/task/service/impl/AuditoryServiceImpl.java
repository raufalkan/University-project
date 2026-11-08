package ua.com.foxminded.task.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import ua.com.foxminded.task.dao.AuditoryRepository;
import ua.com.foxminded.task.dao.AuditoryTypeRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Auditory;
import ua.com.foxminded.task.domain.AuditoryType;
import ua.com.foxminded.task.domain.dto.AuditoryDto;
import ua.com.foxminded.task.service.AuditoryService;
import ua.com.foxminded.task.service.converter.ConverterToDtoService;

@Service
public class AuditoryServiceImpl implements AuditoryService {

    private AuditoryRepository auditoryRepository;
    private AuditoryTypeRepository auditoryTypeRepository;
    private Logger logger;

    @Autowired
    public AuditoryServiceImpl(Logger logger, 
                               AuditoryRepository auditoryRepository, 
                               AuditoryTypeRepository auditoryTypeRepository) {
        this.logger = logger;
        this.auditoryRepository = auditoryRepository;
        this.auditoryTypeRepository = auditoryTypeRepository;
    }

    public Auditory findById(int id) {
        logger.debug("findById() [id:{}]", id);
        return auditoryRepository.getOne(id);
    }

    @Override
    public AuditoryDto findByIdDto(int id) {
        logger.debug("findById() [id:{}]", id);
        Auditory auditory = findById(id);
        return ConverterToDtoService.convert(auditory);
    }

    @Override
    public List<AuditoryDto> findAllDto() {
        logger.debug("findAllDto()");
        return auditoryRepository.findAll().stream().map(ConverterToDtoService::convert).collect(Collectors.toList());
    }

    @Override
    public AuditoryDto create(AuditoryDto auditoryDto) {
        logger.debug("create() [auditoryDto:{}]", auditoryDto);
        if (auditoryDto.getId() != 0) {
            logger.warn("create() [auditoryDto:{}]", auditoryDto);
            throw new EntityAlreadyExistsException("create() auditoryDto: " + auditoryDto);
        }
        Auditory auditory = retriveEntityFromDto(auditoryDto);
        try {
            auditory = auditoryRepository.saveAndFlush(auditory);
        } catch (DataIntegrityViolationException e) {
            logger.warn("create() [auditory:{}], exception:{}", auditory, e);
            throw new EntityNotValidException("create() auditory: " + auditory, e);
        }
        return ConverterToDtoService.convert(auditory);
    }

    @Override
    public AuditoryDto update(AuditoryDto auditoryDto) {
        logger.debug("update() [auditoryDto:{}]", auditoryDto);
        int auditoryId = auditoryDto.getId();
        if (!auditoryRepository.existsById(auditoryId)) {
            throw new EntityNotFoundException("Auditory not exist!");
        }
        Auditory auditory = retriveEntityFromDto(auditoryDto);
        try {
            auditory = auditoryRepository.saveAndFlush(auditory);
        } catch (DataIntegrityViolationException e) {
            logger.warn("update() [auditory:{}], exception:{}", auditory, e);
            throw new EntityNotValidException("update() auditory: " + auditory, e);
        }
        return ConverterToDtoService.convert(auditory);
    }

    @Override
    public Auditory findByAuditoryNumber(String auditoryNumber) {
        logger.debug("findByAuditoryNumber() [auditoryNumber:{}]", auditoryNumber);
        return auditoryRepository.findByAuditoryNumber(auditoryNumber);
    }

    private Auditory retriveEntityFromDto(AuditoryDto auditoryDto) {
        Auditory auditory = (auditoryDto.getId() != 0) ? auditoryRepository.getOne(auditoryDto.getId()) : new Auditory();
        AuditoryType auditoryType = (auditoryDto.getAuditoryTypeId() != 0) ? auditoryTypeRepository.getOne(auditoryDto.getAuditoryTypeId()) : null;
        auditory.setAuditoryNumber(auditoryDto.getAuditoryNumber());
        auditory.setType(auditoryType);
        auditory.setMaxCapacity(auditoryDto.getMaxCapacity());
        auditory.setDescription(auditoryDto.getDescription());
        return auditory;
    }

}
