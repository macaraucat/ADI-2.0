package cl.aduana.ms_report.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_report.model.Reporte;
import cl.aduana.ms_report.repository.ReporteRepository;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    
    @Mock
    private ReporteRepository repo;

    @InjectMocks
    private ReporteService service;

    @Test
    void listarReportes_deberiaRetornarLista() {
        when(repo.findAll()).thenReturn(List.of(new Reporte(), new Reporte()));
        assertEquals(2, service.listar().size());
    }

    @Test
    void obtenerPorId_noExiste_deberiaLanzar() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.obtener(99L));
    }

    @Test
    void eliminarPorId_Existe_deberiaEliminar() {
        when(repo.existsById(1L)).thenReturn(true);
        service.eliminar(1L);
        verify(repo, atMostOnce()).deleteById(1L);
    }

}