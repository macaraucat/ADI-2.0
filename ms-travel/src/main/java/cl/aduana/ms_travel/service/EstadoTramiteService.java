package cl.aduana.ms_travel.service;

import java.time.LocalDateTime;
import java.util.EnumSet;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.aduana.common.exception.InvalidStateException;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_travel.model.Estado;
import cl.aduana.ms_travel.model.EstadoTramite;
import cl.aduana.ms_travel.model.TipoEstado;
import cl.aduana.ms_travel.model.Tramite;
import cl.aduana.ms_travel.repository.EstadoRepository;
import cl.aduana.ms_travel.repository.EstadoTramiteRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstadoTramiteService {

    private final EstadoRepository estadoRepository;
    private final EstadoTramiteRepository estadoTramiteRepository;

    private final EnumSet<TipoEstado> estadosBloqueantes = EnumSet.of(
        TipoEstado.CANCELADO_POR_USUARIO, 
        TipoEstado.FINALIZADO, 
        TipoEstado.INSPECCION_EN_CURSO, 
        TipoEstado.TODO_EN_ORDEN, 
        TipoEstado.RECHAZADO_EN_INSPECCION
    );

    private final EnumSet<TipoEstado> estadosAutomaticos = EnumSet.of(
        TipoEstado.APROBADO_AUTOMATICO, 
        TipoEstado.RECHAZADO_AUTOMATICO,
        TipoEstado.REQUIERE_INSPECCION
    );

    private final EnumSet<TipoEstado> sePuedeAprobar = EnumSet.of(
        TipoEstado.TODO_EN_ORDEN, 
        TipoEstado.APROBADO_AUTOMATICO
    );

    public void validarSiEsEvaluable(Tramite tramite) {
        TipoEstado estadoActual = tramite.getEstadoActual();
        if (estadosAutomaticos.contains(estadoActual)) {
            throw new InvalidStateException("El tramite ya se encuentra evaluado por el sistema: " + estadoActual);
        }
        if (estadosBloqueantes.contains(estadoActual)) {
            throw new InvalidStateException("La operacion no puede llevarse a cabo: " + estadoActual);
        }
    }

    public void validarSiEsActualizable(Tramite tramite) {
        TipoEstado estadoActual = tramite.getEstadoActual();
        if (estadosAutomaticos.contains(estadoActual)) {
            return;
        }
        this.validarSiEsEvaluable(tramite);
    }

    public void validarSiEsAprobable(Tramite tramite) {
        TipoEstado estadoActual = tramite.getEstadoActual();
        if (!sePuedeAprobar.contains(estadoActual)) {
            throw new InvalidStateException(
                "No se puede aprobar ya que el trámite se encuentra en estado que no permite aprobación");
        }
    }

    @Transactional
    public EstadoTramite agregarEstado(Tramite tramite, TipoEstado estado, String mensaje) {
        Estado estadoEncontrado = estadoRepository.findByNombre(estado)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "El estado no existe en la base de datos: " + estado));

        EstadoTramite nuevoEstado = EstadoTramite.builder()
                .tramite(tramite)
                .estado(estadoEncontrado)
                .fechaCambio(LocalDateTime.now())
                .comentario(mensaje)
                .build();

        tramite.getHistorialEstados().add(nuevoEstado);
        tramite.setEstadoActual(estado);

        return estadoTramiteRepository.save(nuevoEstado);
    }
}