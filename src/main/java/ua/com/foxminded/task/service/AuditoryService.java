package ua.com.foxminded.task.service;

import ua.com.foxminded.task.domain.Auditory;
import ua.com.foxminded.task.domain.dto.AuditoryDto;

public interface AuditoryService extends ModelService<AuditoryDto> {
    
    public Auditory findByAuditoryNumber(String auditoryNumber);
}
