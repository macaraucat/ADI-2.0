package cl.aduana.ms_risk.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlertaDTO {
    private Long idAlerta;
    private boolean resuelta;
    @NotNull private Long idInspeccion;
    @NotNull private Long idCriterio;
}