package cl.aduana.ms_login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FuncionarioDTO extends UsuarioDTO {
    @NotBlank(message = "El organismo no puede quedar vacío")
    @Schema(
        description = "Institución/Organismo a la que pertenece el funcionario",
        example = "ADUANA",
        allowableValues = {"ADUANA", "PDI", "SAG"}
    )
    private String organismo;
}