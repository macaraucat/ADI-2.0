package cl.aduana.ms_risk.service;

import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_risk.dto.AlertaDTO;
import cl.aduana.ms_risk.model.Alerta;
import cl.aduana.ms_risk.model.CriterioRiesgo;
import cl.aduana.ms_risk.model.Inspeccion;
import cl.aduana.ms_risk.repository.AlertaRepository;
import cl.aduana.ms_risk.repository.CriterioRiesgoRepository;
import cl.aduana.ms_risk.repository.InspeccionRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertaService {

    private final AlertaRepository repository;
    private final InspeccionRepository inspeccionRepository;
    private final CriterioRiesgoRepository criterioRepository;

    private AlertaDTO toDTO(Alerta a) {
        AlertaDTO dto = new AlertaDTO();
        dto.setIdAlerta(a.getIdAlerta());
        dto.setResuelta(a.isResuelta());
        dto.setIdInspeccion(a.getInspeccion().getIdInspeccion());
        dto.setIdCriterio(a.getCriterioRiesgo().getIdCriterio());
        return dto;
    }

    private Alerta toEntity(AlertaDTO dto) {
        Alerta a = new Alerta();
        a.setResuelta(dto.isResuelta());
        
        Inspeccion insp = inspeccionRepository.findById(dto.getIdInspeccion())
            .orElseThrow(() -> new ResourceNotFoundException("Inspección no encontrada: " + dto.getIdInspeccion()));
        a.setInspeccion(insp);
        
        CriterioRiesgo crit = criterioRepository.findById(dto.getIdCriterio())
            .orElseThrow(() -> new ResourceNotFoundException("Criterio no encontrado: " + dto.getIdCriterio()));
        a.setCriterioRiesgo(crit);
        
        return a;
    }

    public List<AlertaDTO> listar() {
        log.info("Listando alertas");
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public AlertaDTO guardar(AlertaDTO dto) {
        log.info("Guardando alerta para inspección {}", dto.getIdInspeccion());
        return toDTO(repository.save(toEntity(dto)));
    }

    public AlertaDTO obtenerPorId(Long id) {
        log.info("Buscando alerta {}", id);
        Alerta a = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Alerta no encontrada: " + id));
        return toDTO(a);
    }

    public AlertaDTO actualizar(Long id, AlertaDTO dto) {
        log.info("Actualizando alerta {}", id);
        Alerta existente = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Alerta no encontrada: " + id));
        
        existente.setResuelta(dto.isResuelta());
        existente.setInspeccion(inspeccionRepository.findById(dto.getIdInspeccion())
            .orElseThrow(() -> new ResourceNotFoundException("Inspección no encontrada")));
        existente.setCriterioRiesgo(criterioRepository.findById(dto.getIdCriterio())
            .orElseThrow(() -> new ResourceNotFoundException("Criterio no encontrado")));
        
        return toDTO(repository.save(existente));
    }

    public void eliminar(Long id) {
        log.info("Eliminando alerta {}", id);
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Alerta no encontrada: " + id);
        }
        repository.deleteById(id);
    }
}