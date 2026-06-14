package cl.aduana.ms_notification.Service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.aduana.common.client.UsuarioClient;
import cl.aduana.common.dto.request.NotificacionRequestDTO;
import cl.aduana.common.dto.response.NotificacionResponseDTO;
import cl.aduana.common.dto.response.UsuarioClientResponseDTO;
import cl.aduana.common.exception.BadRequestException;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_notification.model.Notificacion;
import cl.aduana.ms_notification.repository.NotificacionRepository;
import cl.aduana.ms_notification.service.NotificacionService;

@ExtendWith(MockitoExtension.class)
public class NotificacionServiceTest {

    @Mock
    private NotificacionRepository repository;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private NotificacionService notificacionService;

    @Test
    @DisplayName("Debe procesar notificación REGISTRO_EXITOSO correctamente")
    void procesarNotificacion_RegistroExitoso() {
        NotificacionRequestDTO request = NotificacionRequestDTO.builder()
                .idUsuario(1L)
                .tipoNotificacion("REGISTRO_EXITOSO")
                .detalle("Bienvenido")
                .build();

        UsuarioClientResponseDTO usuario = new UsuarioClientResponseDTO();
        usuario.setNombreCompleto("Juan Perez");
        usuario.setEmail("juan@correo.cl");

        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuario);
        when(repository.save(any(Notificacion.class))).thenAnswer(invocation -> {
            Notificacion entidad = invocation.getArgument(0);
            entidad.setIdNotificacion(10L);
            return entidad;
        });

        NotificacionResponseDTO response = notificacionService.procesarNotificacion(request);

        assertNotNull(response);
        assertEquals("10", response.getIdNotificacion());
        assertEquals("ENVIADO", response.getEstado());
    }

    @Test
    @DisplayName("Debe procesar notificación TRAMITE_CREADO con detalle personalizado")
    void procesarNotificacion_TramiteCreado() {
        NotificacionRequestDTO request = NotificacionRequestDTO.builder()
                .idUsuario(2L)
                .tipoNotificacion("TRAMITE_CREADO")
                .detalle("Por favor revise su bandeja")
                .build();

        UsuarioClientResponseDTO usuario = new UsuarioClientResponseDTO();
        usuario.setNombreCompleto("Laura Guzman");
        usuario.setEmail("laura@correo.cl");

        when(usuarioClient.obtenerUsuarioPorId(2L)).thenReturn(usuario);
        when(repository.save(any(Notificacion.class))).thenAnswer(invocation -> {
            Notificacion entidad = invocation.getArgument(0);
            entidad.setIdNotificacion(20L);
            return entidad;
        });

        NotificacionResponseDTO response = notificacionService.procesarNotificacion(request);

        assertNotNull(response);
        assertEquals("20", response.getIdNotificacion());
    }

    @Test
    @DisplayName("Debe procesar notificación desconocida usando detalle directo")
    void procesarNotificacion_TipoDesconocido() {
        NotificacionRequestDTO request = NotificacionRequestDTO.builder()
                .idUsuario(3L)
                .tipoNotificacion("OTRO")
                .detalle("Detalle directo")
                .build();

        UsuarioClientResponseDTO usuario = new UsuarioClientResponseDTO();
        usuario.setNombreCompleto("Pedro Perez");
        usuario.setEmail("pedro@correo.cl");

        when(usuarioClient.obtenerUsuarioPorId(3L)).thenReturn(usuario);
        when(repository.save(any(Notificacion.class))).thenAnswer(invocation -> {
            Notificacion entidad = invocation.getArgument(0);
            entidad.setIdNotificacion(30L);
            return entidad;
        });

        NotificacionResponseDTO response = notificacionService.procesarNotificacion(request);

        assertNotNull(response);
        assertEquals("30", response.getIdNotificacion());
    }

    @Test
    @DisplayName("Debe lanzar BadRequestException cuando faltan datos requeridos")
    void procesarNotificacion_BadRequest() {
        NotificacionRequestDTO request = NotificacionRequestDTO.builder()
                .idUsuario(null)
                .tipoNotificacion(null)
                .build();

        assertThrows(BadRequestException.class, () -> notificacionService.procesarNotificacion(request));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el usuario no existe")
    void procesarNotificacion_NoUsuario() {
        NotificacionRequestDTO request = NotificacionRequestDTO.builder()
                .idUsuario(4L)
                .tipoNotificacion("REGISTRO_EXITOSO")
                .detalle("Bienvenido")
                .build();

        when(usuarioClient.obtenerUsuarioPorId(4L)).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> notificacionService.procesarNotificacion(request));
    }

    @Test
    @DisplayName("Debe listar las notificaciones en orden descendente")
    void listar_DeberiaRetornarLista() {
        Notificacion notificacion = Notificacion.builder()
                .idNotificacion(1L)
                .usuarioId(1L)
                .tipoNotificacion("A")
                .mensaje("M1")
                .build();
        when(repository.findAllByOrderByFechaEnvioDesc()).thenReturn(List.of(notificacion));

        List<Notificacion> resultado = notificacionService.listar();
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getIdNotificacion());
    }
}
