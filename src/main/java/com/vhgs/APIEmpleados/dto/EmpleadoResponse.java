package com.vhgs.APIEmpleados.dto;

import java.time.LocalDate;

import org.bson.types.ObjectId;

import lombok.Data;

/**
 * DTO del response del usuario
 * 
 * @author VHGS
 */
@Data
public class EmpleadoResponse {
    private ObjectId id;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private Integer edad;
    private String sexo;
    private LocalDate fechaNacimiento;
    private String puesto;
}