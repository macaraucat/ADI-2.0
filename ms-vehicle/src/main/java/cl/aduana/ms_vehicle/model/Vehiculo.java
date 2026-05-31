package cl.aduana.ms_vehicle.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "vehiculos", schema = "bd_vehiculos")
@Data @NoArgsConstructor @AllArgsConstructor
public class Vehiculo {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVehiculo;
    private String patente;
    private String paisRegistro;
    private String vin;
    private String marca;
    private String modelo;
}