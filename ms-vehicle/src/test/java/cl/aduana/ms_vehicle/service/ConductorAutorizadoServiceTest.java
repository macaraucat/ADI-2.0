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
    void givenDto_whenGuardar_thenReturnConductor() {
        // GIVEN
        AdmisionTemporal adm = new AdmisionTemporal();
        adm.setIdAdmisionTemporal(1L);
        ConductorAutorizadoDTO dto = new ConductorAutorizadoDTO();
        dto.setNombre("Juan Pérez");
        dto.setNroDocumento("12345678");
        dto.setIdAdmisionTemporal(1L);
        ConductorAutorizado saved = new ConductorAutorizado(1L, dto.getNombre(), dto.getNroDocumento(), adm);

        when(admisionRepository.findById(1L)).thenReturn(Optional.of(adm));
        when(repository.save(any())).thenReturn(saved);

        // WHEN
        ConductorAutorizadoDTO resultado = service.guardar(dto);

        // THEN
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(repository, atMostOnce()).save(any());
    }

    @Test
    void givenNonExistingAdmision_whenGuardar_thenThrowResourceNotFoundException() {
        // GIVEN
        ConductorAutorizadoDTO dto = new ConductorAutorizadoDTO();
        dto.setIdAdmisionTemporal(99L);
        when(admisionRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> service.guardar(dto));
    }

    @Test
    void givenDto_whenActualizar_thenReturnConductor() {
        // GIVEN
        AdmisionTemporal adm = new AdmisionTemporal();
        adm.setIdAdmisionTemporal(1L);
        ConductorAutorizado existente = new ConductorAutorizado(1L, "Juan Pérez", "12345678", adm);
        ConductorAutorizadoDTO dto = new ConductorAutorizadoDTO();
        dto.setNombre("Juan Actualizado");
        dto.setNroDocumento("99999999");
        dto.setIdAdmisionTemporal(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(admisionRepository.findById(1L)).thenReturn(Optional.of(adm));
        when(repository.save(any())).thenReturn(existente);

        // WHEN
        ConductorAutorizadoDTO resultado = service.actualizar(1L, dto);

        // THEN
        assertNotNull(resultado);
        verify(repository, atMostOnce()).save(any());
    }

    @Test
    void givenNonExistingId_whenActualizar_thenThrowResourceNotFoundException() {
        // GIVEN
        ConductorAutorizadoDTO dto = new ConductorAutorizadoDTO();
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> service.actualizar(99L, dto));
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

    @Test
    void givenNonExistingId_whenEliminar_thenThrowResourceNotFoundException() {
        // GIVEN
        when(repository.existsById(99L)).thenReturn(false);

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> service.eliminar(99L));
    }
}