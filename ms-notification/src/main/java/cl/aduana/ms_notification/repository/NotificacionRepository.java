package cl.aduana.ms_notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.aduana.ms_notification.model.Notificacion;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findAllByOrderByFechaEnvioDesc();
    
}