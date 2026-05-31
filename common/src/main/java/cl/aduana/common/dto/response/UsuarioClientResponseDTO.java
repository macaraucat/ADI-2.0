package cl.aduana.common.dto.response;

import lombok.Data;

@Data
public class UsuarioClientResponseDTO {
    private String nombreCompleto;
    private String tipoDocumento;
    private String nroDocumento;
    private String email;
}