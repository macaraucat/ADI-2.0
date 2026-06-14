package cl.aduana.ms_risk.service;

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
import cl.aduana.ms_risk.dto.AlertaDTO;
import cl.aduana.ms_risk.model.Alerta;
import cl.aduana.ms_risk.model.CriterioRiesgo;
import cl.aduana.ms_risk.model.Inspeccion;
import cl.aduana.ms_risk.repository.AlertaRepository;
import cl.aduana.ms_risk.repository.CriterioRiesgoRepository;
import cl.aduana.ms_risk.repository.InspeccionRepository;

@ExtendWith(MockitoExtension.class)
public class AlertaServiceTest {

    @Mock
    private AlertaRepository repository;

    @Mock
    private InspeccionRepository inspeccionRepository;

    @Mock
    private CriterioRiesgoRepository criterioRepository;

    @InjectMocks
    private AlertaService service;

    @Test
    void givenAlertas_whenListar_thenReturnList() {
        // GIVEN
        Inspeccion insp = new Inspeccion();
        insp.setIdInspeccion(1L);
        CriterioRiesgo crit = new CriterioRiesgo();
        crit.setIdCriterio(1L);
        Alerta a = new Alerta();
        a.setIdAlerta(1L);
        a.setResuelta(false);
        a.setInspeccion(insp);
        a.setCriterioRiesgo(crit);
        when(repository.findAll()).thenReturn(List.of(a));

        // WHEN
        List<AlertaDTO> resultado = service.listar();

        // THEN
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(repository, atMostOnce()).findAll();
    }

    @Test
    void givenId_whenObtenerPorId_thenReturnAlerta() {
        // GIVEN
        Inspeccion insp = new Inspeccion();
        insp.setIdInspeccion(1L);
        CriterioRiesgo crit = new CriterioRiesgo();
        crit.setIdCriterio(1L);
        Alerta a = new Alerta();
        a.setIdAlerta(1L);
        a.setResuelta(true);
        a.setInspeccion(insp);
        a.setCriterioRiesgo(crit);
        when(repository.findById(1L)).thenReturn(Optional.of(a));

        // WHEN
        AlertaDTO resultado = service.obtenerPorId(1L);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdAlerta());
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
    void givenDto_whenGuardar_thenReturnAlerta() {
        // GIVEN
        Inspeccion insp = new Inspeccion();
        insp.setIdInspeccion(1L);
        CriterioRiesgo crit = new CriterioRiesgo();
        crit.setIdCriterio(1L);
        AlertaDTO dto = new AlertaDTO();
        dto.setResuelta(false);
        dto.setIdInspeccion(1L);
        dto.setIdCriterio(1L);
        Alerta saved = new Alerta();
        saved.setIdAlerta(1L);
        saved.setResuelta(false);
        saved.setInspeccion(insp);
        saved.setCriterioRiesgo(crit);

        when(inspeccionRepository.findById(1L)).thenReturn(Optional.of(insp));
        when(criterioRepository.findById(1L)).thenReturn(Optional.of(crit));
        when(repository.save(any())).thenReturn(saved);

        // WHEN
        AlertaDTO resultado = service.guardar(dto);

        // THEN
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdAlerta());
        verify(repository, atMostOnce()).save(any());
    }

    @Test
    void givenDto_whenActualizar_thenReturnAlerta() {
        // GIVEN
        Inspeccion insp = new Inspeccion();
        insp.setIdInspeccion(1L);
        CriterioRiesgo crit = new CriterioRiesgo();
        crit.setIdCriterio(1L);
        Alerta existente = new Alerta();
        existente.setIdAlerta(1L);
        existente.setResuelta(false);
        existente.setInspeccion(insp);
        existente.setCriterioRiesgo(crit);

        AlertaDTO dto = new AlertaDTO();
        dto.setResuelta(true);
        dto.setIdInspeccion(1L);
        dto.setIdCriterio(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(inspeccionRepository.findById(1L)).thenReturn(Optional.of(insp));
        when(criterioRepository.findById(1L)).thenReturn(Optional.of(crit));
        when(repository.save(any())).thenReturn(existente);

        // WHEN
        AlertaDTO resultado = service.actualizar(1L, dto);

        // THEN
        assertNotNull(resultado);
        verify(repository, atMostOnce()).save(any());
    }

    @Test
    void givenNonExistingId_whenActualizar_thenThrowResourceNotFoundException() {
        // GIVEN
        AlertaDTO dto = new AlertaDTO();
        dto.setIdInspeccion(1L);
        dto.setIdCriterio(1L);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> service.actualizar(99L, dto));
    }

    @Test
    void givenId_whenEliminar_thenDeleteAlerta() {
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