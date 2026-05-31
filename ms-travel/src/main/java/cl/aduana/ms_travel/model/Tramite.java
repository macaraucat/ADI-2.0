package cl.aduana.ms_travel.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tramite", schema = "bd_tramites")
@Builder @Data @AllArgsConstructor @NoArgsConstructor
public class Tramite {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long idTramite;
    private Long idUsuario;
    private Long idVehiculo;
    @Enumerated(EnumType.STRING) private TipoTramite tipoTramite;
    private LocalDateTime fechaDeIngreso;
    @Enumerated(EnumType.STRING) private TipoEstado estadoActual;
    
    @Builder.Default
    @OneToMany(mappedBy = "tramite", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pasajero> pasajeros = new ArrayList<>();
    
    @Builder.Default
    @OneToMany(mappedBy = "tramite", cascade = CascadeType.ALL, orphanRemoval = true) 
    private List<EstadoTramite> historialEstados = new ArrayList<>();
}