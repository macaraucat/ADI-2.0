package cl.aduana.ms_vehicle.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.aduana.ms_vehicle.model.Vehiculo;

public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    Optional<Vehiculo> findByPatente(String patente);
}