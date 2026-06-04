package cl.aduana.ms_login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {
    @NotBlank(message = "El tipo de documento no puede estar vacío")
    @Schema(
        description = "Tipo de documento de identificación",
        example = "RUN",
        allowableValues = {"RUN", "DNI", "PASAPORTE"}
    )
    private String tipoDocumento;
    
    @NotBlank(message = "El número de documento no puede estar vacío")
    @Size(min = 8, max = 10, message = "El documento debe tener entre 8 y 10 caracteres")
    @Schema(
        description = "Número de documento único",
        example = "12345678-9",
        minLength = 8,
        maxLength = 10
    )
    private String nroDocumento;
    
    @NotBlank(message = "El nombre completo no puede estar vacío")
    @Schema(
        description = "Nombre completo del usuario (nombre, apellido paterno, apellido materno)",
        example = "Ana García López",
        minLength = 5
    )
    private String nombreCompleto;
    
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Schema(
        description = "Correo electrónico único del usuario",
        example = "ana@correo.cl",
        format = "email"
    )
    private String email;
    
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 8, max = 12, message = "La contraseña debe tener entre 8 y 12 caracteres")
    @Schema(
        description = "Contraseña segura (8-12 caracteres)",
        example = "MiClave123",
        minLength = 8,
        maxLength = 12
    )
    private String password;
}