package cl.aduana.ms_risk.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PatchInspeccionDTO {
    @NotBlank private String resultado;
    private String comentarios;
}