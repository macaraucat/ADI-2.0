package cl.aduana.common.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        Server gatewayServer = new Server()
                .url("http://localhost:8080")
                .description("Gateway Servidor Principal");

        return new OpenAPI().servers(List.of(gatewayServer));
    }
}