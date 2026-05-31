package cl.aduana.ms_notification.controller;

import java.util.List;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import cl.aduana.common.dto.request.NotificacionRequestDTO;
import cl.aduana.common.dto.response.NotificacionResponseDTO;
import cl.aduana.ms_notification.model.Notificacion;
import cl.aduana.ms_notification.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/notificaciones")
@RequiredArgsConstructor @Slf4j
public class NotificacionController {

    private final NotificacionService service;

    @PostMapping("/enviar")
    public ResponseEntity<NotificacionResponseDTO> enviar(@Valid @RequestBody NotificacionRequestDTO request) {
        log.info("POST /enviar - tipo={}", request.getTipoNotificacion());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.procesarNotificacion(request));
    }

    @GetMapping("")
    public ResponseEntity<List<Notificacion>> historial() {
        log.info("GET /notificaciones - historial");
        return ResponseEntity.ok(service.listar());
    }
}