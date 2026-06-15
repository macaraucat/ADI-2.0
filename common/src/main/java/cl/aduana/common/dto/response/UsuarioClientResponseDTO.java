package cl.aduana.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class UsuarioClientResponseDTO {
    private String nombreCompleto;
    private String tipoDocumento;
    private String nroDocumento;
    private String email;
}