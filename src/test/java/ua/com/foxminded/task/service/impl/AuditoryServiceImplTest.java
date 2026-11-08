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

import ua.com.foxminded.task.dao.AuditoryRepository;
import ua.com.foxminded.task.dao.AuditoryTypeRepository;
import ua.com.foxminded.task.dao.exception.EntityAlreadyExistsException;
import ua.com.foxminded.task.dao.exception.EntityNotValidException;
import ua.com.foxminded.task.domain.Auditory;
import ua.com.foxminded.task.domain.AuditoryType;
import ua.com.foxminded.task.domain.dto.AuditoryDto;
import ua.com.foxminded.task.domain.repository.AuditoryModelRepository;
import ua.com.foxminded.task.domain.repository.AuditoryTypeModelRepository;
import ua.com.foxminded.task.domain.repository.dto.AuditoryDtoModelRepository;

class AuditoryServiceImplTest {

    @Mock
    private Logger logger;
    @Mock
    private AuditoryRepository auditoryRepository;
    @Mock
    private AuditoryTypeRepository auditoryTypeRepository;
    @InjectMocks
    private AuditoryServiceImpl auditoryService;

    @BeforeEach
    public void initMocks() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenFindById_thenFindEntity() {
        Auditory expectedAuditory = AuditoryModelRepository.getModel1();
        doReturn(expectedAuditory).when(auditoryRepository).getOne(1);

        Auditory actuallyAuditory = auditoryService.findById(1);

        verify(auditoryRepository, times(1)).getOne(anyInt());
        assertEquals(expectedAuditory, actuallyAuditory);
    }

    @Test
    void whenFindById_thenFindEntityAndConvertItToDto() {
        Auditory auditory = AuditoryModelRepository.getModel1();
        AuditoryDto auditoryDtoExpected = AuditoryDtoModelRepository.getModel1();
        doReturn(auditory).when(auditoryRepository).getOne(1);

        AuditoryDto auditoryDtoActually = auditoryService.findByIdDto(1);

        verify(auditoryRepository, times(1)).getOne(anyInt());
        assertEquals(auditoryDtoExpected, auditoryDtoActually);
    }

    @Test
    void whenFindByAll_thenFindEntitiesAndConvertItToDto() {
        List<Auditory> auditories = AuditoryModelRepository.getModels();
        List<AuditoryDto> auditoryDtosExpected = AuditoryDtoModelRepository.getModels();
        doReturn(auditories).when(auditoryRepository).findAll();

        List<AuditoryDto> auditoryDtosActually = auditoryService.findAllDto();

        verify(auditoryRepository, times(1)).findAll();
        assertEquals(auditoryDtosExpected, auditoryDtosActually);
    }

    @Test
    void whenCreate_thenInvocCreateDaoClass() {
        AuditoryDto auditoryDto = AuditoryDtoModelRepository.getModel1();
        Auditory auditoryInput = AuditoryModelRepository.getModel1();
        Auditory auditoryExpected = AuditoryModelRepository.getModel1();
        AuditoryType auditoryTypeExpected = AuditoryTypeModelRepository.getModel1();

        doReturn(auditoryExpected).when(auditoryRepository).saveAndFlush(auditoryInput);
        doReturn(auditoryTypeExpected).when(auditoryTypeRepository).getOne(auditoryDto.getAuditoryTypeId());

        AuditoryDto auditoryDtoActually = auditoryService.create(auditoryDto);

        verify(auditoryRepository, times(1)).saveAndFlush(auditoryInput);
        verify(auditoryTypeRepository, times(1)).getOne(auditoryDto.getAuditoryTypeId());
        assertEquals(auditoryDto, auditoryDtoActually);
    }

    @Test
    void whenUpdate_thenInvocUpdateDaoClass() {
        AuditoryDto auditoryDto = AuditoryDtoModelRepository.getModel1();
        auditoryDto.setId(1);
        Auditory auditory = AuditoryModelRepository.getModel1();
        auditory.setId(1);
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();
        auditoryType.setId(1);

        doReturn(auditory).when(auditoryRepository).saveAndFlush(auditory);
        doReturn(true).when(auditoryRepository).existsById(auditoryDto.getId());
        doReturn(auditory).when(auditoryRepository).getOne(auditoryDto.getId());
        doReturn(auditoryType).when(auditoryTypeRepository).getOne(auditoryDto.getAuditoryTypeId());

        AuditoryDto groupDtoActually = auditoryService.update(auditoryDto);

        verify(auditoryRepository, times(1)).saveAndFlush(auditory);
        verify(auditoryRepository, times(1)).getOne(auditoryDto.getId());
        assertEquals(auditoryDto, groupDtoActually);
    }

    @Test
    void whenFindByAuditoryNumber_thenInvokeMethod() {
        Auditory auditory = AuditoryModelRepository.getModel1();
        auditory.setId(1);
        String expectedAuditoryNumber = auditory.getAuditoryNumber();
        doReturn(auditory).when(auditoryRepository).findByAuditoryNumber(expectedAuditoryNumber);

        Auditory actuallyGroup = auditoryService.findByAuditoryNumber(expectedAuditoryNumber);

        verify(auditoryRepository, times(1)).findByAuditoryNumber(expectedAuditoryNumber);
        assertEquals(expectedAuditoryNumber, actuallyGroup.getAuditoryNumber());
    }

    @Test
    void whenCreateRecordEntityWithId_thenThrowException() {
        AuditoryDto auditory = new AuditoryDto();
        auditory.setId(1);

        assertThatThrownBy(() -> auditoryService.create(auditory))
            .isInstanceOf(EntityAlreadyExistsException.class)
            .hasMessage("create() auditoryDto: %s", auditory);
    }

    @Test
    void whenCreateRecordWithNotValidEntity_thenThrowException() {
        AuditoryDto auditoryDto = AuditoryDtoModelRepository.getModel1();
        Auditory auditory = AuditoryModelRepository.getModel1();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();
        auditoryType.setId(1);

        doReturn(auditoryType).when(auditoryTypeRepository).getOne(auditoryDto.getAuditoryTypeId());
        doThrow(DataIntegrityViolationException.class).when(auditoryRepository).saveAndFlush(auditory);

        assertThatThrownBy(() -> auditoryService.create(auditoryDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("create() auditory: " + auditory);
    }

    @Test
    void whenUpdateRecordWithNotValidEntity_thenThrowException() {
        AuditoryDto auditoryDto = AuditoryDtoModelRepository.getModel1();
        Auditory auditory = AuditoryModelRepository.getModel1();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();
        auditoryType.setId(1);

        doReturn(auditoryType).when(auditoryTypeRepository).getOne(auditoryDto.getAuditoryTypeId());
        doReturn(true).when(auditoryRepository).existsById(auditory.getId());
        doThrow(DataIntegrityViolationException.class).when(auditoryRepository).saveAndFlush(auditory);

        assertThatThrownBy(() -> auditoryService.update(auditoryDto))
            .isInstanceOf(EntityNotValidException.class)
            .hasMessageContaining("update() auditory: " + auditory);
    }

    @Test
    void whenUpdateRecordNotFound_thenThrowException() {
        AuditoryDto auditoryDto = AuditoryDtoModelRepository.getModel1();
        Auditory auditory = AuditoryModelRepository.getModel1();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();
        auditoryType.setId(1);

        doReturn(auditoryType).when(auditoryTypeRepository).getOne(auditoryDto.getAuditoryTypeId());
        doReturn(false).when(auditoryRepository).existsById(auditory.getId());

        assertThatThrownBy(() -> auditoryService.update(auditoryDto))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Auditory not exist!");
    }
}
