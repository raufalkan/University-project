package ua.com.foxminded.task.domain.repository.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.AuditoryType;
import ua.com.foxminded.task.domain.dto.AuditoryDto;
import ua.com.foxminded.task.domain.repository.AuditoryTypeModelRepository;

public class AuditoryDtoModelRepository {

    private AuditoryDtoModelRepository() {
    }

    public static List<AuditoryDto> getModels() {
        List<AuditoryDto> auditories = Arrays.asList(getModel1(), getModel2(), getModel3(), getModel4(), getModel5(), getModel6());
        return new ArrayList<>(auditories);
    }

    public static AuditoryDto getModel1() {
        AuditoryDto auditory = new AuditoryDto();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();
        auditoryType.setId(1);
        auditory.setAuditoryTypeTitle(auditoryType.getType());
        auditory.setAuditoryTypeId(auditoryType.getId());
        auditory.setAuditoryNumber("101a");
        auditory.setMaxCapacity(100);
        auditory.setDescription("bla bla bla 1");
        return auditory;
    }

    public static AuditoryDto getModel2() {
        AuditoryDto auditory = new AuditoryDto();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel2();
        auditoryType.setId(2);
        auditory.setAuditoryTypeTitle(auditoryType.getType());
        auditory.setAuditoryTypeId(auditoryType.getId());
        auditory.setAuditoryNumber("102a");
        auditory.setMaxCapacity(50);
        auditory.setDescription("bla bla bla 2");
        return auditory;
    }

    public static AuditoryDto getModel3() {
        AuditoryDto auditory = new AuditoryDto();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel3();
        auditoryType.setId(3);
        auditory.setAuditoryTypeTitle(auditoryType.getType());
        auditory.setAuditoryTypeId(auditoryType.getId());
        auditory.setAuditoryNumber("201a");
        auditory.setMaxCapacity(10);
        auditory.setDescription("bla bla bla 3");
        return auditory;
    }

    public static AuditoryDto getModel4() {
        AuditoryDto auditory = new AuditoryDto();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel4();
        auditoryType.setId(4);
        auditory.setAuditoryTypeTitle(auditoryType.getType());
        auditory.setAuditoryTypeId(auditoryType.getId());
        auditory.setAuditoryNumber("103a");
        auditory.setMaxCapacity(30);
        auditory.setDescription("bla bla bla 4");
        return auditory;
    }

    public static AuditoryDto getModel5() {
        AuditoryDto auditory = new AuditoryDto();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();
        auditoryType.setId(1);
        auditory.setAuditoryTypeTitle(auditoryType.getType());
        auditory.setAuditoryTypeId(auditoryType.getId());
        auditory.setAuditoryNumber("405a");
        auditory.setMaxCapacity(14);
        auditory.setDescription("bla bla bla 5");
        return auditory;
    }

    public static AuditoryDto getModel6() {
        AuditoryDto auditory = new AuditoryDto();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel2();
        auditoryType.setId(2);
        auditory.setAuditoryTypeTitle(auditoryType.getType());
        auditory.setAuditoryTypeId(auditoryType.getId());
        auditory.setAuditoryNumber("161a");
        auditory.setMaxCapacity(20);
        auditory.setDescription("bla bla bla 6");
        return auditory;
    }

}
