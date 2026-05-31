package cl.aduana.ms_vehicle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VehiculoDTO {
    private Long idVehiculo;
    @NotBlank private String patente;
    @NotBlank private String paisRegistro;
    @NotBlank private String vin;
    @NotBlank private String marca;
    @NotBlank private String modelo;
}