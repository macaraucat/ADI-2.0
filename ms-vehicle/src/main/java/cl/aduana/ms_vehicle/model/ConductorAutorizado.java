package cl.aduana.ms_vehicle.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "conductores_autorizados", schema = "bd_vehiculos")
@Data @NoArgsConstructor @AllArgsConstructor
public class ConductorAutorizado {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConductor;
    private String nombre;
    private String nroDocumento;

    @ManyToOne
    @JoinColumn(name = "id_admision_temporal")
    private AdmisionTemporal admisionTemporal;
}