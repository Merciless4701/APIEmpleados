package com.vhgs.APIEmpleados;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vhgs.APIEmpleados.audit.AuditService;
import com.vhgs.APIEmpleados.controller.EmpleadoController;
import com.vhgs.APIEmpleados.dto.EmpleadoRequest;
import com.vhgs.APIEmpleados.dto.EmpleadoResponse;
import com.vhgs.APIEmpleados.exceptions.EmpleadoNotFoundException;
import com.vhgs.APIEmpleados.service.EmpleadoService;

import jakarta.servlet.http.HttpServletRequest;



@ExtendWith(MockitoExtension.class)
public class EmpleadoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmpleadoService empleadoService;

    @Mock
    private AuditService auditService;

    @Mock
    private HttpServletRequest request; 

    @InjectMocks
    private EmpleadoController empleadoController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        
        mockMvc = MockMvcBuilders.standaloneSetup(empleadoController).build();
        
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }


    private EmpleadoResponse createEmpleadoResponse(String id, String primerNombre, String primerApellido) {
        EmpleadoResponse response = new EmpleadoResponse();
        response.setId(new ObjectId(id)); 
        response.setPrimerNombre(primerNombre);
        response.setSegundoNombre("");
        response.setPrimerApellido(primerApellido);
        response.setSegundoApellido("");
        response.setEdad(30);
        response.setSexo("M");
        response.setFechaNacimiento(LocalDate.of(1994, 1, 1));
        response.setPuesto("Ingeniero");
        return response;
    }

    
    private EmpleadoRequest createEmpleadoRequest(String primerNombre, String primerApellido) {
        EmpleadoRequest request = new EmpleadoRequest();
        request.setPrimerNombre(primerNombre);
        request.setSegundoNombre("");
        request.setPrimerApellido(primerApellido);
        request.setSegundoApellido("");
        request.setEdad(30);
        request.setSexo("M");
        request.setFechaNacimiento(LocalDate.of(1994, 1, 1));
        request.setPuesto("Ingeniero");
        return request;
    }

    @Test
    @DisplayName("Debería retornar una lista de empleados al obtener todos")
    void getAllEmpleados_shouldReturnListOfEmpleados() throws Exception {
        
        List<EmpleadoResponse> empleados = Arrays.asList(
                createEmpleadoResponse("60c72b2f9b1d4c0001a1b2c3", "Juan", "Perez"),
                createEmpleadoResponse("60c72b2f9b1d4c0001a1b2c4", "Maria", "Gomez")
        );
        when(empleadoService.getAllEmpleados()).thenReturn(empleados);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/Empleados/allEmpleados")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(empleados.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].primerNombre").value("Juan"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].primerApellido").value("Gomez"));

        verify(empleadoService, times(1)).getAllEmpleados();
        verify(auditService, times(1)).logEvent(
                eq("EMPLOYEE_READ"), eq("Empleado"), eq(null), eq("Obtener todos los empleados"),
                eq("SUCCESS"), eq(null), (Map<String, Object>) any(Map.class)
        );
    }

    @Test
    @DisplayName("Debería retornar un empleado por ID")
    void getEmpleadoById_shouldReturnEmpleado() throws Exception {
        String empleadoId = "60c72b2f9b1d4c0001a1b2c3";
        EmpleadoResponse empleado = createEmpleadoResponse(empleadoId, "Juan", "Perez");
        when(empleadoService.getEmpleadoById(empleadoId)).thenReturn(Optional.of(empleado));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/Empleados/{id}", empleadoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.primerNombre").value("Juan"));

        verify(empleadoService, times(1)).getEmpleadoById(empleadoId);
        verify(auditService, times(1)).logEvent(
                eq("EMPLOYEE_READ"), eq("Empleado"), eq(empleadoId), eq("Obtener empleado por ID"),
                eq("SUCCESS"), eq(null), (Map<String, Object>) any(Map.class)
        );
    }

    @Test
    @DisplayName("Debería retornar 404 si el empleado no es encontrado por ID")
    void getEmpleadoById_shouldReturnNotFound() throws Exception {
        String empleadoId = "nonExistentId";
        when(empleadoService.getEmpleadoById(empleadoId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/Empleados/{id}", empleadoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empleadoService, times(1)).getEmpleadoById(empleadoId);
        verify(auditService, times(1)).logEvent(
                eq("EMPLOYEE_READ"), eq("Empleado"), eq(empleadoId), eq("Obtener empleado por ID"),
                eq("FAILURE"), eq("Empleado no encontrado"), (Map<String, Object>) any(Map.class)
        );
    }

    @Test
    @DisplayName("Debería crear nuevos empleados")
    void createEmpleados_shouldCreateNewEmpleados() throws Exception {
        List<EmpleadoRequest> requests = Arrays.asList(
                createEmpleadoRequest("Pedro", "Lopez"),
                createEmpleadoRequest("Ana", "Diaz")
        );
        List<EmpleadoResponse> responses = Arrays.asList(
                createEmpleadoResponse("60c72b2f9b1d4c0001a1b2c5", "Pedro", "Lopez"),
                createEmpleadoResponse("60c72b2f9b1d4c0001a1b2c6", "Ana", "Diaz")
        );

        when(empleadoService.createEmpleados(anyList())).thenReturn(responses);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/Empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(responses.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].primerNombre").value("Pedro"));

        verify(empleadoService, times(1)).createEmpleados(anyList());
        verify(auditService, times(1)).logEvent(
                eq("EMPLOYEE_CREATED"), eq("Empleado"), eq(null), eq("Crear varios empleados"),
                eq("SUCCESS"), eq(null), any(Map.class)
        );
    }

    @Test
    @DisplayName("Debería retornar 400 para entrada inválida al crear empleados")
    void createEmpleados_shouldReturnBadRequestForInvalidInput() throws Exception {
        EmpleadoRequest invalidRequest = createEmpleadoRequest(null, "Apellido");
        invalidRequest.setPrimerNombre(null); 
        List<EmpleadoRequest> requests = Collections.singletonList(invalidRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/Empleados")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest()); 

        verify(empleadoService, never()).createEmpleados(anyList());

        verify(auditService, never()).logEvent(
                eq("EMPLOYEE_CREATED"), eq("Empleado"), eq(null), eq("Fallo al crear varios empleados"),
                eq("FAILURE"), anyString(), any(Map.class)
        );
    }

    @Test
    @DisplayName("Debería eliminar un empleado")
    void deleteEmpleado_shouldDeleteEmpleado() throws Exception {
        String empleadoId = "60c72b2f9b1d4c0001a1b2c7";

        doNothing().when(empleadoService).deleteEmpleado(empleadoId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/Empleados/{id}", empleadoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent()); // 204 No Content

        verify(empleadoService, times(1)).deleteEmpleado(empleadoId);
        verify(auditService, times(1)).logEvent(
                eq("EMPLOYEE_DELETED"), eq("Empleado"), eq(empleadoId), eq("Eliminar empleado por ID"),
                eq("SUCCESS"), eq(null), any(Map.class)
        );
    }

    @Test
    @DisplayName("Debería retornar 404 si el empleado a eliminar no existe")
    void deleteEmpleado_shouldReturnNotFoundForNonExistingEmpleado() throws Exception {
        String empleadoId = "nonExistentId";
        
        doThrow(new EmpleadoNotFoundException("Empleado no encontrado")).when(empleadoService).deleteEmpleado(empleadoId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/Empleados/{id}", empleadoId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()); 

        verify(empleadoService, times(1)).deleteEmpleado(empleadoId);
        verify(auditService, times(1)).logEvent(
                eq("EMPLOYEE_DELETED"), eq("Empleado"), eq(empleadoId), eq("Fallo al eliminar empleado (no encontrado)"),
                eq("FAILURE"), anyString(), any(Map.class)
        );
    }

    @Test
    @DisplayName("Debería actualizar un empleado existente")
    void updateEmpleado_shouldUpdateEmpleado() throws Exception {
        String empleadoId = "60c72b2f9b1d4c0001a1b2c8";
        EmpleadoRequest requestBody = createEmpleadoRequest("Carlos", "Ruiz");
        EmpleadoResponse updatedResponse = createEmpleadoResponse(empleadoId, "Carlos", "Ruiz");

        when(empleadoService.updateEmpleado(eq(empleadoId), any(EmpleadoRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/Empleados/{id}", empleadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.primerNombre").value("Carlos"));

        verify(empleadoService, times(1)).updateEmpleado(eq(empleadoId), any(EmpleadoRequest.class));
        verify(auditService, times(1)).logEvent(
                eq("EMPLOYEE_UPDATED"), eq("Empleado"), eq(empleadoId), eq("Actualizar empleado"),
                eq("SUCCESS"), eq(null), any(Map.class)
        );
    }

    @Test
    @DisplayName("Debería retornar 404 si el empleado a actualizar no existe")
    void updateEmpleado_shouldReturnNotFoundForNonExistingEmpleado() throws Exception {
        String empleadoId = "nonExistentId";
        EmpleadoRequest requestBody = createEmpleadoRequest("Carlos", "Ruiz");

        doThrow(new EmpleadoNotFoundException("Empleado no encontrado")).when(empleadoService).updateEmpleado(eq(empleadoId), any(EmpleadoRequest.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/Empleados/{id}", empleadoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(empleadoService, times(1)).updateEmpleado(eq(empleadoId), any(EmpleadoRequest.class));
        verify(auditService, times(1)).logEvent(
                eq("EMPLOYEE_UPDATED"), eq("Empleado"), eq(empleadoId), eq("Fallo al actualizar empleado (no encontrado)"),
                eq("FAILURE"), anyString(), any(Map.class)
        );
    }
}