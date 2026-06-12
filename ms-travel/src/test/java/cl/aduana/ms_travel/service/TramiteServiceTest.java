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
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
        when(usuarioClient.obtenerUsuarioPorId(idTramiteValido)).thenReturn(uRes);
        when(vehiculoClient.obtenerVehiculoPorId(idTramiteValido)).thenReturn(vRes);

        EvaluacionRiesgoResponseDTO riskRes = new EvaluacionRiesgoResponseDTO();
        riskRes.setNivelRiesgo("BAJO");
        when(riskClient.evaluarRiesgo(any(EvaluacionRiesgoRequestDTO.class))).thenReturn(Optional.of(riskRes));
        when(tramiteMapper.toResponse(any(Tramite.class))).thenReturn(new TramiteResponseDTO());

        TramiteResponseDTO response = tramiteService.crearNuevoTramite(request);
        assertNotNull(response);
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
}
