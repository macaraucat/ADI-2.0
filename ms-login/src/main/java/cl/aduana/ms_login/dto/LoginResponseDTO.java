package cl.aduana.ms_login.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private Long id;
    private String tipoDocumento;
    private String nroDocumento;
    private String nombreCompleto;
    private String email;
    private String organismo;
    private Set<String> roles;
}