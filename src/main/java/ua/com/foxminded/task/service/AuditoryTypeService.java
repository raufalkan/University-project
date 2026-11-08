package ua.com.foxminded.task.service;

import ua.com.foxminded.task.domain.AuditoryType;
import ua.com.foxminded.task.domain.dto.AuditoryTypeDto;

public interface AuditoryTypeService extends ModelService<AuditoryTypeDto> {

    public AuditoryType findByType(String type);

}
