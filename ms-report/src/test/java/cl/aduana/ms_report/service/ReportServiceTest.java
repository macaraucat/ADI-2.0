package cl.aduana.ms_report.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.aduana.common.client.RiskClient;
import cl.aduana.common.client.TravelClient;
import cl.aduana.common.client.UsuarioClient;
import cl.aduana.common.client.VehiculoClient;
import cl.aduana.common.dto.response.AlertaClientResponseDTO;
import cl.aduana.common.dto.response.VehiculoClientResponseDTO;
import cl.aduana.common.exception.BadRequestException;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_report.model.Reporte;
import cl.aduana.ms_report.model.TipoReporte;
import cl.aduana.ms_report.repository.ReporteRepository;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {
    
    @Mock
    private ReporteRepository repo;
    @Mock
    private UsuarioClient usuarioClient;
    @Mock
    private VehiculoClient vehiculoClient;
    @Mock
    private TravelClient travelClient;
    @Mock
    private RiskClient riskClient;
    @InjectMocks
    private ReporteService service;

    @Test
    void listarReportes_deberiaRetornarLista() {
        when(repo.findAll()).thenReturn(List.of(new Reporte(), new Reporte()));
        assertEquals(2, service.listar().size());
        verify(repo).findAll();
    }

    @Test
    void listar_deberiaRetornarVacio() {
        when(repo.findAll()).thenReturn(List.of());        
        assertTrue(service.listar().isEmpty());
    }

    @Test
    void obtener_deberiaRetornarReporteCuandoExiste() {
        Reporte reporte = crearReporte(1L);
        when(repo.findById(1L)).thenReturn(Optional.of(reporte));

        Reporte result = service.obtener(1L);
        assertEquals(1L, result.getId());
        assertEquals(TipoReporte.USUARIOS, result.getTipo());
        verify(repo).findById(1L);
    }

    @Test
    void obtenerPorId_noExiste_deberiaLanzar() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.obtener(99L));
    }

    @Test
    void generar_vehiculos_deberiaCrearReporte() {
        when(vehiculoClient.listarTodos()).thenReturn(List.of(new VehiculoClientResponseDTO()));
        when(repo.save(any(Reporte.class))).thenAnswer(inv -> inv.getArgument(0));

        Reporte result = service.generar(TipoReporte.VEHICULOS, "juan@test.cl");
        assertEquals(1, result.getTotalRegistros());
        verify(vehiculoClient).listarTodos();
    }

    @Test
    void generar_tramites_deberiaCrearReporte() {
        when(travelClient.listarTodos()).thenReturn(List.of());
        when(repo.save(any(Reporte.class))).thenAnswer(inv -> inv.getArgument(0));

        Reporte result = service.generar(TipoReporte.TRAMITES, "juan@test.cl");
        assertEquals(0, result.getTotalRegistros());
        verify(travelClient).listarTodos();
    }

    @Test
    void generar_alertas_deberiaCrearReporte() {
        when(riskClient.listarAlertas()).thenReturn(List.of(new AlertaClientResponseDTO(), new AlertaClientResponseDTO()));
        when(repo.save(any(Reporte.class))).thenAnswer(inv -> inv.getArgument(0));

        Reporte result = service.generar(TipoReporte.ALERTAS, "juan@test.cl");
        assertEquals(2, result.getTotalRegistros());
        verify(riskClient).listarAlertas();
    }

    @Test
    void generar_conTipoNulo_deberiaLanzarBadRequest() {
        assertThrows(BadRequestException.class, () -> service.generar(null, "juan@test.cl"));
        verifyNoInteractions(repo, usuarioClient, vehiculoClient, travelClient, riskClient);
    }

    @Test
    void eliminarPorId_Existe_deberiaEliminar() {
        when(repo.existsById(1L)).thenReturn(true);
        service.eliminar(1L);
        verify(repo).existsById(1L);
        verify(repo).deleteById(1L);
    }

    @Test
    void eliminar_deberiaLanzarResourceNotFoundCuandoNoExiste() {
        when(repo.existsById(99L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.eliminar(99L));
        verify(repo, never()).deleteById(anyLong());
    }    

    private Reporte crearReporte(Long id) {
        return Reporte.builder()
                .id(id)
                .tipo(TipoReporte.USUARIOS)
                .fechaGeneracion(LocalDateTime.now())
                .totalRegistros(150)
                .generadoPor("juan@test.cl")
                .formato("JSON")
                .build();
    }
}