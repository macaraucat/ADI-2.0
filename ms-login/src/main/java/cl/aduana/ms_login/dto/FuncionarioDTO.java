package cl.aduana.ms_login.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FuncionarioDTO extends UsuarioDTO {
    @NotBlank(message = "El organismo no puede quedar vacío")
    private String organismo; // ADUANA, PDI, SAG
}