package cl.aduana.ms_notification.service;

import java.util.List;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import cl.aduana.common.client.UsuarioClient;
import cl.aduana.common.dto.request.NotificacionRequestDTO;
import cl.aduana.common.dto.response.NotificacionResponseDTO;
import cl.aduana.common.dto.response.UsuarioClientResponseDTO;
import cl.aduana.common.exception.BadRequestException;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_notification.model.Notificacion;
import cl.aduana.ms_notification.repository.NotificacionRepository;

@Service @RequiredArgsConstructor @Slf4j
public class NotificacionService {

    private final NotificacionRepository repository;
    private final UsuarioClient usuarioClient;

    public NotificacionResponseDTO procesarNotificacion(NotificacionRequestDTO request) {
        log.info("Procesando notificación usuarioId={}", request.getIdUsuario());
        
        if (request.getIdUsuario() == null || request.getTipoNotificacion() == null) {
            throw new BadRequestException("idUsuario y tipoNotificacion son obligatorios");
        }

        UsuarioClientResponseDTO usuario = usuarioClient.obtenerUsuarioPorId(request.getIdUsuario());
        if (usuario == null) {
            throw new ResourceNotFoundException("Usuario no encontrado: " + request.getIdUsuario());
        }

        String mensaje = switch (request.getTipoNotificacion().toUpperCase()) {
            case "REGISTRO_EXITOSO" -> 
                String.format("¡Hola %s! Tu cuenta ha sido creada exitosamente.", usuario.getNombreCompleto());
            case "TRAMITE_CREADO" -> 
                String.format("Estimado/a %s, su trámite ha sido ingresado. %s", 
                              usuario.getNombreCompleto(), request.getDetalle());
            default -> "Notificación: " + request.getDetalle();
        };

        log.info("[EMAIL SIMULADO] to={} | asunto={} | {}", usuario.getEmail(), request.getTipoNotificacion(), mensaje);

        Notificacion guardada = repository.save(Notificacion.builder()
            .usuarioId(request.getIdUsuario())
            .tipoNotificacion(request.getTipoNotificacion().toUpperCase())
            .mensaje(mensaje)
            .build());

        NotificacionResponseDTO resp = new NotificacionResponseDTO();
        resp.setIdNotificacion(String.valueOf(guardada.getIdNotificacion()));
        resp.setEstado("ENVIADO");
        return resp;
    }

    public List<Notificacion> listar() {
        return repository.findAllByOrderByFechaEnvioDesc();
    }
}