package cl.aduana.ms_vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConductorAutorizadoDTO {
    private Long idConductor;
    @NotBlank private String nombre;
    @NotBlank private String nroDocumento;
    @NotNull private Long idAdmisionTemporal;
}