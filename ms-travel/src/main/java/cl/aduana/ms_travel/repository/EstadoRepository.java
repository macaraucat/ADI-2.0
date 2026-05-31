package cl.aduana.ms_travel.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cl.aduana.ms_travel.model.Estado;
import cl.aduana.ms_travel.model.TipoEstado;

@Repository
public interface EstadoRepository extends JpaRepository<Estado,Integer> {
    Optional<Estado> findByNombre(TipoEstado estado);  
}