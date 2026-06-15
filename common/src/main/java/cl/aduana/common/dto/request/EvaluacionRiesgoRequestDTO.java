package cl.aduana.common.dto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class EvaluacionRiesgoRequestDTO {
    private Long idTramite;
    private String nroDocumentoSujeto;
    private String patente;
    private List<String> nroDocumentoAcompanantes;
}