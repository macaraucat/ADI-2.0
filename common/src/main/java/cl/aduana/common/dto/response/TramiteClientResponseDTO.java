package cl.aduana.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class TramiteClientResponseDTO {
    private Long idTramite;
    private String estadoGlobal;
}