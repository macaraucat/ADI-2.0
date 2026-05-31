package cl.aduana.ms_login.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.aduana.ms_login.model.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Long>{
    Optional<Permiso> findByCodigo(String codigo);
}