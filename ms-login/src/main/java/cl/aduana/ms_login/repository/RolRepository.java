package cl.aduana.ms_login.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.aduana.ms_login.model.Rol;

public interface RolRepository extends JpaRepository<Rol, Long>{
    Optional<Rol> findByCodigo(String codigo);
}