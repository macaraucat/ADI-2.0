package cl.aduana.ms_report.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_report.model.Reporte;
import cl.aduana.ms_report.model.TipoReporte;
import cl.aduana.ms_report.service.ReporteService;

@WebMvcTest(ReporteController.class)
public class ReportControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ReporteService service;

    @Test
    void listar_deberiaRetornarOkConListaVacia() throws Exception {
        when(service.listar()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/reportes"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service).listar();
    }

    @Test
    void listar_deberiaRetornarListaConReportes() throws Exception {
        Reporte reporte = crearReporteEjemplo();

        when(service.listar()).thenReturn(List.of(reporte));

        mockMvc.perform(get("/api/v1/reportes"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].id").value(1))
               .andExpect(jsonPath("$[0].tipo").value("USUARIOS"))
               .andExpect(jsonPath("$[0].totalRegistros").value(150))
               .andExpect(jsonPath("$[0].generadoPor").value("juan.garcia@aduana.cl"))
               .andExpect(jsonPath("$[0].formato").value("JSON"));
    }

    @Test
    void obtener_deberiaRetornarReportePorId() throws Exception {
        Reporte reporte = crearReporteEjemplo();
        when(service.obtener(1L)).thenReturn(reporte);

        mockMvc.perform(get("/api/v1/reportes/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1))
               .andExpect(jsonPath("$.tipo").value("USUARIOS"))
               .andExpect(jsonPath("$.generadoPor").value("juan.garcia@aduana.cl"));

        verify(service).obtener(1L);
    }

    @Test
    void obtener_deberiaRetornar404CuandoNoExiste() throws Exception {
        when(service.obtener(999L)).thenThrow(new ResourceNotFoundException("Reporte id 999 no encontrado"));

        mockMvc.perform(get("/api/v1/reportes/999"))
               .andExpect(status().isNotFound());

        verify(service).obtener(999L);
    }

    @Test
    void eliminar_deberiaRetornar204() throws Exception {
        doNothing().when(service).eliminar(1L);

        mockMvc.perform(delete("/api/v1/reportes/1"))
               .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void eliminar_deberiaRetornar404CuandoNoExiste() throws Exception {
        doThrow(new ResourceNotFoundException("Reporte id 999 no encontrado")).when(service).eliminar(999L);

        mockMvc.perform(delete("/api/v1/reportes/999"))
               .andExpect(status().isNotFound());

        verify(service).eliminar(999L);
    }

    private Reporte crearReporteEjemplo() {
        Reporte r = new Reporte();
        r.setId(1L);
        r.setTipo(TipoReporte.USUARIOS);
        r.setFechaGeneracion(LocalDateTime.of(2024, 1, 15, 14, 30, 0));
        r.setTotalRegistros(150);
        r.setGeneradoPor("juan.garcia@aduana.cl");
        r.setFormato("JSON");
        return r;
    }
}