package cl.aduana.common.dto.report;

import lombok.Data;

@Data
public class UsuarioReportDTO {
    private Long id;
    private String nroDocumento;
    private String nombreCompleto;
}