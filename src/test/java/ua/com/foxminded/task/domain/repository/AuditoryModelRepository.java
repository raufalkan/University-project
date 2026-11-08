package ua.com.foxminded.task.domain.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ua.com.foxminded.task.domain.Auditory;
import ua.com.foxminded.task.domain.AuditoryType;

public class AuditoryModelRepository {

    private AuditoryModelRepository() {
    }

    public static List<Auditory> getModels() {
        List<Auditory> auditories = Arrays.asList(getModel1(), getModel2(), getModel3(), getModel4(), getModel5(), getModel6());
        return new ArrayList<>(auditories);
    }

    public static Auditory getModel1() {
        Auditory auditory = new Auditory();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();
        auditoryType.setId(1);
        auditory.setType(auditoryType);
        auditory.setAuditoryNumber("101a");
        auditory.setMaxCapacity(100);
        auditory.setDescription("bla bla bla 1");
        return auditory;
    }

    public static Auditory getModel2() {
        Auditory auditory = new Auditory();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel2();
        auditoryType.setId(2);
        auditory.setType(auditoryType);
        auditory.setAuditoryNumber("102a");
        auditory.setMaxCapacity(50);
        auditory.setDescription("bla bla bla 2");
        return auditory;
    }

    public static Auditory getModel3() {
        Auditory auditory = new Auditory();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel3();
        auditoryType.setId(3);
        auditory.setType(auditoryType);
        auditory.setAuditoryNumber("201a");
        auditory.setMaxCapacity(10);
        auditory.setDescription("bla bla bla 3");
        return auditory;
    }

    public static Auditory getModel4() {
        Auditory auditory = new Auditory();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel4();
        auditoryType.setId(4);
        auditory.setType(auditoryType);
        auditory.setAuditoryNumber("103a");
        auditory.setMaxCapacity(30);
        auditory.setDescription("bla bla bla 4");
        return auditory;
    }

    public static Auditory getModel5() {
        Auditory auditory = new Auditory();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel1();
        auditoryType.setId(1);
        auditory.setType(auditoryType);
        auditory.setAuditoryNumber("405a");
        auditory.setMaxCapacity(14);
        auditory.setDescription("bla bla bla 5");
        return auditory;
    }

    public static Auditory getModel6() {
        Auditory auditory = new Auditory();
        AuditoryType auditoryType = AuditoryTypeModelRepository.getModel2();
        auditoryType.setId(2);
        auditory.setType(auditoryType);
        auditory.setAuditoryNumber("161a");
        auditory.setMaxCapacity(20);
        auditory.setDescription("bla bla bla 6");
        return auditory;
    }

}
