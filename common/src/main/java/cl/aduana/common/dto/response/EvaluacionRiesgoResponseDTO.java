package cl.aduana.common.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
 public class EvaluacionRiesgoResponseDTO {
    private String nivelRiesgo;
    private String detalle;
}