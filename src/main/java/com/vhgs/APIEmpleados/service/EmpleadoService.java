package com.vhgs.APIEmpleados.service;

import java.util.List;
import java.util.Optional;

import com.vhgs.APIEmpleados.dto.EmpleadoRequest;
import com.vhgs.APIEmpleados.dto.EmpleadoResponse;


public interface EmpleadoService {
    List<EmpleadoResponse> getAllEmpleados();
    Optional<EmpleadoResponse> getEmpleadoById(String id);
    List<EmpleadoResponse> createEmpleados(List<EmpleadoRequest> EmpleadoRequests);
    void deleteEmpleado(String id);
    EmpleadoResponse updateEmpleado(String id, EmpleadoRequest EmpleadoRequest);
}
