package cl.aduana.ms_vehicle.service;

import java.util.List;
import org.springframework.stereotype.Service;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_vehicle.dto.AdmisionTemporalDTO;
import cl.aduana.ms_vehicle.model.AdmisionTemporal;
import cl.aduana.ms_vehicle.model.Vehiculo;
import cl.aduana.ms_vehicle.repository.AdmisionTemporalRepository;
import cl.aduana.ms_vehicle.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;

@Service @RequiredArgsConstructor
public class AdmisionTemporalService {
    private final AdmisionTemporalRepository repository;
    private final VehiculoRepository vehiculoRepository;

    private AdmisionTemporalDTO toDTO(AdmisionTemporal a) {
        AdmisionTemporalDTO dto = new AdmisionTemporalDTO();
        dto.setIdAdmisionTemporal(a.getIdAdmisionTemporal());
        dto.setFechaSalida(a.getFechaSalida());
        dto.setFechaVencimiento(a.getFechaVencimiento());
        dto.setPasoSalida(a.getPasoSalida());
        dto.setEstado(a.getEstado());
        dto.setIdVehiculo(a.getVehiculo().getIdVehiculo());
        return dto;
    }

    public List<AdmisionTemporalDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public AdmisionTemporalDTO guardar(AdmisionTemporalDTO dto) {
        Vehiculo v = vehiculoRepository.findById(dto.getIdVehiculo())
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado"));
        AdmisionTemporal a = new AdmisionTemporal();
        a.setFechaSalida(dto.getFechaSalida());
        a.setFechaVencimiento(dto.getFechaVencimiento());
        a.setPasoSalida(dto.getPasoSalida());
        a.setEstado(dto.getEstado());
        a.setVehiculo(v);
        return toDTO(repository.save(a));
    }

    public AdmisionTemporalDTO obtenerPorId(Long id) {
        return toDTO(repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Admisión no encontrada")));
    }

    public AdmisionTemporalDTO actualizar(Long id, AdmisionTemporalDTO dto) {
        AdmisionTemporal a = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Admisión no encontrada"));
        Vehiculo v = vehiculoRepository.findById(dto.getIdVehiculo())
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado"));
        a.setFechaSalida(dto.getFechaSalida());
        a.setFechaVencimiento(dto.getFechaVencimiento());
        a.setPasoSalida(dto.getPasoSalida());
        a.setEstado(dto.getEstado());
        a.setVehiculo(v);
        return toDTO(repository.save(a));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Admisión no encontrada");
        repository.deleteById(id);
    }
}