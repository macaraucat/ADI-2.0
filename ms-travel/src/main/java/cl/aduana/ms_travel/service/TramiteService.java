package cl.aduana.ms_travel.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value; // <-- IMPORTANTE
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TramiteService {
    private final TramiteRepository tramiteRepository;
    private final EstadoTramiteService estadoTramiteService;
    private final TramiteMapper tramiteMapper;
    private final EstadoTramiteMapper estadoTramiteMapper;
    private final PasajeroMapper pasajeroMapper;
    private final RiskClient riskClient;
    private final UsuarioClient usuarioClient;
    private final VehiculoClient vehiculoClient;
    private final NotificacionClient notificacionClient;

    // Testing de vías de emergencia sin afectar la lógica real del servicio
    @Value("${app.modo-emergencia:false}")
    private boolean modoEmergencia;

    public Tramite findTramiteOrThrow(Long idTramite) {
        log.info("iniciando busqueda para id: {}", idTramite);
        return tramiteRepository.findById(idTramite)
                .orElseThrow(() -> new ResourceNotFoundException("Tramite no encontrado"));
    }

    @Transactional(readOnly = true)
    public TramiteResponseDTO findById(Long idTramite) {
        Tramite tramite = this.findTramiteOrThrow(idTramite);
        return tramiteMapper.toResponse(tramite);
    }

    @Transactional(readOnly = true)
    public List<TramiteResumenResponseDTO> listarTodos(List<TipoEstado> estados) {
        if (estados == null || estados.isEmpty()) {
            log.info("Listando tramites...");
            return tramiteRepository.findAll().stream().map(tramiteMapper::toResumenResponse).toList();
        }
        log.info("Listando tramite segun los estados: {}", estados.toString());
        return tramiteRepository.findAllByEstadoActualIn(estados)
                .stream().map(tramiteMapper::toResumenResponse).toList();
    }

    @Transactional
    public EstadoTramiteResponseDTO cancelarTramite(Long idTramite) {
        Tramite tramite = this.findTramiteOrThrow(idTramite);
        estadoTramiteService.validarSiEsActualizable(tramite);
        log.info("Cancelando tramite para Id: {}", tramite.getIdTramite());
        EstadoTramite nuevoEstado = estadoTramiteService.agregarEstado(tramite, 
            TipoEstado.CANCELADO_POR_USUARIO, "Tramite cancelado por el usuario");
        return estadoTramiteMapper.toResponse(nuevoEstado);
    }

    @Transactional
    public TramiteResumenResponseDTO actualizarTramite(Long idTramite, TramiteRequestUpdateDTO tramiteRequestUpdate) {
        Tramite tramiteExistente = this.findTramiteOrThrow(idTramite);
        estadoTramiteService.validarSiEsActualizable(tramiteExistente);
        Tramite tramiteActualizado = tramiteMapper.updateEntityFromRequest(tramiteExistente, tramiteRequestUpdate);
        tramiteActualizado.setEstadoActual(TipoEstado.INGRESADO);
        tramiteRepository.save(tramiteActualizado);
        return tramiteMapper.toResumenResponse(tramiteActualizado);
    }

    @Transactional
    public TramiteResponseDTO crearNuevoTramite(TramiteRequestDTO request) {
        log.info("Iniciando verificacion de Usuario con Id: {}", request.getIdUsuario());
        if (request.getIdUsuario() == null || request.getIdVehiculo() == null) {
            throw new BadRequestException("idUsuario e idVehiculo son obligatorios");
        }        
        
        //SIMULACIÓN DE CLIENTES (USUARIO Y VEHÍCULO)
        if (modoEmergencia) {
            log.warn("[MODO EMERGENCIA] Saltando llamadas reales a UsuarioClient y VehiculoClient");
        } else {
            usuarioClient.obtenerUsuarioPorId(request.getIdUsuario());        
            vehiculoClient.obtenerVehiculoPorId(request.getIdVehiculo());
        }

        Tramite nuevo = tramiteMapper.toEntity(request);
        nuevo.setEstadoActual(TipoEstado.INGRESADO);
        nuevo.setFechaDeIngreso(LocalDateTime.now());

        if (request.getPasajeros() != null) {
            List<Pasajero> pasajeros = request.getPasajeros().stream()
                    .map(pasajeroRequest -> {
                        Pasajero p = pasajeroMapper.toEntity(pasajeroRequest);
                        p.setTramite(nuevo);
                        return p;
                    }).toList();
            nuevo.setPasajeros(pasajeros);
        }

        Tramite guardado = tramiteRepository.save(nuevo);
        estadoTramiteService.agregarEstado(guardado, TipoEstado.INGRESADO, "Tramite creado con exito");

        //SIMULACIÓN DE NOTIFICACIÓN
        if (modoEmergencia) {
            log.warn("[MODO EMERGENCIA] Simulando envío de notificación por correo exitosa");
        } else {
            NotificacionRequestDTO aviso = NotificacionRequestDTO.builder()
                    .idUsuario(guardado.getIdUsuario())
                    .tipoNotificacion("CorreoElectronico")
                    .detalle("Su tramite ha sido creado con exito!")
                    .build();
            Optional<NotificacionResponseDTO> respuesta = notificacionClient.enviarMensaje(aviso);
            log.info("Notificando al usuario... {}", respuesta);
        }

        log.info("Tramite creado, preparando evaluacion de riesgo. Tramite: {}", 
            tramiteMapper.toResumenResponse(guardado));
        
        this.evaluarRiesgoTramite(guardado.getIdTramite());
        return tramiteMapper.toResponse(guardado);
    }

    @Transactional
    public TramiteResponseDTO evaluarRiesgoTramite(Long idTramite) {
        log.info("Iniciando evaluación de riesgo para el trámite ID: {}", idTramite);
        Tramite tramite = tramiteRepository.findById(idTramite)
                .orElseThrow(() -> new ResourceNotFoundException("Tramite no encontrado"));
        estadoTramiteService.validarSiEsEvaluable(tramite);

        String nroDocumentoSujeto = "MOCK-DOC-123";
        String patente = "MOCK-PAT-999";

        //SIMULACIÓN DE CLIENTES EN RIESGO
        if (modoEmergencia) {
            log.warn("[MODO EMERGENCIA] Creando datos simulados para análisis de riesgo");
        } else {
            UsuarioClientResponseDTO usuario = usuarioClient.obtenerUsuarioPorId(tramite.getIdUsuario());
            if (usuario == null) {
                throw new ResourceNotFoundException("No se pudo obtener usuario");
            }
            VehiculoClientResponseDTO vehiculo = vehiculoClient.obtenerVehiculoPorId(tramite.getIdVehiculo());
            if (vehiculo == null) {
                throw new ResourceNotFoundException("No se pudo obtener vehículo");
            }
            nroDocumentoSujeto = usuario.getNroDocumento();
            patente = vehiculo.getPatente();
        }

        EvaluacionRiesgoResponseDTO resultadoRiesgo;

        //  SIMULACIÓN DE RESPUESTA DE RIESGO
        if (modoEmergencia) {
            log.warn("[MODO EMERGENCIA] Simulando análisis del RiskClient -> Retornando riesgo BAJO predefinido");

            // Al usar @Data, el constructor vacío está disponible
            resultadoRiesgo = new EvaluacionRiesgoResponseDTO();

            // Seteamos únicamente los campos reales que tiene el DTO
            resultadoRiesgo.setNivelRiesgo("BAJO"); // <-- Puedes cambiarlo a "MEDIO" o "ALTO" para probar
            resultadoRiesgo.setDetalle("Simulación de riesgo exitosa en modo de emergencia");
        } else {
            EvaluacionRiesgoRequestDTO evaluacionRiesgo = EvaluacionRiesgoRequestDTO.builder()
                    .nroDocumentoSujeto(nroDocumentoSujeto)
                    .patente(patente)
                    .nroDocumentoAcompanantes(tramite.getPasajeros().stream()
                            .map(Pasajero::getNroDocumento).toList())
                    .idTramite(idTramite)
                    .build();

            resultadoRiesgo = riskClient.evaluarRiesgo(evaluacionRiesgo)
                    .orElseThrow(() -> new IllegalStateException("Error al enviar solicitud de riesgo"));
        }

        log.info("Evaluación completada. Nivel: {}", resultadoRiesgo.getNivelRiesgo());

        switch (resultadoRiesgo.getNivelRiesgo()) {
            case "BAJO" -> {
                tramite.setEstadoActual(TipoEstado.APROBADO_AUTOMATICO);
                estadoTramiteService.agregarEstado(tramite, TipoEstado.APROBADO_AUTOMATICO,
                        "Tramite aprobado automaticamente");
            }
            case "MEDIO" -> {
                tramite.setEstadoActual(TipoEstado.REQUIERE_INSPECCION);
                estadoTramiteService.agregarEstado(tramite, TipoEstado.REQUIERE_INSPECCION,
                        "Tramite requiere inspección");
            }
            case "ALTO" -> {
                tramite.setEstadoActual(TipoEstado.RECHAZADO_AUTOMATICO);
                estadoTramiteService.agregarEstado(tramite, TipoEstado.RECHAZADO_AUTOMATICO,
                        "Tramite rechazado automaticamente");
            }
        }
        return tramiteMapper.toResponse(tramite);
    }

    public EstadoTramiteResponseDTO actualizaEstadoGlobal(Long tramiteId) {
        throw new UnsupportedOperationException("Unimplemented method 'actualizaEstadoGlobal'");
    }

    @Transactional
    public EstadoTramiteResponseDTO aprobarTramite(Long idTramite) {
        Tramite tramite = tramiteRepository.findById(idTramite).orElseThrow(() -> new ResourceNotFoundException("Tramite no encontrado"));
        estadoTramiteService.validarSiEsAprobable(tramite);
        EstadoTramite nuevoEstado = estadoTramiteService.agregarEstado(tramite, TipoEstado.APROBADO, "Tramite aprobado por funcionario de aduana");
        tramiteRepository.save(tramite);
        return estadoTramiteMapper.toResponse(nuevoEstado);
    }

    @Transactional(readOnly = true)
    public List<TramiteResumenResponseDTO> buscarPorParametros(Long idUsuario, Long idVehiculo) {
        log.info("Iniciando búsqueda paramétrica en BD...");
        
        if (idUsuario == null && idVehiculo == null) {
            log.warn("Se intentó buscar sin parámetros");
            throw new BadRequestException("Debes proporcionar al menos un filtro: userId o vehiculoId");
        }

        List<Tramite> resultados;
        if (idUsuario != null && idVehiculo != null) {
            resultados = tramiteRepository.findByIdUsuarioAndIdVehiculo(idUsuario, idVehiculo);
        } else if (idUsuario != null) {
            resultados = tramiteRepository.findByIdUsuario(idUsuario);
        } else {
            resultados = tramiteRepository.findByIdVehiculo(idVehiculo);
        }

        return resultados.stream()
                .map(tramiteMapper::toResumenResponse)
                .toList();
    }

    // METODOS DE PASAJEROS

    @Transactional
    public PasajeroResponseDTO agregarPasajero(Long idTramite, PasajeroRequestDTO pasajeroRequest) {
        Tramite tramite = this.findTramiteOrThrow(idTramite); 
        estadoTramiteService.validarSiEsActualizable(tramite);
        
        Pasajero nuevoPasajero = pasajeroMapper.toEntity(pasajeroRequest);
        nuevoPasajero.setTramite(tramite);    
        tramite.getPasajeros().add(nuevoPasajero);
        return pasajeroMapper.toResponse(nuevoPasajero);
    }

    @Transactional
    public PasajeroResponseDTO modificarPasajero(Long idTramite, Long idPasajero, PasajeroRequestDTO pasajeroRequest) {
        Tramite tramite = this.findTramiteOrThrow(idTramite);
        estadoTramiteService.validarSiEsActualizable(tramite);
        
        log.info("buscando pasajero Id: {}, del tramite Id: {}", idPasajero, idTramite);
        Pasajero pasajeroExistente = tramite.getPasajeros().stream()
                .filter(p -> p.getIdPasajero().equals(idPasajero))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no registrado en el trámite"));

        pasajeroMapper.updateEntityFromRequest(pasajeroExistente, pasajeroRequest);
        log.info("pasajero actualizado: {}", pasajeroExistente);
        return pasajeroMapper.toResponse(pasajeroExistente);
    }
    
    @Transactional
    public String quitarPasajero(Long idTramite, Long idPasajero) {
        Tramite tramite = this.findTramiteOrThrow(idTramite);
        estadoTramiteService.validarSiEsActualizable(tramite);
        log.info("buscando pasajero Id: {}, del tramite Id: {}", idPasajero, idTramite);
        Pasajero pasajero = tramite.getPasajeros().stream()
                .filter(p -> p.getIdPasajero().equals(idPasajero))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero no encontrado en este trámite"));
        
        tramite.getPasajeros().remove(pasajero);
        return "Pasajero Eliminado de forma exitosa";
    }

    @Transactional(readOnly = true)
    public List<PasajeroResponseDTO> listarPasajeros(Long idTramite) {
        Tramite tramite = this.findTramiteOrThrow(idTramite);
        return tramite.getPasajeros().stream()
                .map(pasajeroMapper::toResponse)
                .toList();
    }    
}