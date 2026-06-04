package cl.aduana.ms_login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank(message = "El documento es obligatorio")
    @Size(min = 8, max = 10, message = "El documento debe tener entre 8 y 10 caracteres")
    @Schema(
        description = "Número de documento del usuario (RUN, DNI o Pasaporte)",
        example = "12345678-9",
        minLength = 8,
        maxLength = 10
    )
    private String nroDocumento;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 12, message = "La contraseña debe tener entre 8 y 12 caracteres")
    @Schema(
        description = "Contraseña del usuario",
        example = "MiClave123",
        minLength = 8,
        maxLength = 12
    )
    private String password;
}