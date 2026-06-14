package cl.aduana.ms_travel.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.aduana.common.client.NotificacionClient;
import cl.aduana.common.client.RiskClient;
import cl.aduana.common.client.UsuarioClient;
import cl.aduana.common.client.VehiculoClient;
import cl.aduana.common.dto.request.EvaluacionRiesgoRequestDTO;
import cl.aduana.common.dto.request.NotificacionRequestDTO;
import cl.aduana.common.dto.response.EvaluacionRiesgoResponseDTO;
import cl.aduana.common.dto.response.NotificacionResponseDTO;
import cl.aduana.common.dto.response.UsuarioClientResponseDTO;
import cl.aduana.common.dto.response.VehiculoClientResponseDTO;
import cl.aduana.common.exception.BadRequestException;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_travel.dto.estado.EstadoTramiteMapper;
import cl.aduana.ms_travel.dto.estado.EstadoTramiteResponseDTO;
import cl.aduana.ms_travel.dto.pasajero.PasajeroMapper;
import cl.aduana.ms_travel.dto.pasajero.PasajeroRequestDTO;
import cl.aduana.ms_travel.dto.pasajero.PasajeroResponseDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteMapper;
import cl.aduana.ms_travel.dto.tramite.TramiteRequestDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteRequestUpdateDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteResponseDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteResumenResponseDTO;
import cl.aduana.ms_travel.model.EstadoTramite;
import cl.aduana.ms_travel.model.Pasajero;
import cl.aduana.ms_travel.model.TipoEstado;
import cl.aduana.ms_travel.model.Tramite;
import cl.aduana.ms_travel.repository.TramiteRepository;

@ExtendWith(MockitoExtension.class)
class TramiteServiceTest {

    @Mock
    private TramiteRepository tramiteRepository;
    @Mock
    private EstadoTramiteService estadoTramiteService;
    @Mock
    private TramiteMapper tramiteMapper;
    @Mock
    private EstadoTramiteMapper estadoTramiteMapper;
    @Mock
    private PasajeroMapper pasajeroMapper;
    @Mock
    private RiskClient riskClient;
    @Mock
    private UsuarioClient usuarioClient;
    @Mock
    private VehiculoClient vehiculoClient;
    @Mock
    private NotificacionClient notificacionClient;

    @InjectMocks
    private TramiteService tramiteService;

    private Tramite tramiteMock;
    private Long idTramiteValido = 1L;

    @BeforeEach
    void setUp() {
        tramiteMock = new Tramite();
        tramiteMock.setIdTramite(idTramiteValido);
        tramiteMock.setIdUsuario(100L);
        tramiteMock.setIdVehiculo(200L);
        tramiteMock.setPasajeros(new ArrayList<>());
        tramiteMock.setEstadoActual(TipoEstado.INGRESADO);
    }

    // --- PRUEBAS: findTramiteOrThrow & findById ---
    @Test
    @DisplayName("Debe retornar trámite cuando el ID existe")
    void findTramiteOrThrow_Exito() {
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));
        Tramite resultado = tramiteService.findTramiteOrThrow(idTramiteValido);
        assertNotNull(resultado);
        assertEquals(idTramiteValido, resultado.getIdTramite());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el trámite no existe")
    void findTramiteOrThrow_NoEncontrado() {
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.empty());
        
        ResourceNotFoundException excepcion = assertThrows(ResourceNotFoundException.class,
                () -> tramiteService.findTramiteOrThrow(idTramiteValido));

        assertEquals("Tramite no encontrado", excepcion.getMessage());
    }

    @Test
    @DisplayName("Debe retornar TramiteResponseDTO por ID")
    void findById_Exito() {
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));
        when(tramiteMapper.toResponse(tramiteMock)).thenReturn(new TramiteResponseDTO());

        TramiteResponseDTO dto = tramiteService.findById(idTramiteValido);
        assertNotNull(dto);
    }

    // --- PRUEBAS: listarTodos ---
    @Test
    @DisplayName("Debe listar todos los trámites si el filtro de estados está vacío")
    void listarTodos_SinEstados() {
        when(tramiteRepository.findAll()).thenReturn(List.of(tramiteMock));
        when(tramiteMapper.toResumenResponse(any(Tramite.class))).thenReturn(new TramiteResumenResponseDTO());

        List<TramiteResumenResponseDTO> resultado = tramiteService.listarTodos(Collections.emptyList());
        assertFalse(resultado.isEmpty());
        verify(tramiteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe filtrar trámites por estados si se le proporciona una lista")
    void listarTodos_ConEstados() {
        List<TipoEstado> estados = List.of(TipoEstado.INGRESADO);

        // CORRECCIÓN: Cambiamos List.of por Optional.of para cumplir con tu repositorio
        when(tramiteRepository.findAllByEstadoActualIn(estados)).thenReturn(Optional.of(tramiteMock));

        when(tramiteMapper.toResumenResponse(any(Tramite.class))).thenReturn(new TramiteResumenResponseDTO());

        List<TramiteResumenResponseDTO> resultado = tramiteService.listarTodos(estados);
        assertFalse(resultado.isEmpty());
        verify(tramiteRepository, times(1)).findAllByEstadoActualIn(estados);
    }

    @Test
    @DisplayName("Debe listar todos los trámites si el filtro de estados es nulo")
    void listarTodos_NullEstados() {
        when(tramiteRepository.findAll()).thenReturn(List.of(tramiteMock));
        when(tramiteMapper.toResumenResponse(any(Tramite.class))).thenReturn(new TramiteResumenResponseDTO());

        List<TramiteResumenResponseDTO> resultado = tramiteService.listarTodos(null);

        assertFalse(resultado.isEmpty());
        verify(tramiteRepository, times(1)).findAll();
    }

    // --- PRUEBAS: cancelarTramite ---
    @Test
    @DisplayName("Debe cancelar un trámite exitosamente")
    void cancelarTramite_Exito() {
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));
        EstadoTramite nuevoEstado = new EstadoTramite();

        when(estadoTramiteService.agregarEstado(tramiteMock, TipoEstado.CANCELADO_POR_USUARIO, "Tramite cancelado por el usuario"))
                .thenReturn(nuevoEstado);
        when(estadoTramiteMapper.toResponse(nuevoEstado)).thenReturn(new EstadoTramiteResponseDTO());

        EstadoTramiteResponseDTO response = tramiteService.cancelarTramite(idTramiteValido);
        assertNotNull(response);
        verify(estadoTramiteService).validarSiEsActualizable(tramiteMock);
    }

    // --- PRUEBAS: crearNuevoTramite ---
    @Test
    @DisplayName("Debe lanzar BadRequestException al crear trámite con datos nulos")
    void crearNuevoTramite_BadRequest() {
        TramiteRequestDTO request = new TramiteRequestDTO(); // IdUsuario e IdVehiculo nulos
        assertThrows(BadRequestException.class, () -> tramiteService.crearNuevoTramite(request));
    }

    @Test
    @DisplayName("Debe crear trámite exitosamente con pasajeros y flujo de notificación")
    void crearNuevoTramite_Exito() {
        TramiteRequestDTO request = new TramiteRequestDTO();
        request.setIdUsuario(100L);
        request.setIdVehiculo(200L);
        request.setPasajeros(List.of(new PasajeroRequestDTO()));

        Tramite nuevoTramite = new Tramite();
        nuevoTramite.setIdTramite(idTramiteValido);
        nuevoTramite.setIdUsuario(100L);
        nuevoTramite.setIdVehiculo(200L);

        when(usuarioClient.obtenerUsuarioPorId(100L)).thenReturn(new UsuarioClientResponseDTO());
        when(vehiculoClient.obtenerVehiculoPorId(200L)).thenReturn(new VehiculoClientResponseDTO());
        when(tramiteMapper.toEntity(request)).thenReturn(nuevoTramite);
        when(pasajeroMapper.toEntity(any())).thenReturn(new Pasajero());
        when(tramiteRepository.save(any(Tramite.class))).thenReturn(nuevoTramite);

        // Mock de dependencias internas llamadas por el método o submétodos
        when(notificacionClient.enviarMensaje(any(NotificacionRequestDTO.class))).thenReturn(Optional.of(new NotificacionResponseDTO()));
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(nuevoTramite));

        // Para el sub-método evaluarRiesgoTramite dentro de crear
        UsuarioClientResponseDTO uRes = new UsuarioClientResponseDTO();
        uRes.setNroDocumento("123");
        VehiculoClientResponseDTO vRes = new VehiculoClientResponseDTO();
        vRes.setPatente("ABCD12");
        when(usuarioClient.obtenerUsuarioPorId(100L)).thenReturn(uRes);
        when(vehiculoClient.obtenerVehiculoPorId(200L)).thenReturn(vRes);

        EvaluacionRiesgoResponseDTO riskRes = new EvaluacionRiesgoResponseDTO();
        riskRes.setNivelRiesgo("BAJO");
        when(riskClient.evaluarRiesgo(any(EvaluacionRiesgoRequestDTO.class))).thenReturn(Optional.of(riskRes));
        when(tramiteMapper.toResponse(any(Tramite.class))).thenReturn(new TramiteResponseDTO());

        TramiteResponseDTO response = tramiteService.crearNuevoTramite(request);
        assertNotNull(response);
    }

    @Test
    @DisplayName("Debe crear trámite en modo emergencia sin invocar clientes remotos ni notificaciones")
    void crearNuevoTramite_ModoEmergencia() throws Exception {
        java.lang.reflect.Field field = TramiteService.class.getDeclaredField("modoEmergencia");
        field.setAccessible(true);
        field.set(tramiteService, true);

        TramiteRequestDTO request = new TramiteRequestDTO();
        request.setIdUsuario(100L);
        request.setIdVehiculo(200L);
        request.setPasajeros(List.of(new PasajeroRequestDTO()));

        Tramite nuevoTramite = new Tramite();
        nuevoTramite.setIdTramite(idTramiteValido);
        nuevoTramite.setIdUsuario(100L);
        nuevoTramite.setIdVehiculo(200L);

        when(tramiteMapper.toEntity(request)).thenReturn(nuevoTramite);
        when(pasajeroMapper.toEntity(any())).thenReturn(new Pasajero());
        when(tramiteRepository.save(any(Tramite.class))).thenReturn(nuevoTramite);
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(nuevoTramite));
        when(tramiteMapper.toResponse(any(Tramite.class))).thenReturn(new TramiteResponseDTO());
        when(estadoTramiteService.agregarEstado(any(Tramite.class), any(TipoEstado.class), anyString())).thenReturn(new EstadoTramite());

        TramiteResponseDTO response = tramiteService.crearNuevoTramite(request);

        assertNotNull(response);
        verify(usuarioClient, times(0)).obtenerUsuarioPorId(any());
        verify(vehiculoClient, times(0)).obtenerVehiculoPorId(any());
        verify(notificacionClient, times(0)).enviarMensaje(any(NotificacionRequestDTO.class));
    }

    // --- PRUEBAS: evaluarRiesgoTramite (Caminos del Switch) ---
    @Test
    @DisplayName("Debe evaluar riesgo BAJO y pasar a APROBADO_AUTOMATICO")
    void evaluarRiesgo_Bajo() {
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));

        UsuarioClientResponseDTO uRes = new UsuarioClientResponseDTO();
        uRes.setNroDocumento("123");
        VehiculoClientResponseDTO vRes = new VehiculoClientResponseDTO();
        vRes.setPatente("ABCD12");
        when(usuarioClient.obtenerUsuarioPorId(any())).thenReturn(uRes);
        when(vehiculoClient.obtenerVehiculoPorId(any())).thenReturn(vRes);

        EvaluacionRiesgoResponseDTO riskRes = new EvaluacionRiesgoResponseDTO();
        riskRes.setNivelRiesgo("BAJO");
        when(riskClient.evaluarRiesgo(any(EvaluacionRiesgoRequestDTO.class))).thenReturn(Optional.of(riskRes));
        when(tramiteMapper.toResponse(tramiteMock)).thenReturn(new TramiteResponseDTO());

        tramiteService.evaluarRiesgoTramite(idTramiteValido);
        assertEquals(TipoEstado.APROBADO_AUTOMATICO, tramiteMock.getEstadoActual());
    }

    @Test
    @DisplayName("Debe evaluar riesgo ALTO y pasar a RECHAZADO_AUTOMATICO")
    void evaluarRiesgo_Alto() {
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));

        UsuarioClientResponseDTO uRes = new UsuarioClientResponseDTO();
        uRes.setNroDocumento("123");
        VehiculoClientResponseDTO vRes = new VehiculoClientResponseDTO();
        vRes.setPatente("ABCD12");
        when(usuarioClient.obtenerUsuarioPorId(any())).thenReturn(uRes);
        when(vehiculoClient.obtenerVehiculoPorId(any())).thenReturn(vRes);

        EvaluacionRiesgoResponseDTO riskRes = new EvaluacionRiesgoResponseDTO();
        riskRes.setNivelRiesgo("ALTO");
        when(riskClient.evaluarRiesgo(any(EvaluacionRiesgoRequestDTO.class))).thenReturn(Optional.of(riskRes));

        tramiteService.evaluarRiesgoTramite(idTramiteValido);
        assertEquals(TipoEstado.RECHAZADO_AUTOMATICO, tramiteMock.getEstadoActual());
    }

    // --- PRUEBAS: buscarPorParametros ---
    @Test
    @DisplayName("Debe lanzar BadRequestException si se busca sin parámetros")
    void buscarPorParametros_SinFiltros() {
        assertThrows(BadRequestException.class, () -> tramiteService.buscarPorParametros(null, null));
    }

    @Test
    @DisplayName("Debe buscar por Usuario y Vehículo simultáneamente")
    void buscarPorParametros_Ambos() {
        when(tramiteRepository.findByIdUsuarioAndIdVehiculo(100L, 200L)).thenReturn(List.of(tramiteMock));
        List<TramiteResumenResponseDTO> res = tramiteService.buscarPorParametros(100L, 200L);
        assertNotNull(res);
    }

    // --- PRUEBAS: Gestión de Pasajeros ---
    @Test
    @DisplayName("Debe agregar un pasajero al trámite con éxito")
    void agregarPasajero_Exito() {
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));
        PasajeroRequestDTO req = new PasajeroRequestDTO();
        Pasajero p = new Pasajero();

        when(pasajeroMapper.toEntity(req)).thenReturn(p);
        when(pasajeroMapper.toResponse(p)).thenReturn(new PasajeroResponseDTO());

        PasajeroResponseDTO res = tramiteService.agregarPasajero(idTramiteValido, req);
        assertNotNull(res);
        assertEquals(1, tramiteMock.getPasajeros().size());
    }

    @Test
    @DisplayName("Debe quitar pasajero exitosamente")
    void quitarPasajero_Exito() {
        Pasajero p = new Pasajero();
        p.setIdPasajero(55L);
        tramiteMock.getPasajeros().add(p);

        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));

        String res = tramiteService.quitarPasajero(idTramiteValido, 55L);
        assertEquals("Pasajero Eliminado de forma exitosa", res);
        assertTrue(tramiteMock.getPasajeros().isEmpty());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el pasajero a quitar no pertenece al trámite")
    void quitarPasajero_NoEncontrado() {
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));
        assertThrows(ResourceNotFoundException.class, () -> tramiteService.quitarPasajero(idTramiteValido, 999L));
    }

    @Test
    @DisplayName("Debe modificar un pasajero existente en el trámite")
    void modificarPasajero_Exito() {
        Pasajero pasajeroExistente = new Pasajero();
        pasajeroExistente.setIdPasajero(99L);
        pasajeroExistente.setNroDocumento("00000000");
        tramiteMock.getPasajeros().add(pasajeroExistente);

        PasajeroRequestDTO request = new PasajeroRequestDTO();
        request.setNroDocumento("11111111");
        Pasajero actualizado = new Pasajero();
        actualizado.setIdPasajero(99L);
        actualizado.setNroDocumento("11111111");

        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));
        when(pasajeroMapper.updateEntityFromRequest(pasajeroExistente, request)).thenReturn(actualizado);
        when(pasajeroMapper.toResponse(any(Pasajero.class))).thenReturn(new PasajeroResponseDTO());

        PasajeroResponseDTO respuesta = tramiteService.modificarPasajero(idTramiteValido, 99L, request);
        assertNotNull(respuesta);
        assertEquals("00000000", pasajeroExistente.getNroDocumento());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el pasajero no existe al modificar")
    void modificarPasajero_NoEncontrado() {
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));
        assertThrows(ResourceNotFoundException.class, () -> tramiteService.modificarPasajero(idTramiteValido, 123L, new PasajeroRequestDTO()));
    }

    @Test
    @DisplayName("Debe listar los pasajeros del trámite")
    void listarPasajeros_Exito() {
        Pasajero p = new Pasajero();
        p.setIdPasajero(23L);
        tramiteMock.getPasajeros().add(p);

        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));
        when(pasajeroMapper.toResponse(p)).thenReturn(new PasajeroResponseDTO());

        List<PasajeroResponseDTO> resultados = tramiteService.listarPasajeros(idTramiteValido);
        assertNotNull(resultados);
        assertEquals(1, resultados.size());
    }

    @Test
    @DisplayName("Debe actualizar un trámite y devolver resumen")
    void actualizarTramite_Exito() {
        TramiteRequestUpdateDTO updateRequest = new TramiteRequestUpdateDTO();
        Tramite tramiteActualizado = new Tramite();
        tramiteActualizado.setIdTramite(idTramiteValido);
        tramiteActualizado.setIdUsuario(tramiteMock.getIdUsuario());
        tramiteActualizado.setIdVehiculo(tramiteMock.getIdVehiculo());
        tramiteActualizado.setPasajeros(new ArrayList<>());
        tramiteActualizado.setEstadoActual(TipoEstado.INGRESADO);

        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));
        when(tramiteMapper.updateEntityFromRequest(tramiteMock, updateRequest)).thenReturn(tramiteActualizado);
        when(tramiteRepository.save(tramiteActualizado)).thenReturn(tramiteActualizado);
        when(tramiteMapper.toResumenResponse(tramiteActualizado)).thenReturn(new TramiteResumenResponseDTO());

        TramiteResumenResponseDTO resultado = tramiteService.actualizarTramite(idTramiteValido, updateRequest);

        assertNotNull(resultado);
        verify(estadoTramiteService).validarSiEsActualizable(tramiteMock);
    }

    @Test
    @DisplayName("Debe buscar trámites solo por idUsuario")
    void buscarPorParametros_IdUsuario() {
        when(tramiteRepository.findByIdUsuario(100L)).thenReturn(List.of(tramiteMock));
        List<TramiteResumenResponseDTO> res = tramiteService.buscarPorParametros(100L, null);
        assertNotNull(res);
    }

    @Test
    @DisplayName("Debe buscar trámites solo por idVehiculo")
    void buscarPorParametros_IdVehiculo() {
        when(tramiteRepository.findByIdVehiculo(200L)).thenReturn(List.of(tramiteMock));
        List<TramiteResumenResponseDTO> res = tramiteService.buscarPorParametros(null, 200L);
        assertNotNull(res);
    }

    @Test
    @DisplayName("Debe evaluar riesgo MEDIO y pasar a REQUIERE_INSPECCION")
    void evaluarRiesgo_Medio() {
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));
        UsuarioClientResponseDTO uRes = new UsuarioClientResponseDTO();
        uRes.setNroDocumento("123");
        VehiculoClientResponseDTO vRes = new VehiculoClientResponseDTO();
        vRes.setPatente("ABCD12");
        when(usuarioClient.obtenerUsuarioPorId(any())).thenReturn(uRes);
        when(vehiculoClient.obtenerVehiculoPorId(any())).thenReturn(vRes);
        EvaluacionRiesgoResponseDTO riskRes = new EvaluacionRiesgoResponseDTO();
        riskRes.setNivelRiesgo("MEDIO");
        when(riskClient.evaluarRiesgo(any(EvaluacionRiesgoRequestDTO.class))).thenReturn(Optional.of(riskRes));
        when(tramiteMapper.toResponse(tramiteMock)).thenReturn(new TramiteResponseDTO());

        tramiteService.evaluarRiesgoTramite(idTramiteValido);
        assertEquals(TipoEstado.REQUIERE_INSPECCION, tramiteMock.getEstadoActual());
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando no se encuentra el usuario en evaluación de riesgo")
    void evaluarRiesgo_NoUsuario() {
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));
        when(usuarioClient.obtenerUsuarioPorId(any())).thenReturn(null);
        assertThrows(ResourceNotFoundException.class, () -> tramiteService.evaluarRiesgoTramite(idTramiteValido));
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando no se encuentra el vehículo en evaluación de riesgo")
    void evaluarRiesgo_NoVehiculo() {
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));
        UsuarioClientResponseDTO uRes = new UsuarioClientResponseDTO();
        uRes.setNroDocumento("123");
        when(usuarioClient.obtenerUsuarioPorId(any())).thenReturn(uRes);
        when(vehiculoClient.obtenerVehiculoPorId(any())).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> tramiteService.evaluarRiesgoTramite(idTramiteValido));
    }

    @Test
    @DisplayName("Debe aprobar un trámite con éxito")
    void aprobarTramite_Exito() {
        when(tramiteRepository.findById(idTramiteValido)).thenReturn(Optional.of(tramiteMock));
        EstadoTramite estado = new EstadoTramite();
        doNothing().when(estadoTramiteService).validarSiEsAprobable(tramiteMock);
        when(estadoTramiteService.agregarEstado(tramiteMock, TipoEstado.APROBADO, "Tramite aprobado por funcionario de aduana")).thenReturn(estado);
        when(estadoTramiteMapper.toResponse(estado)).thenReturn(new EstadoTramiteResponseDTO());

        EstadoTramiteResponseDTO response = tramiteService.aprobarTramite(idTramiteValido);
        assertNotNull(response);
        verify(tramiteRepository).save(tramiteMock);
    }

    @Test
    @DisplayName("Debe lanzar UnsupportedOperationException para actualizaEstadoGlobal no implementado")
    void actualizaEstadoGlobal_NoImplementado() {
        assertThrows(UnsupportedOperationException.class, () -> tramiteService.actualizaEstadoGlobal(idTramiteValido));
    }
}
