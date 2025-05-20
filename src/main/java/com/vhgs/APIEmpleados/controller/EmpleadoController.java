package com.vhgs.APIEmpleados.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vhgs.APIEmpleados.audit.AuditService;
import com.vhgs.APIEmpleados.dto.EmpleadoRequest;
import com.vhgs.APIEmpleados.dto.EmpleadoResponse;
import com.vhgs.APIEmpleados.exceptions.EmpleadoNotFoundException;
import com.vhgs.APIEmpleados.service.EmpleadoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



/**
 * Controller de empleados.
 * 
 * @author VHGS
 */

@RestController
@RequestMapping("/api/Empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private static final Logger logger = LoggerFactory.getLogger(EmpleadoController.class);
    private final EmpleadoService empleadoService;
    private final AuditService auditService;

    
    /**
     * Obtiene todos los Empleados de la BD.
     * 
     * @return Lista de empleados
     */
    @GetMapping("/allEmpleados")
    public ResponseEntity<List<EmpleadoResponse>> getAllEmpleados() {
        List<EmpleadoResponse> Empleados = empleadoService.getAllEmpleados();
        auditService.logEvent("EMPLOYEE_READ", "Empleado", null, "Obtener todos los empleados", "SUCCESS", null, Map.of("count", Empleados.size()));
        return ResponseEntity.ok(Empleados);
    }

    /**
     * Obtiene un empleado específico dado el Id de MongoDB
     * 
     * @return EmpleadoResponse
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoResponse> getEmpleadoById(@PathVariable String id) {
        return empleadoService.getEmpleadoById(id)
                .map(empleado -> {
                    auditService.logEvent("EMPLOYEE_READ", "Empleado", id, "Obtener empleado por ID", "SUCCESS", null, Collections.emptyMap());
                    return ResponseEntity.ok(empleado);
                })
                .orElseGet(() -> {
                    auditService.logEvent("EMPLOYEE_READ", "Empleado", id, "Obtener empleado por ID", "FAILURE", "Empleado no encontrado", Collections.emptyMap());
                    return ResponseEntity.notFound().build();
                });
    }
    
    
    /**
     * Crea un empleado validando la mayoría de los datos 
     * 
     * @return EmpleadoResponse
     */
    @PostMapping
    public ResponseEntity<List<EmpleadoResponse>> createEmpleados(@Valid @RequestBody List<EmpleadoRequest> empleadoRequest) {
        List<EmpleadoResponse> createdEmpleados;
        String result = "SUCCESS";
        String errorMessage = null;
        try {
            createdEmpleados = empleadoService.createEmpleados(empleadoRequest);
            auditService.logEvent("EMPLOYEE_CREATED", "Empleado", null,
                                 "Crear varios empleados", result, errorMessage,
                                 Map.of("count", createdEmpleados.size(), "employeeIds", createdEmpleados.stream().map(EmpleadoResponse::getId).toList()));
            return new ResponseEntity<>(createdEmpleados, HttpStatus.CREATED);
        } catch (Exception e) {
            result = "FAILURE";
            errorMessage = e.getMessage();
            auditService.logEvent("EMPLOYEE_CREATED", "Empleado", null,
                                 "Fallo al crear varios empleados", result, errorMessage,
                                 Map.of("requestBodySize", empleadoRequest.size()));
            throw e; // Relanza la excepción para que Spring la maneje
        }
    }

    /**
     * Elimina de la BD un empleado específico dado el Id de MongoDB
     * 
     * @return message
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmpleado(@PathVariable String id) {
        String result = "SUCCESS";
        String errorMessage = null;
        try {
            empleadoService.deleteEmpleado(id);
            auditService.logEvent("EMPLOYEE_DELETED", "Empleado", id,
                                 "Eliminar empleado por ID", result, errorMessage, Collections.emptyMap());
            return ResponseEntity.noContent().build();
        } catch (EmpleadoNotFoundException e) {
            result = "FAILURE";
            errorMessage = e.getMessage();
            auditService.logEvent("EMPLOYEE_DELETED", "Empleado", id,
                                 "Fallo al eliminar empleado (no encontrado)", result, errorMessage, Collections.emptyMap());
            throw e; 
        } catch (Exception e) {
            result = "FAILURE";
            errorMessage = e.getMessage();
            auditService.logEvent("EMPLOYEE_DELETED", "Empleado", id,
                                 "Fallo al eliminar empleado", result, errorMessage, Collections.emptyMap());
            throw e; 
        }
    }

    /**
     * Modifica de la BD un empleado específico dado el Id de MongoDB
     * 
     * @return EmpleadoResponse
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoResponse> updateEmpleado(@PathVariable String id, @Valid @RequestBody EmpleadoRequest empleadoRequest) {
        String result = "SUCCESS";
        String errorMessage = null;
        EmpleadoResponse updatedEmpleado;
        try {
            updatedEmpleado = empleadoService.updateEmpleado(id, empleadoRequest);
             auditService.logEvent("EMPLOYEE_UPDATED", "Empleado", id,
                                         "Actualizar empleado", result, errorMessage,
                                         Map.of("requestData", empleadoRequest));
            return ResponseEntity.ok(updatedEmpleado);
        } catch (EmpleadoNotFoundException e) {
            result = "FAILURE";
            errorMessage = e.getMessage();
             auditService.logEvent("EMPLOYEE_UPDATED", "Empleado", id,
                                                 "Fallo al actualizar empleado (no encontrado)", result, errorMessage, Collections.emptyMap());
            throw e;
        }  catch (Exception e) {
            result = "FAILURE";
            errorMessage = e.getMessage();
             auditService.logEvent("EMPLOYEE_UPDATED", "Empleado", id,
                                                 "Fallo al actualizar empleado", result, errorMessage, Collections.emptyMap());
            throw e;
        }
    }


}