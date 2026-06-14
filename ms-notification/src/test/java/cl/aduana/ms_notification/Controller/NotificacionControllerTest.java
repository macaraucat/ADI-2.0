package cl.aduana.ms_notification.Controller;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import cl.aduana.common.dto.request.NotificacionRequestDTO;
import cl.aduana.common.dto.response.NotificacionResponseDTO;
import cl.aduana.ms_notification.controller.NotificacionController;
import cl.aduana.ms_notification.model.Notificacion;
import cl.aduana.ms_notification.service.NotificacionService;

@ExtendWith(MockitoExtension.class)
class NotificacionControllerTest {

    @Mock
    private NotificacionService service;

    @InjectMocks
    private NotificacionController controller;

    @Test
    @DisplayName("POST /api/v1/notificaciones/enviar debe devolver CREATED y datos de respuesta")
    void enviarNotificacion_DeberiaRetornarCreated() {
        NotificacionRequestDTO request = NotificacionRequestDTO.builder()
                .idUsuario(1L)
                .tipoNotificacion("REGISTRO_EXITOSO")
                .detalle("Bienvenido")
                .build();

        NotificacionResponseDTO response = new NotificacionResponseDTO();
        response.setIdNotificacion("10");
        response.setEstado("ENVIADO");
        when(service.procesarNotificacion(any(NotificacionRequestDTO.class))).thenReturn(response);

        ResponseEntity<NotificacionResponseDTO> result = controller.enviar(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals("10", result.getBody().getIdNotificacion());
        assertEquals("ENVIADO", result.getBody().getEstado());
    }

    @Test
    @DisplayName("GET /api/v1/notificaciones debe devolver historial de notificaciones")
    void historial_DeberiaRetornarLista() {
        Notificacion notificacion = Notificacion.builder()
                .idNotificacion(1L)
                .usuarioId(1L)
                .tipoNotificacion("REGISTRO_EXITOSO")
                .mensaje("Mensaje")
                .build();
        when(service.listar()).thenReturn(List.of(notificacion));

        ResponseEntity<List<Notificacion>> result = controller.historial();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        assertEquals(1L, result.getBody().get(0).getIdNotificacion());
    }
}