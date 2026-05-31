package cl.aduana.ms_vehicle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.aduana.ms_vehicle.model.ConductorAutorizado;

public interface ConductorAutorizadoRepository extends JpaRepository<ConductorAutorizado, Long> {}