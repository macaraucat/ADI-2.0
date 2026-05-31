package cl.aduana.ms_login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioDTO {
    @NotBlank(message = "El tipo de documento no puede estar vacío")
    private String tipoDocumento;
    
    @NotBlank(message = "El número de documento no puede estar vacío")
    @Size(min = 8, max = 10)
    private String nroDocumento;
    
    @NotBlank(message = "El nombre completo no puede estar vacío")
    private String nombreCompleto;
    
    @NotBlank @Email
    private String email;
    
    @NotBlank @Size(min = 8, max = 12)
    private String password;
}