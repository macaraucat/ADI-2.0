package cl.aduana.ms_report.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity @Table(name="reporte", schema="bd_reportes")
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Reporte {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING) private TipoReporte tipo;
    private LocalDateTime fechaGeneracion;
    private Integer totalRegistros;
    private String generadoPor;
    private String formato;
}