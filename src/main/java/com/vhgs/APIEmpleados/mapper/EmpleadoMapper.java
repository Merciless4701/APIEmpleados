package com.vhgs.APIEmpleados.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.vhgs.APIEmpleados.dto.EmpleadoRequest;
import com.vhgs.APIEmpleados.dto.EmpleadoResponse;
import com.vhgs.APIEmpleados.entity.Empleado;

/**
 * Mapper para transformar las entidades en DTO's y viceversa
 * 
 * @author VHGS
 */
@Mapper(componentModel = "spring")
public interface EmpleadoMapper {
	/**
     * Entity to DTO
     * 
     * @return Lista de empleados
     */
	EmpleadoResponse toResponse(Empleado empleado);
	/**
     * List Entity to List DTO  
     * 
     * @return Lista de empleados
     */
	List<EmpleadoResponse> toResponseList(List<Empleado> empleado);
	/**
     * Entity to DTO  
     * 
     * @return Lista de empleados
     */
	Empleado toEntity(EmpleadoRequest empleadoRequest);
	/**
     * Función para actualizar una entidad dado un DTO y actualizar la BD 
     * 
     * @return Lista de empleados
     */
	@Mapping(target = "id", ignore = true)
    void updateEntityFromRequest(EmpleadoRequest empleadoRequest, @MappingTarget Empleado empleado);
}
