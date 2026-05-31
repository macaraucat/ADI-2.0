package cl.aduana.ms_travel.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "estado", schema = "bd_tramites")
@Data @NoArgsConstructor @AllArgsConstructor
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Integer idEstado;

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre_estado", nullable = false, unique = true)
    private TipoEstado nombre; // INGRESADO, APROBADO, etc.

    @Column(name = "descripcion")
    private String descripcion;
}