package cl.aduana.ms_risk.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "patente_infraccion", schema = "bd_alertas")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatenteInfraccion {

    @Id
    private String patente;
}