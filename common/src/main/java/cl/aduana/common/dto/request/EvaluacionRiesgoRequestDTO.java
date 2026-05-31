package cl.aduana.common.dto.request;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class EvaluacionRiesgoRequestDTO {
    private Long idTramite;
    private String nroDocumentoSujeto;
    private String patente;
    private List<String> nroDocumentoAcompanantes;
}