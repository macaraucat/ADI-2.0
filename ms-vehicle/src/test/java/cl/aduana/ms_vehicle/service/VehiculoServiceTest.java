package cl.aduana.ms_vehicle.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    void givenId_whenEliminar_thenDeleteVehiculo() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);

        // WHEN
        service.eliminar(1L);

        // THEN
        verify(repository, atMostOnce()).deleteById(1L);
    }
}