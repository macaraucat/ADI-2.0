package cl.aduana.ms_report.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.aduana.ms_report.model.Reporte;

public interface ReporteRepository extends JpaRepository<Reporte, Long> {}