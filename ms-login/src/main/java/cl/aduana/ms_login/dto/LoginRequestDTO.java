package cl.aduana.ms_login.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @NotBlank @Size(min = 8, max = 10)
    private String nroDocumento;

    @NotBlank @Size(min = 8, max = 12)
    private String password;
}