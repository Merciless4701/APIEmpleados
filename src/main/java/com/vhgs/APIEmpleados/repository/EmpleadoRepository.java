package com.vhgs.APIEmpleados.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.vhgs.APIEmpleados.entity.Empleado;

/**
 * Repository para acceder a la BD de MongoDB
 * 
 * @author VHGS
 */
public interface EmpleadoRepository extends MongoRepository<Empleado, String> {
}
