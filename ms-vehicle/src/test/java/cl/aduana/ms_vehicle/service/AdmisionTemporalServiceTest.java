package cl.aduana.ms_vehicle.service;

import java.time.LocalDate;
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
import cl.aduana.ms_vehicle.dto.AdmisionTemporalDTO;
import cl.aduana.ms_vehicle.model.AdmisionTemporal;
import cl.aduana.ms_vehicle.model.Vehiculo;
import cl.aduana.ms_vehicle.repository.AdmisionTemporalRepository;
import cl.aduana.ms_vehicle.repository.VehiculoRepository;

@ExtendWith(MockitoExtension.class)
public class AdmisionTemporalServiceTest {

    @Mock
    private AdmisionTemporalRepository repository;

    @Mock
    private VehiculoRepository vehiculoRepository;

    @InjectMocks
    private AdmisionTemporalService service;

    @Test
    void givenAdmisiones_whenListar_thenReturnList() {
        // GIVEN
        Vehiculo v = new Vehiculo(1L, "AB1234", "Chile", "VIN123", "Toyota", "Corolla");
        AdmisionTemporal a = new AdmisionTemporal(1L, LocalDate.now(), LocalDate.now().plusDays(30), "Paso Los Libertadores", "ACTIVO", v);
        when(repository.findAll()).thenReturn(List.of(a));

        // WHEN
        List<AdmisionTemporalDTO> resultado = service.listar();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, atMostOnce()).findAll();
    }

    @Test
    void givenId_whenObtenerPorId_thenReturnAdmision() {
        // GIVEN
        Vehiculo v = new Vehiculo(1L, "AB1234", "Chile", "VIN123", "Toyota", "Corolla");
        AdmisionTemporal a = new AdmisionTemporal(1L, LocalDate.now(), LocalDate.now().plusDays(30), "Paso Los Libertadores", "ACTIVO", v);
        when(repository.findById(1L)).thenReturn(Optional.of(a));

        // WHEN
        AdmisionTemporalDTO resultado = service.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdAdmisionTemporal());
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
    void givenDto_whenGuardar_thenReturnAdmision() {
        // GIVEN
        Vehiculo v = new Vehiculo(1L, "AB1234", "Chile", "VIN123", "Toyota", "Corolla");
        AdmisionTemporalDTO dto = new AdmisionTemporalDTO();
        dto.setIdVehiculo(1L);
        dto.setFechaSalida(LocalDate.now());
        dto.setFechaVencimiento(LocalDate.now().plusDays(30));
        dto.setPasoSalida("Paso Los Libertadores");
        dto.setEstado("ACTIVO");
        AdmisionTemporal saved = new AdmisionTemporal(1L, dto.getFechaSalida(), dto.getFechaVencimiento(), dto.getPasoSalida(), dto.getEstado(), v);
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(v));
        when(repository.save(any())).thenReturn(saved);

        // WHEN
        AdmisionTemporalDTO resultado = service.guardar(dto);

        // THEN
        assertNotNull(resultado);
        assertEquals("ACTIVO", resultado.getEstado());
        verify(repository, atMostOnce()).save(any());
    }

    @Test
    void givenDto_whenActualizar_thenReturnAdmision() {
        // GIVEN
        Vehiculo v = new Vehiculo(1L, "AB1234", "Chile", "VIN123", "Toyota", "Corolla");
        AdmisionTemporal existente = new AdmisionTemporal(1L, LocalDate.now(), LocalDate.now().plusDays(30), "Paso Los Libertadores", "ACTIVO", v);
        AdmisionTemporalDTO dto = new AdmisionTemporalDTO();
        dto.setIdVehiculo(1L);
        dto.setFechaSalida(LocalDate.now());
        dto.setFechaVencimiento(LocalDate.now().plusDays(60));
        dto.setPasoSalida("Paso Los Libertadores");
        dto.setEstado("CERRADO");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(vehiculoRepository.findById(1L)).thenReturn(Optional.of(v));
        when(repository.save(any())).thenReturn(existente);

        // WHEN
        AdmisionTemporalDTO resultado = service.actualizar(1L, dto);

        // THEN
        assertNotNull(resultado);
        verify(repository, atMostOnce()).save(any());
    }

    @Test
    void givenNonExistingId_whenActualizar_thenThrowResourceNotFoundException() {
        // GIVEN
        AdmisionTemporalDTO dto = new AdmisionTemporalDTO();
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> service.actualizar(99L, dto));
    }

    @Test
    void givenId_whenEliminar_thenDeleteAdmision() {
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