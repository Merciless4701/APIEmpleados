package com.vhgs.APIEmpleados.audit;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repositorio para consultar la Bitácora de eventos.
 * 
 * @author VHGS
 */
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {

}
