package com.vhgs.APIEmpleados.audit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para consultar la Bitácora de eventos.
 * 
 * @author VHGS
 */
@Service
public class AuditService {
	private final AuditLogRepository auditLogRepository;

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
    
    public List<AuditLog> getLogs(){
    	return auditLogRepository.findAll();
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logEvent(String eventType, String entityType, String entityId, String action,
                         String result, String errorMessage, Map<String, Object> details) {
        AuditLog auditLog = new AuditLog();
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setEventType(eventType);
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setAction(action);
        auditLog.setResult(result);
        auditLog.setErrorMessage(errorMessage);
        auditLog.setDetails(details);
        
        auditLogRepository.save(auditLog);
    }

}
