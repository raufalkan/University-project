package ua.com.foxminded.task.homework.rauf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import ua.com.foxminded.task.dao.AuditoryTypeRepository;
import ua.com.foxminded.task.domain.AuditoryType;
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;
import ua.com.foxminded.task.service.impl.AuditoryTypeServiceImpl;

@ExtendWith(MockitoExtension.class)
class AuditoryTypeServiceTest {

    @Mock
    private Logger logger;

    @Mock
    private AuditoryTypeRepository auditoryTypeRepository;

    private AuditoryTypeServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new AuditoryTypeServiceImpl(logger, auditoryTypeRepository);
    }

    @Test
    void findByIdDtoReturnsType() {
        when(auditoryTypeRepository.getOne(1)).thenReturn(auditoryType(1, "Lecture"));

        AuditoryTypeDto result = service.findByIdDto(1);

        assertEquals("Lecture", result.getType());
    }

    @ParameterizedTest
    @CsvSource({"1,Lecture", "2,Lab", "3,Seminar"})
    void findByIdDtoMatchesEachRow(int id, String type) {
        when(auditoryTypeRepository.getOne(id)).thenReturn(auditoryType(id, type));

        AuditoryTypeDto result = service.findByIdDto(id);

        assertEquals(id, result.getId());
        assertEquals(type, result.getType());
    }

    @Test
    void findAllDtoReturnsThreeItems() {
        List<AuditoryType> types = Arrays.asList(
                auditoryType(1, "Lecture"), auditoryType(2, "Lab"), auditoryType(3, "Seminar"));
        when(auditoryTypeRepository.findAll()).thenReturn(types);

        List<AuditoryTypeDto> result = service.findAllDto();

        assertEquals(3, result.size());
    }

    @Test
    void findByTypeUnknownReturnsNull() {
        when(auditoryTypeRepository.findByType("Gym")).thenReturn(null);

        assertNull(service.findByType("Gym"));
    }

    @Test
    void differentIdsGiveDifferentDtos() {
        when(auditoryTypeRepository.getOne(1)).thenReturn(auditoryType(1, "Lecture"));
        when(auditoryTypeRepository.getOne(2)).thenReturn(auditoryType(2, "Lab"));

        AuditoryTypeDto first = service.findByIdDto(1);
        AuditoryTypeDto second = service.findByIdDto(2);

        assertNotEquals(first, second);
    }

    private AuditoryType auditoryType(int id, String type) {
        AuditoryType auditoryType = new AuditoryType();
        auditoryType.setId(id);
        auditoryType.setType(type);
        return auditoryType;
    }
}