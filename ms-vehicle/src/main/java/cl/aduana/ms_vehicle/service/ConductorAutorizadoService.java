package cl.aduana.ms_vehicle.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_vehicle.dto.ConductorAutorizadoDTO;
import cl.aduana.ms_vehicle.model.AdmisionTemporal;
import cl.aduana.ms_vehicle.model.ConductorAutorizado;
import cl.aduana.ms_vehicle.repository.AdmisionTemporalRepository;
import cl.aduana.ms_vehicle.repository.ConductorAutorizadoRepository;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class ConductorAutorizadoService {
    private final ConductorAutorizadoRepository repository;
    private final AdmisionTemporalRepository admisionRepository;

    private ConductorAutorizadoDTO toDTO(ConductorAutorizado c) {
        ConductorAutorizadoDTO dto = new ConductorAutorizadoDTO();
        dto.setIdConductor(c.getIdConductor());
        dto.setNombre(c.getNombre());
        dto.setNroDocumento(c.getNroDocumento());
        dto.setIdAdmisionTemporal(c.getAdmisionTemporal().getIdAdmisionTemporal());
        return dto;
    }

    public List<ConductorAutorizadoDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public ConductorAutorizadoDTO guardar(ConductorAutorizadoDTO dto) {
        AdmisionTemporal adm = admisionRepository.findById(dto.getIdAdmisionTemporal())
            .orElseThrow(() -> new ResourceNotFoundException("Admisión no encontrada"));
        ConductorAutorizado c = new ConductorAutorizado();
        c.setNombre(dto.getNombre());
        c.setNroDocumento(dto.getNroDocumento());
        c.setAdmisionTemporal(adm);
        return toDTO(repository.save(c));
    }

    public ConductorAutorizadoDTO obtenerPorId(Long id) {
        return toDTO(repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado")));
    }

    public ConductorAutorizadoDTO actualizar(Long id, ConductorAutorizadoDTO dto) {
        ConductorAutorizado c = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado"));
        AdmisionTemporal adm = admisionRepository.findById(dto.getIdAdmisionTemporal())
            .orElseThrow(() -> new ResourceNotFoundException("Admisión no encontrada"));
        c.setNombre(dto.getNombre());
        c.setNroDocumento(dto.getNroDocumento());
        c.setAdmisionTemporal(adm);
        return toDTO(repository.save(c));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Conductor no encontrado");
        repository.deleteById(id);
    }
}