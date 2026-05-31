package cl.aduana.ms_risk.model;

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
@Table(name = "criterio_riesgo", schema = "bd_alertas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CriterioRiesgo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCriterio;
    private String descripcion;
    @Enumerated(EnumType.STRING)
    private NivelAlerta nivelAlerta;

    public enum NivelAlerta {
        BAJO, MEDIO, ALTO
    }
}