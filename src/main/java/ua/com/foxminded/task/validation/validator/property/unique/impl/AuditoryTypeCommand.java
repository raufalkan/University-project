package ua.com.foxminded.task.validation.validator.property.unique.impl;

import java.util.Objects;

import ua.com.foxminded.task.domain.AuditoryType;
import ua.com.foxminded.task.service.AuditoryTypeService;
import ua.com.foxminded.task.validation.validator.property.unique.Command;

public class AuditoryTypeCommand implements Command {

    private AuditoryTypeService auditoryTypeService;

    public AuditoryTypeCommand(AuditoryTypeService auditoryTypeService) {
        this.auditoryTypeService = auditoryTypeService;
    }

    @Override
    public boolean check(String fieldId, String fieldUnique) {
        boolean result = true;
        AuditoryType objectExist = auditoryTypeService.findByType(fieldUnique);
        if (!Objects.isNull(objectExist)) {
            result = (objectExist.getId() == Integer.valueOf(fieldId));
        }
        return result;
    }

}
