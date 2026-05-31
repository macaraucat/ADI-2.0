package cl.aduana.ms_risk.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import cl.aduana.common.exception.BadRequestException;
import cl.aduana.common.exception.InvalidStateException;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_risk.dto.InspeccionRequestDTO;
import cl.aduana.ms_risk.dto.InspeccionResponseDTO;
import cl.aduana.ms_risk.dto.PatchInspeccionDTO;
import cl.aduana.ms_risk.model.Inspeccion;
import cl.aduana.ms_risk.model.Inspeccion.EstadoInspeccion;
import cl.aduana.ms_risk.repository.InspeccionRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class InspeccionService {

    private final InspeccionRepository repository;

    private InspeccionResponseDTO toDTO(Inspeccion i) {
        InspeccionResponseDTO dto = new InspeccionResponseDTO();
        dto.setIdInspeccion(i.getIdInspeccion());
        dto.setIdTramite(i.getIdTramite());
        dto.setIdFuncionario(i.getIdFuncionario());
        dto.setFecha(i.getFecha());
        dto.setResultado(i.getResultado());
        dto.setComentarios(i.getComentarios());
        dto.setEstado(i.getEstado());
        return dto;
    }

    private Inspeccion toEntity(InspeccionRequestDTO dto) {
        Inspeccion i = new Inspeccion();
        i.setIdTramite(dto.getIdTramite());
        i.setIdFuncionario(dto.getIdFuncionario());
        i.setFecha(dto.getFecha() != null ? dto.getFecha() : LocalDate.now());
        i.setComentarios(dto.getComentarios());
        i.setEstado(EstadoInspeccion.INICIADA);
        return i;
    }

    public InspeccionResponseDTO guardar(InspeccionRequestDTO dto) {
        log.info("Creando inspección para trámite {}", dto.getIdTramite());
        if (dto.getIdTramite() <= 0) {
            throw new BadRequestException("El id del trámite debe ser mayor a 0");
        }
        return toDTO(repository.save(toEntity(dto)));
    }

    public List<InspeccionResponseDTO> listar() {
        log.info("Listando inspecciones");
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public InspeccionResponseDTO obtenerPorId(Long id) {
        log.info("Buscando inspección {}", id);
        Inspeccion i = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inspección no encontrada: " + id));
        return toDTO(i);
    }

    public InspeccionResponseDTO finalizar(Long id, PatchInspeccionDTO patch) {
        log.info("Finalizando inspección {}", id);
        Inspeccion existente = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inspección no encontrada: " + id));

        if (EstadoInspeccion.FINALIZADA.equals(existente.getEstado())) {
            throw new InvalidStateException("La inspección ya está finalizada");
        }

        existente.setResultado(patch.getResultado());
        existente.setComentarios(patch.getComentarios());
        existente.setEstado(EstadoInspeccion.FINALIZADA);

        return toDTO(repository.save(existente));
    }

    public void eliminar(Long id) {
        log.info("Eliminando inspección {}", id);
        Inspeccion existente = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Inspección no encontrada: " + id));

        if (EstadoInspeccion.FINALIZADA.equals(existente.getEstado())) {
            throw new InvalidStateException("No se puede eliminar una inspección finalizada");
        }

        repository.delete(existente);
    }
}