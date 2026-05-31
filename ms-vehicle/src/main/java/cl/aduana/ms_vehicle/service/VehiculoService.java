package cl.aduana.ms_vehicle.service;

import java.util.List;
import org.springframework.stereotype.Service;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_vehicle.dto.VehiculoDTO;
import cl.aduana.ms_vehicle.model.Vehiculo;
import cl.aduana.ms_vehicle.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @RequiredArgsConstructor @Slf4j
public class VehiculoService {
    private final VehiculoRepository repository;

    private VehiculoDTO toDTO(Vehiculo v) {
        VehiculoDTO dto = new VehiculoDTO();
        dto.setIdVehiculo(v.getIdVehiculo());
        dto.setPatente(v.getPatente());
        dto.setPaisRegistro(v.getPaisRegistro());
        dto.setVin(v.getVin());
        dto.setMarca(v.getMarca());
        dto.setModelo(v.getModelo());
        return dto;
    }

    private Vehiculo toEntity(VehiculoDTO dto) {
        Vehiculo v = new Vehiculo();
        v.setPatente(dto.getPatente());
        v.setPaisRegistro(dto.getPaisRegistro());
        v.setVin(dto.getVin());
        v.setMarca(dto.getMarca());
        v.setModelo(dto.getModelo());
        return v;
    }

    public List<VehiculoDTO> listar() {
        return repository.findAll().stream().map(this::toDTO).toList();
    }

    public VehiculoDTO guardar(VehiculoDTO dto) {
        return toDTO(repository.save(toEntity(dto)));
    }

    public VehiculoDTO obtenerPorId(Long id) {
        return toDTO(repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado")));
    }

    public VehiculoDTO obtenerPorPatente(String patente) {
        return toDTO(repository.findByPatente(patente)
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado")));
    }

    public VehiculoDTO actualizar(Long id, VehiculoDTO dto) {
        Vehiculo v = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehículo no encontrado"));
        v.setPatente(dto.getPatente());
        v.setPaisRegistro(dto.getPaisRegistro());
        v.setVin(dto.getVin());
        v.setMarca(dto.getMarca());
        v.setModelo(dto.getModelo());
        return toDTO(repository.save(v));
    }

    public void eliminar(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Vehículo no encontrado");
        repository.deleteById(id);
    }
}