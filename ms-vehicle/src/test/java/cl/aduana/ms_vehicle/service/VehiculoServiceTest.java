package cl.aduana.ms_vehicle.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_vehicle.dto.VehiculoDTO;
import cl.aduana.ms_vehicle.model.Vehiculo;
import cl.aduana.ms_vehicle.repository.VehiculoRepository;

@ExtendWith(MockitoExtension.class)
public class VehiculoServiceTest {

    @Mock
    private VehiculoRepository repository;

    @InjectMocks
    private VehiculoService service;

    @Test
    void givenVehiculos_whenListar_thenReturnList() {
        // GIVEN
        Vehiculo v = new Vehiculo(1L, "AB1234", "Chile", "VIN123", "Toyota", "Corolla");
        when(repository.findAll()).thenReturn(List.of(v));

        // WHEN
        List<VehiculoDTO> resultado = service.listar();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, atMostOnce()).findAll();
    }

    @Test
    void givenId_whenObtenerPorId_thenReturnVehiculo() {
        // GIVEN
        Vehiculo v = new Vehiculo(1L, "AB1234", "Chile", "VIN123", "Toyota", "Corolla");
        when(repository.findById(1L)).thenReturn(Optional.of(v));

        // WHEN
        VehiculoDTO resultado = service.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals("AB1234", resultado.getPatente());
        verify(repository, atMostOnce()).findById(1L);
    }

    @Test
    void givenNonExistingId_whenObtenerPorId_thenThrowResourceNotFoundException() {
        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
        verify(repository, atMostOnce()).findById(99L);
    }

    @Test
    void givenPatente_whenObtenerPorPatente_thenReturnVehiculo() {
        // GIVEN
        Vehiculo v = new Vehiculo(1L, "AB1234", "Chile", "VIN123", "Toyota", "Corolla");
        when(repository.findByPatente("AB1234")).thenReturn(Optional.of(v));

        // WHEN
        VehiculoDTO resultado = service.obtenerPorPatente("AB1234");

        // THEN
        assertNotNull(resultado);
        assertEquals("AB1234", resultado.getPatente());
        verify(repository, atMostOnce()).findByPatente("AB1234");
    }

    @Test
    void givenNonExistingPatente_whenObtenerPorPatente_thenThrowResourceNotFoundException() {
        // GIVEN
        when(repository.findByPatente("ZZ9999")).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorPatente("ZZ9999"));
    }

    @Test
    void givenDto_whenGuardar_thenReturnVehiculo() {
        // GIVEN
        VehiculoDTO dto = new VehiculoDTO();
        dto.setPatente("AB1234");
        dto.setPaisRegistro("Chile");
        dto.setVin("VIN123");
        dto.setMarca("Toyota");
        dto.setModelo("Corolla");

        Vehiculo saved = new Vehiculo(1L, "AB1234", "Chile", "VIN123", "Toyota", "Corolla");
        when(repository.save(any())).thenReturn(saved);

        // WHEN
        VehiculoDTO resultado = service.guardar(dto);

        // THEN
        assertNotNull(resultado);
        assertEquals("AB1234", resultado.getPatente());
        verify(repository, atMostOnce()).save(any());
    }

    @Test
    void givenDto_whenActualizar_thenReturnVehiculo() {
        // GIVEN
        Vehiculo existente = new Vehiculo(1L, "AB1234", "Chile", "VIN123", "Toyota", "Corolla");
        VehiculoDTO dto = new VehiculoDTO();
        dto.setPatente("CD5678");
        dto.setPaisRegistro("Chile");
        dto.setVin("VIN999");
        dto.setMarca("Nissan");
        dto.setModelo("Versa");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(existente);

        // WHEN
        VehiculoDTO resultado = service.actualizar(1L, dto);

        // THEN
        assertNotNull(resultado);
        verify(repository, atMostOnce()).save(any());
    }

    @Test
    void givenNonExistingId_whenActualizar_thenThrowResourceNotFoundException() {
        // GIVEN
        VehiculoDTO dto = new VehiculoDTO();
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> service.actualizar(99L, dto));
    }

    @Test
    void givenId_whenEliminar_thenDeleteVehiculo() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);

        // WHEN
        service.eliminar(1L);

        // THEN
        verify(repository, atMostOnce()).deleteById(1L);
    }

    @Test
    void givenNonExistingId_whenEliminar_thenThrowResourceNotFoundException() {
        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> service.eliminar(99L));
    }
}