package ua.com.foxminded.task.service.impl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import ua.com.foxminded.task.dao.AuditoryTypeRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.AuditoryType;
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.domain.repository.AuditoryTypeModelRepository;
import ua.com.foxminded.task.domain.repository.dto.AuditoryTypeDtoModelRepository;

class AuditoryTypeServiceImplTest {

    @Mock
    private Logger logger;
    @Mock
    private AuditoryTypeRepository auditoryTypeRepository;
    @InjectMocks
    private AuditoryTypeServiceImpl auditoryTypeService;

    @BeforeEach
    void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenFindById_thenFindEntity() {
        AuditoryType expectedAuditoryType = AuditoryTypeModelRepository.getModel1();
        doReturn(expectedAuditoryType).when(auditoryTypeRepository).getOne(1);

        AuditoryType actuallyAuditoryType = auditoryTypeService.findById(1);

        verify(auditoryTypeRepository, times(1)).getOne(anyInt());
        assertEquals(expectedAuditoryType, actuallyAuditoryType);
    }

    @Test
    void whenFindById_thenFindEntityAndConvertItToDto() {
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();
        AuditoryTypeDto auditoryTypeDtoExpected = AuditoryTypeDtoModelRepository.getModel1();
        doReturn(auditoryType).when(auditoryTypeRepository).getOne(1);

        AuditoryTypeDto auditoryTypeDtoActually = auditoryTypeService.findByIdDto(1);

        verify(auditoryTypeRepository, times(1)).getOne(anyInt());
        assertEquals(auditoryTypeDtoExpected, auditoryTypeDtoActually);
    }

    @Test
    void whenFindByAll_thenFindEntitiesAndConvertItToDto() {
        List<AuditoryType> auditoryTypes = AuditoryTypeModelRepository.getModels();
        List<AuditoryTypeDto> auditoryTypeDtosExpected = AuditoryTypeDtoModelRepository.getModels();
        doReturn(auditoryTypes).when(auditoryTypeRepository).findAll();

        List<AuditoryTypeDto> auditoryTypeDtosActually = auditoryTypeService.findAllDto();

        verify(auditoryTypeRepository, times(1)).findAll();
        assertEquals(auditoryTypeDtosExpected, auditoryTypeDtosActually);
    }

    @Test
    void whenCreate_thenInvocCreateDaoClass() {
        AuditoryTypeDto auditoryTypeDto = AuditoryTypeDtoModelRepository.getModel1();
        AuditoryType auditoryTypeInput = AuditoryTypeModelRepository.getModel1();
        AuditoryType auditoryTypeExpected = AuditoryTypeModelRepository.getModel1();

        doReturn(auditoryTypeExpected).when(auditoryTypeRepository).saveAndFlush(auditoryTypeInput);

        AuditoryTypeDto auditoryTypeDtoActually = auditoryTypeService.create(auditoryTypeDto);

        verify(auditoryTypeRepository, times(1)).saveAndFlush(auditoryTypeInput);
        assertEquals(auditoryTypeDto, auditoryTypeDtoActually);
    }

    @Test
    void whenUpdate_thenInvocUpdateDaoClass() {
        AuditoryTypeDto auditoryTypeDto = AuditoryTypeDtoModelRepository.getModel1();
        auditoryTypeDto.setId(1);
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();
        auditoryType.setId(1);

        doReturn(auditoryType).when(auditoryTypeRepository).saveAndFlush(auditoryType);
        doReturn(true).when(auditoryTypeRepository).existsById(auditoryTypeDto.getId());
        doReturn(auditoryType).when(auditoryTypeRepository).getOne(auditoryTypeDto.getId());

        AuditoryTypeDto auditoryTypeDtoActually = auditoryTypeService.update(auditoryTypeDto);

        verify(auditoryTypeRepository, times(1)).saveAndFlush(auditoryType);
        verify(auditoryTypeRepository, times(1)).getOne(auditoryTypeDto.getId());
        assertEquals(auditoryTypeDto, auditoryTypeDtoActually);
    }

    @Test
    void whenFindByType_thenInvokeMethod() {
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();
        auditoryType.setId(1);
        String expectedType = auditoryType.getType();
        doReturn(auditoryType).when(auditoryTypeRepository).findByType(expectedType);

        AuditoryType actuallyAuditoryType = auditoryTypeService.findByType(expectedType);

        verify(auditoryTypeRepository, times(1)).findByType(expectedType);
        assertEquals(expectedType, actuallyAuditoryType.getType());
    }

    @Test
    void whenCreateRecordEntityWithId_thenThrowException() {
        AuditoryTypeDto auditoryTypeDto = new AuditoryTypeDto();
        auditoryTypeDto.setId(1);

        assertThatThrownBy(() -> auditoryTypeService.create(auditoryTypeDto))
            .isInstanceOf(EntityAlreadyExistsException.class)
            .hasMessage("create() auditoryTypeDto: %s", auditoryTypeDto);
    }

    @Test
    void whenCreateRecordWithNotValidEntity_thenThrowException() {
        AuditoryTypeDto auditoryTypeDto = AuditoryTypeDtoModelRepository.getModel1();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();

        doThrow(DataIntegrityViolationException.class).when(auditoryTypeRepository).saveAndFlush(auditoryType);

        assertThatThrownBy(() -> auditoryTypeService.create(auditoryTypeDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("create() auditoryType: " + auditoryType);
    }

    @Test
    void whenUpdateRecordWithNotValidEntity_thenThrowException() {
        AuditoryTypeDto auditoryTypeDto = AuditoryTypeDtoModelRepository.getModel1();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();

        doReturn(true).when(auditoryTypeRepository).existsById(auditoryType.getId());
        doThrow(DataIntegrityViolationException.class).when(auditoryTypeRepository).saveAndFlush(auditoryType);

        assertThatThrownBy(() -> auditoryTypeService.update(auditoryTypeDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("update() auditoryType: " + auditoryType);
    }

    @Test
    void whenUpdateRecordNotFound_thenThrowException() {
        AuditoryTypeDto auditoryTypeDto = AuditoryTypeDtoModelRepository.getModel1();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();

        doReturn(false).when(auditoryTypeRepository).existsById(auditoryType.getId());

        assertThatThrownBy(() -> auditoryTypeService.update(auditoryTypeDto))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Auditory Type not exist!");
    }
}
