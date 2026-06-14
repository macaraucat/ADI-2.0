package cl.aduana.ms_risk.service;

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

import cl.aduana.common.exception.BadRequestException;
import cl.aduana.common.exception.InvalidStateException;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_risk.dto.InspeccionRequestDTO;
import cl.aduana.ms_risk.dto.InspeccionResponseDTO;
import cl.aduana.ms_risk.dto.PatchInspeccionDTO;
import cl.aduana.ms_risk.model.Inspeccion;
import cl.aduana.ms_risk.model.Inspeccion.EstadoInspeccion;
import cl.aduana.ms_risk.repository.InspeccionRepository;

@ExtendWith(MockitoExtension.class)
public class InspeccionServiceTest {

    @Mock
    private InspeccionRepository repository;

    @InjectMocks
    private InspeccionService service;

    @Test
    void givenInspecciones_whenListar_thenReturnList() {
        // GIVEN
        Inspeccion i = new Inspeccion();
        i.setIdInspeccion(1L);
        i.setIdTramite(10L);
        i.setIdFuncionario("5");
        i.setFecha(LocalDate.now());
        i.setEstado(EstadoInspeccion.INICIADA);
        when(repository.findAll()).thenReturn(List.of(i));

        // WHEN
        List<InspeccionResponseDTO> resultado = service.listar();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, atMostOnce()).findAll();
    }

    @Test
    void givenId_whenObtenerPorId_thenReturnInspeccion() {
        // GIVEN
        Inspeccion i = new Inspeccion();
        i.setIdInspeccion(1L);
        i.setIdTramite(10L);
        i.setIdFuncionario("5");
        i.setFecha(LocalDate.now());
        i.setEstado(EstadoInspeccion.INICIADA);
        when(repository.findById(1L)).thenReturn(Optional.of(i));

        // WHEN
        InspeccionResponseDTO resultado = service.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdInspeccion());
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
    void givenDto_whenGuardar_thenReturnInspeccion() {
        // GIVEN
        InspeccionRequestDTO dto = new InspeccionRequestDTO();
        dto.setIdTramite(10L);
        dto.setIdFuncionario("5");
        dto.setFecha(LocalDate.now());
        dto.setComentarios("Sin novedad");

        Inspeccion saved = new Inspeccion();
        saved.setIdInspeccion(1L);
        saved.setIdTramite(10L);
        saved.setIdFuncionario("5");
        saved.setFecha(dto.getFecha());
        saved.setComentarios(dto.getComentarios());
        saved.setEstado(EstadoInspeccion.INICIADA);

        when(repository.save(any())).thenReturn(saved);

        // WHEN
        InspeccionResponseDTO resultado = service.guardar(dto);

        // THEN
        assertNotNull(resultado);
        assertEquals(EstadoInspeccion.INICIADA, resultado.getEstado());
        verify(repository, atMostOnce()).save(any());
    }

    @Test
    void givenInvalidIdTramite_whenGuardar_thenThrowBadRequestException() {
        // GIVEN
        InspeccionRequestDTO dto = new InspeccionRequestDTO();
        dto.setIdTramite(0L);
        dto.setIdFuncionario("5");

        // WHEN & THEN
        assertThrows(BadRequestException.class, () -> service.guardar(dto));
    }

    @Test
    void givenId_whenFinalizar_thenReturnInspeccionFinalizada() {
        // GIVEN
        Inspeccion existente = new Inspeccion();
        existente.setIdInspeccion(1L);
        existente.setIdTramite(10L);
        existente.setIdFuncionario("5");
        existente.setEstado(EstadoInspeccion.INICIADA);

        PatchInspeccionDTO patch = new PatchInspeccionDTO();
        patch.setResultado("APROBADO");
        patch.setComentarios("Todo correcto");

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(any())).thenReturn(existente);

        // WHEN
        InspeccionResponseDTO resultado = service.finalizar(1L, patch);

        // THEN
        assertNotNull(resultado);
        assertEquals(EstadoInspeccion.FINALIZADA, resultado.getEstado());
        verify(repository, atMostOnce()).save(any());
    }

    @Test
    void givenNonExistingId_whenFinalizar_thenThrowResourceNotFoundException() {
        // GIVEN
        PatchInspeccionDTO patch = new PatchInspeccionDTO();
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> service.finalizar(99L, patch));
    }

    @Test
    void givenFinalizada_whenFinalizar_thenThrowInvalidStateException() {
        // GIVEN
        Inspeccion existente = new Inspeccion();
        existente.setIdInspeccion(1L);
        existente.setEstado(EstadoInspeccion.FINALIZADA);

        PatchInspeccionDTO patch = new PatchInspeccionDTO();

        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        // WHEN & THEN
        assertThrows(InvalidStateException.class, () -> service.finalizar(1L, patch));
    }

    @Test
    void givenId_whenEliminar_thenDeleteInspeccion() {
        // GIVEN
        Inspeccion existente = new Inspeccion();
        existente.setIdInspeccion(1L);
        existente.setEstado(EstadoInspeccion.INICIADA);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        // WHEN
        service.eliminar(1L);

        // THEN
        verify(repository, atMostOnce()).delete(existente);
    }

    @Test
    void givenNonExistingId_whenEliminar_thenThrowResourceNotFoundException() {
        // GIVEN
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> service.eliminar(99L));
    }

    @Test
    void givenFinalizada_whenEliminar_thenThrowInvalidStateException() {
        // GIVEN
        Inspeccion existente = new Inspeccion();
        existente.setIdInspeccion(1L);
        existente.setEstado(EstadoInspeccion.FINALIZADA);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));

        // WHEN & THEN
        assertThrows(InvalidStateException.class, () -> service.eliminar(1L));
    }
}