package ua.com.foxminded.task.validation.validator.property.unique.impl;

import java.util.Objects;

import ua.com.foxminded.task.domain.Auditory;
import ua.com.foxminded.task.service.AuditoryService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

public class AuditoryCommand implements Command {

    private AuditoryService auditoryService;

    public AuditoryCommand(AuditoryService auditoryService) {
        this.auditoryService = auditoryService;
    }

    @Override
    public boolean check(String fieldId, String fieldUnique) {
        boolean result = true;
        Auditory objectExist = auditoryService.findByAuditoryNumber(fieldUnique);
        if (!Objects.isNull(objectExist)) {
            result = (objectExist.getId() == Integer.valueOf(fieldId));
        }
        return result;
    }

}
