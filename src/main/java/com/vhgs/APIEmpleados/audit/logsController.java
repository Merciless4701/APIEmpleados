package com.vhgs.APIEmpleados.audit;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

/**
 * Controller para consultar la Bitácora de eventos.
 * 
 * @author VHGS
 */
@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
public class logsController {
	
	private final AuditService auditService;
    
	@GetMapping("/logs")
    public ResponseEntity<List<AuditLog>> consultaLogs(){
    	List<AuditLog> logs = auditService.getLogs();
    	return ResponseEntity.ok(logs);
    }

}
