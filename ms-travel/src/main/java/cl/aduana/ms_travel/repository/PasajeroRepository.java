package cl.aduana.ms_travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import cl.aduana.ms_travel.model.Pasajero;

public interface PasajeroRepository extends JpaRepository<Pasajero, Long> {}