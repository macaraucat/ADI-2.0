package cl.aduana.ms_risk.dto;

import java.time.LocalDate;
import cl.aduana.ms_risk.model.Inspeccion.EstadoInspeccion;
import lombok.Data;

@Data
public class InspeccionResponseDTO {
    private Long idInspeccion;
    private Long idTramite;
    private String idFuncionario;
    private LocalDate fecha;
    private String resultado;
    private String comentarios;
    private EstadoInspeccion estado;
}