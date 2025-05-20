package com.vhgs.APIEmpleados.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * DTO para los request de los empleados, se utilizan validation para garantizar la información
 * 
 * @author VHGS
 */
@Data
public class EmpleadoRequest {
    @NotBlank(message = "nombre requerido")
    private String primerNombre;
    private String segundoNombre;
    @NotBlank(message = "el apellido es requerido")
    private String primerApellido;
    private String segundoApellido;
    @NotNull(message = "la edad es requerida")
    private Integer edad;
    @NotBlank(message = "genero")
    @Pattern(regexp = "[HM]", message = "solo puede ser: 'H' o 'M'")
    private String sexo;
    @NotNull(message = "la fecha de nacimiento debe tener este formato: dd-MM-yyyy")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate fechaNacimiento;
    @NotBlank(message = "el puesto es requerido")
    private String puesto;
}