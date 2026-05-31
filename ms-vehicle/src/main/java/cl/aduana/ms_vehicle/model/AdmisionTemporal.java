package cl.aduana.ms_vehicle.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "admisiones", schema = "bd_vehiculos")
@Data @NoArgsConstructor @AllArgsConstructor
public class AdmisionTemporal {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAdmisionTemporal;
    private LocalDate fechaSalida;
    private LocalDate fechaVencimiento;
    private String pasoSalida;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_vehiculo")
    private Vehiculo vehiculo;
}