package cl.aduana.ms_notification.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.aduana.ms_notification.model.Notificacion;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findAllByOrderByFechaEnvioDesc();
    
}