package cl.aduana.ms_report.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cl.aduana.ms_report.service.ReporteService;

@WebMvcTest(ReporteController.class)
public class ReportControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ReporteService service;

    @Test
    void listar() throws Exception {
        when(service.listar()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/reportes")).andExpect(status().isOk());
    }
}