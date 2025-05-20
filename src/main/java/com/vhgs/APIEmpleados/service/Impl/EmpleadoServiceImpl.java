package com.vhgs.APIEmpleados.service.Impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vhgs.APIEmpleados.dto.EmpleadoRequest;
import com.vhgs.APIEmpleados.dto.EmpleadoResponse;
import com.vhgs.APIEmpleados.entity.Empleado;
import com.vhgs.APIEmpleados.exceptions.EmpleadoNotFoundException;
import com.vhgs.APIEmpleados.mapper.EmpleadoMapper;
import com.vhgs.APIEmpleados.repository.EmpleadoRepository;
import com.vhgs.APIEmpleados.service.EmpleadoService;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService{
	
	private static final Logger logger = LoggerFactory.getLogger(EmpleadoServiceImpl.class);
    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;
    private final Validator validator;

	@Override
	@Transactional(readOnly = true)
	public List<EmpleadoResponse> getAllEmpleados() {
		
		logger.info("obteniendo empleados");
        return empleadoRepository.findAll().stream()
                .map(empleadoMapper::toResponse)
                .toList();
	}

	@Override
	@Transactional
	public Optional<EmpleadoResponse> getEmpleadoById(String id) {
		logger.info("Fetching employee with ID: {}", id);
        return empleadoRepository.findById(id)
                .map(empleadoMapper::toResponse);
	}

	@Override
	@Transactional
	public List<EmpleadoResponse> createEmpleados(List<EmpleadoRequest> empleadoRequests) {
		return empleadoRequests.stream()
                .map(this::validateAndMapToEntity)
                .map(empleadoRepository::save)
                .map(empleadoMapper::toResponse)
                .toList();
	}

	@Override
	@Transactional
	public void deleteEmpleado(String id) {
		logger.info("tratando de eliminar empleado: "+id);
		if (!empleadoRepository.existsById(id)) {
            logger.warn(String.format("Empleado no encontrado!!", id));
            throw new EmpleadoNotFoundException(String.format("Empleado no encontrado!!",id));
        }
		empleadoRepository.deleteById(id);
        logger.info("Empleado Eliminado");
		
	}

	@Override
	@Transactional
	public EmpleadoResponse updateEmpleado(String id, EmpleadoRequest empleadoRequest) {
		logger.info("Actualizando empleado"+id);
	    Empleado existingEmployee = empleadoRepository.findById(id)
	            .orElseThrow(() -> {
	            	logger.warn(String.format("Empleado no encontrado!!", id));
	                throw new EmpleadoNotFoundException(String.format("Empleado no encontrado!!",id));
	            });

	    validateAndMapToEntity(empleadoRequest);

	    empleadoMapper.updateEntityFromRequest(empleadoRequest, existingEmployee);
	    Empleado updatedEmployee = empleadoRepository.save(existingEmployee);

	    return empleadoMapper.toResponse(updatedEmployee);
	}
	
    private Empleado validateAndMapToEntity(EmpleadoRequest request) {
        Set<ConstraintViolation<EmpleadoRequest>> violations = validator.validate(request);

        if (!violations.isEmpty()) {
            String errors = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Error de validación: " + errors);
        }

        Empleado empleado = empleadoMapper.toEntity(request);
        return empleado;
    }

}
