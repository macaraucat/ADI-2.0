package cl.aduana.ms_risk.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inspeccion", schema = "bd_alertas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Inspeccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long idInspeccion;

    private Long idTramite;
    private String idFuncionario;
    private LocalDate fecha;
    private String resultado;
    private String comentarios;

    @Enumerated(EnumType.STRING)
    private EstadoInspeccion estado;

    public enum EstadoInspeccion {
        INICIADA, FINALIZADA
    }
}