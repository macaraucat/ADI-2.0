package cl.aduana.ms_vehicle.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.aduana.ms_vehicle.model.AdmisionTemporal;

public interface AdmisionTemporalRepository extends JpaRepository<AdmisionTemporal, Long> {}