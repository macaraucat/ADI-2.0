package cl.aduana.ms_notification.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "notificacion", schema = "bd_notificaciones")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long idNotificacion;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "tipo_notificacion", nullable = false)
    private String tipoNotificacion;

    @Column(name = "mensaje", nullable = false)
    private String mensaje;

    @Column(name = "fecha_envio", nullable = false)
    private LocalDateTime fechaEnvio;

    @PrePersist
    protected void onCreate() {
        this.fechaEnvio = LocalDateTime.now();
    }
}