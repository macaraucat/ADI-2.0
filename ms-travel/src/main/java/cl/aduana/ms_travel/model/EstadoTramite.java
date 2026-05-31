package cl.aduana.ms_travel.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "estado_tramite", schema = "bd_tramites")
@Builder @Data @AllArgsConstructor @NoArgsConstructor
public class EstadoTramite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tramite_estado_id")
    private Long idEstadoTramite;

    // a qué trámite pertenece este historial
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tramite", nullable = false)
    @ToString.Exclude
    private Tramite tramite;

    // qué estado se registró
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado", nullable = false)
    private Estado estado;

    @Column(name = "fecha_cambio")
    private LocalDateTime fechaCambio;

    @Column(name = "comentario")
    private String comentario;
}