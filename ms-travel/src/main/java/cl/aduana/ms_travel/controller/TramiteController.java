package cl.aduana.ms_travel.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.aduana.ms_travel.dto.estado.EstadoTramiteResponseDTO;
import cl.aduana.ms_travel.dto.pasajero.PasajeroRequestDTO;
import cl.aduana.ms_travel.dto.pasajero.PasajeroResponseDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteRequestDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteRequestUpdateDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteResponseDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteResumenResponseDTO;
import cl.aduana.ms_travel.model.TipoEstado;
import cl.aduana.ms_travel.service.TramiteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/tramites")
@Slf4j
@RequiredArgsConstructor
public class TramiteController {

    private final TramiteService service;

    // LISTAR
    @GetMapping
    public ResponseEntity<List<TramiteResumenResponseDTO>> listar(
            @RequestParam(required = false) List<TipoEstado> estados) {
        log.info("GET /tramites - estados={}", estados);
        return ResponseEntity.ok(service.listarTodos(estados));
    }

    // OBTENER POR ID
    @GetMapping("/{id}")
    public ResponseEntity<TramiteResponseDTO> obtener(@PathVariable Long id) {
        log.info("GET /tramites/{} - buscando", id);
        return ResponseEntity.ok(service.findById(id));
    }

    // CREAR
    @PostMapping
    public ResponseEntity<TramiteResponseDTO> crear(@Valid @RequestBody TramiteRequestDTO request) {
        log.info("POST /tramites - nuevo tramite usuario={} vehiculo={}", 
                 request.getIdUsuario(), request.getIdVehiculo());
        TramiteResponseDTO creado = service.crearNuevoTramite(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // ACTUALIZAR
    @PutMapping("/{id}")
    public ResponseEntity<TramiteResumenResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody TramiteRequestUpdateDTO request) {
        log.info("PUT /tramites/{} - actualizando", id);
        return ResponseEntity.ok(service.actualizarTramite(id, request));
    }

    // CANCELAR
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<EstadoTramiteResponseDTO> cancelar(@PathVariable Long id) {
        log.warn("PATCH /tramites/{}/cancelar", id);
        return ResponseEntity.ok(service.cancelarTramite(id));
    }

    // APROBAR
    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<EstadoTramiteResponseDTO> aprobar(@PathVariable Long id) {
        log.info("PATCH /tramites/{}/aprobar", id);
        return ResponseEntity.ok(service.aprobarTramite(id));
    }

    // EVALUAR RIESGO MANUAL
    @PostMapping("/{id}/evaluar-riesgo")
    public ResponseEntity<TramiteResponseDTO> evaluarRiesgo(@PathVariable Long id) {
        log.info("POST /tramites/{}/evaluar-riesgo", id);
        return ResponseEntity.ok(service.evaluarRiesgoTramite(id));
    }

    // BUSCAR POR PARAMETROS
    @GetMapping("/buscar")
    public ResponseEntity<List<TramiteResumenResponseDTO>> buscar(
            @RequestParam(required = false) Long idUsuario,
            @RequestParam(required = false) Long idVehiculo) {
        log.info("GET /tramites/buscar - usuario={} vehiculo={}", idUsuario, idVehiculo);
        return ResponseEntity.ok(service.buscarPorParametros(idUsuario, idVehiculo));
    }

    // --- PASAJEROS ---

    @GetMapping("/{id}/pasajeros")
    public ResponseEntity<List<PasajeroResponseDTO>> listarPasajeros(@PathVariable Long id) {
        log.info("GET /tramites/{}/pasajeros", id);
        return ResponseEntity.ok(service.listarPasajeros(id));
    }

    @PostMapping("/{id}/pasajeros")
    public ResponseEntity<PasajeroResponseDTO> agregarPasajero(
            @PathVariable Long id,
            @Valid @RequestBody PasajeroRequestDTO request) {
        log.info("POST /tramites/{}/pasajeros - doc={}", id, request.getNroDocumento());
        PasajeroResponseDTO resp = service.agregarPasajero(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PutMapping("/{id}/pasajeros/{idPasajero}")
    public ResponseEntity<PasajeroResponseDTO> modificarPasajero(
            @PathVariable Long id,
            @PathVariable Long idPasajero,
            @Valid @RequestBody PasajeroRequestDTO request) {
        log.info("PUT /tramites/{}/pasajeros/{}", id, idPasajero);
        return ResponseEntity.ok(service.modificarPasajero(id, idPasajero, request));
    }

    @DeleteMapping("/{id}/pasajeros/{idPasajero}")
    public ResponseEntity<String> quitarPasajero(
            @PathVariable Long id,
            @PathVariable Long idPasajero) {
        log.warn("DELETE /tramites/{}/pasajeros/{}", id, idPasajero);
        return ResponseEntity.ok(service.quitarPasajero(id, idPasajero));
    }
}