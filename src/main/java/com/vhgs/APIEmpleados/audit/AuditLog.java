package com.vhgs.APIEmpleados.audit;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * Entidad de la consultar la Bitácora de eventos.
 * 
 * @author VHGS
 */
@Data
@Document(collection = "auditLog")
public class AuditLog {
	@Id
    private String id;
    private LocalDateTime timestamp;
    private String eventType; // Tipo de acción (e.g., "EMPLOYEE_CREATED", "LOGIN_FAILED")
    private String entityType; // Tipo de entidad afectada (e.g., "Empleado")
    private String entityId; // ID de la entidad afectada (si aplica)
    private String action; // Descripción de la acción (e.g., "Crear Empleado", "Actualizar Puesto")
    private String result; // Resultado (e.g., "SUCCESS", "FAILURE")
    private String errorMessage; // Mensaje de error (si aplica)
    private Map<String, Object> details; 

}
