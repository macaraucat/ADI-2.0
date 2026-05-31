package cl.aduana.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class VehiculoClientResponseDTO {
    private Long idVehiculo;
    private String patente;
    private String paisRegistro;
    private String marca;
    private String modelo;
}