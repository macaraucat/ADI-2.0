package cl.aduana.ms_travel.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.aduana.ms_travel.model.TipoEstado;
import cl.aduana.ms_travel.model.Tramite;

public interface TramiteRepository extends JpaRepository<Tramite, Long> {
    Optional<Tramite> findAllByEstadoActualIn(List<TipoEstado> estados);
    List<Tramite> findByIdUsuarioAndIdVehiculo(Long idUsuario, Long idVehiculo);
    List<Tramite> findByIdUsuario(Long idUsuario);
    List<Tramite> findByIdVehiculo(Long idVehiculo);
}