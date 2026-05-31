package cl.aduana.ms_risk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "alerta", schema = "bd_alertas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAlerta;
    private boolean resuelta;
    @ManyToOne
    @JoinColumn(name = "id_inspeccion")
    private Inspeccion inspeccion;
    @ManyToOne
    @JoinColumn(name = "id_criterio")
    private CriterioRiesgo criterioRiesgo;
}