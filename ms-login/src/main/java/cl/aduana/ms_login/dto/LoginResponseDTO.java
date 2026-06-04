package cl.aduana.ms_login.dto;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponseDTO {
    @Schema(description = "ID interno del usuario", example = "1")
    private Long id;
    
    @Schema(description = "Tipo de documento", example = "RUN")
    private String tipoDocumento;
    
    @Schema(description = "Número de documento", example = "12345678-9")
    private String nroDocumento;
    
    @Schema(description = "Nombre completo del usuario", example = "Juan García López")
    private String nombreCompleto;
    
    @Schema(description = "Correo electrónico", example = "juan.garcia@correo.cl", format = "email")
    private String email;
    
    @Schema(description = "Organismo al que pertenece", example = "ADUANA")
    private String organismo;
    
    @Schema(
        description = "Conjunto de roles asignados al usuario",
        example = "[\"AGENTE_ADUANA\", \"USUARIO_GESTIONAR\"]"
    )
    private Set<String> roles;
}