package com.vhgs.APIEmpleados.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


/**
 * Configuración para implementar de Swagger.
 * 
 * @author VHGS
 */
@Configuration
public class ApiConfig {

    @Bean
    public OpenAPI empleadoAPI() {
        return new OpenAPI()
                .info(new Info().title("Empleado API")
                        .description("API")
                        .version("v1.0"));
    }
}
