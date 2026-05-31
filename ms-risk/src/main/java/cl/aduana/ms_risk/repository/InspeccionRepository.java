package cl.aduana.ms_risk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.aduana.ms_risk.model.Inspeccion;

public interface InspeccionRepository extends JpaRepository<Inspeccion, Long> {}