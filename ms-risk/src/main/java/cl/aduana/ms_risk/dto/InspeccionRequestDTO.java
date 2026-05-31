package cl.aduana.ms_risk.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InspeccionRequestDTO {
    @NotNull private Long idTramite;
    @NotNull private String idFuncionario;
    private LocalDate fecha;
    private String comentarios;
}