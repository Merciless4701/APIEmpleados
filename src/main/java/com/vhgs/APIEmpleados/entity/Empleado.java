package com.vhgs.APIEmpleados.entity;

import java.time.LocalDate;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * Entidad del empleado
 * 
 * @author VHGS
 */
@Document(collection = "empleados")
@Data
public class Empleado {
    @Id
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