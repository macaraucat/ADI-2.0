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
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "reporte", schema = "bd_reportes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del reporte en el sistema", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Tipo de reporte generado.", example = "USUARIOS", allowableValues = {"USUARIOS", "VEHICULOS", "TRAMITES","ALERTAS"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoReporte tipo;

    @Schema(description = "Fecha y hora en que se generó el reporte", example = "2024-01-15T14:30:00", format = "date-time", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime fechaGeneracion;

    @Schema(description = "Cantidad total de registros incluidos en el reporte", example = "150", minimum = "0", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer totalRegistros;

    @Schema(description = "Identificación del funcionario o usuario que generó el reporte (E-mail, Nro. Documento o ID).", example = "juan.garcia@aduana.cl", requiredMode = Schema.RequiredMode.REQUIRED)
    private String generadoPor;

    @Schema(description = "Formato de salida del reporte.", example = "JSON", allowableValues = { "JSON", "CSV", "Excel", "PDF"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private String formato;
}