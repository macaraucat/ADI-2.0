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
import cl.aduana.ms_vehicle.dto.ConductorAutorizadoDTO;
import cl.aduana.ms_vehicle.model.AdmisionTemporal;
import cl.aduana.ms_vehicle.model.ConductorAutorizado;
import cl.aduana.ms_vehicle.repository.AdmisionTemporalRepository;
import cl.aduana.ms_vehicle.repository.ConductorAutorizadoRepository;

@ExtendWith(MockitoExtension.class)
public class ConductorAutorizadoServiceTest {

    @Mock
    private ConductorAutorizadoRepository repository;

    @Mock
    private AdmisionTemporalRepository admisionRepository;

    @InjectMocks
    private ConductorAutorizadoService service;

    @Test
    void givenConductores_whenListar_thenReturnList() {
        // GIVEN
        AdmisionTemporal adm = new AdmisionTemporal();
        adm.setIdAdmisionTemporal(1L);
        ConductorAutorizado c = new ConductorAutorizado(1L, "Juan Pérez", "12345678", adm);
        when(repository.findAll()).thenReturn(List.of(c));

        // WHEN
        List<ConductorAutorizadoDTO> resultado = service.listar();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, atMostOnce()).findAll();
    }

    @Test
    void givenId_whenObtenerPorId_thenReturnConductor() {
        // GIVEN
        AdmisionTemporal adm = new AdmisionTemporal();
        adm.setIdAdmisionTemporal(1L);
        ConductorAutorizado c = new ConductorAutorizado(1L, "Juan Pérez", "12345678", adm);
        when(repository.findById(1L)).thenReturn(Optional.of(c));

        // WHEN
        ConductorAutorizadoDTO resultado = service.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
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
    void givenId_whenEliminar_thenDeleteConductor() {
        // GIVEN
        when(repository.existsById(1L)).thenReturn(true);

        // WHEN
        service.eliminar(1L);

        // THEN
        verify(repository, atMostOnce()).deleteById(1L);
    }
}