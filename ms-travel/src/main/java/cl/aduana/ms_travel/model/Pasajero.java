package cl.aduana.ms_travel.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@Entity
@Table(name = "pasajero", schema = "bd_tramites")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Pasajero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pasajero")
    private Long idPasajero;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "nro_documento")
    private String nroDocumento;

    @Column(name = "nacionalidad")
    private String nacionalidad;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    // Muchos pasajeros pertenecen a UN trámite
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tramite")
    @ToString.Exclude
    private Tramite tramite;
}