package cl.aduana.ms_risk.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.aduana.ms_risk.model.PatenteInfraccion;

public interface PatenteInfraccionRepository extends JpaRepository<PatenteInfraccion, String> {}