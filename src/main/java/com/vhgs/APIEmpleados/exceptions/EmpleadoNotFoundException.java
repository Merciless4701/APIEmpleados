package com.vhgs.APIEmpleados.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción custom para el API
 * 
 * @author VHGS
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EmpleadoNotFoundException extends RuntimeException {
	public EmpleadoNotFoundException(String message) {
        super(message);
    }
}
