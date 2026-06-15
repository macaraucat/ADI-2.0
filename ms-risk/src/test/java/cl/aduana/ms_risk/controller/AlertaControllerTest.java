package cl.aduana.ms_risk.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_risk.dto.AlertaDTO;
import cl.aduana.ms_risk.service.AlertaService;

@WebMvcTest(AlertaController.class)
public class AlertaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AlertaService service;

    @Test
    void givenAlertas_whenListar_thenReturn200WithList() throws Exception {
        // GIVEN
        AlertaDTO dto = new AlertaDTO();
        dto.setIdAlerta(1L);
        dto.setResuelta(false);
        dto.setIdInspeccion(1L);
        dto.setIdCriterio(1L);

        when(service.listar()).thenReturn(List.of(dto));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/alerta"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idAlerta").value(1L));
    }

    @Test
    void givenDto_whenGuardar_thenReturn201() throws Exception {
        // GIVEN
        AlertaDTO dto = new AlertaDTO();
        dto.setResuelta(false);
        dto.setIdInspeccion(1L);
        dto.setIdCriterio(1L);

        AlertaDTO saved = new AlertaDTO();
        saved.setIdAlerta(1L);
        saved.setResuelta(false);
        saved.setIdInspeccion(1L);
        saved.setIdCriterio(1L);

        when(service.guardar(any())).thenReturn(saved);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/alerta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idAlerta").value(1L));
    }

    @Test
    void givenId_whenObtener_thenReturn200() throws Exception {
        // GIVEN
        AlertaDTO dto = new AlertaDTO();
        dto.setIdAlerta(1L);
        dto.setResuelta(true);
        dto.setIdInspeccion(1L);
        dto.setIdCriterio(1L);

        when(service.obtenerPorId(1L)).thenReturn(dto);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/alerta/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAlerta").value(1L));
    }

    @Test
    void givenNonExistingId_whenObtener_thenReturn404() throws Exception {
        // GIVEN
        when(service.obtenerPorId(99L)).thenThrow(new ResourceNotFoundException("Alerta no encontrada: 99"));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/alerta/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenDto_whenActualizar_thenReturn200() throws Exception {
        // GIVEN
        AlertaDTO dto = new AlertaDTO();
        dto.setResuelta(true);
        dto.setIdInspeccion(1L);
        dto.setIdCriterio(1L);

        AlertaDTO actualizado = new AlertaDTO();
        actualizado.setIdAlerta(1L);
        actualizado.setResuelta(true);
        actualizado.setIdInspeccion(1L);
        actualizado.setIdCriterio(1L);

        when(service.actualizar(eq(1L), any())).thenReturn(actualizado);

        // WHEN & THEN
        mockMvc.perform(put("/api/v1/alerta/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resuelta").value(true));
    }

    @Test
    void givenNonExistingId_whenActualizar_thenReturn404() throws Exception {
        // GIVEN
        AlertaDTO dto = new AlertaDTO();
        dto.setResuelta(true);
        dto.setIdInspeccion(1L);
        dto.setIdCriterio(1L);

        when(service.actualizar(eq(99L), any())).thenThrow(new ResourceNotFoundException("Alerta no encontrada: 99"));

        // WHEN & THEN
        mockMvc.perform(put("/api/v1/alerta/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenId_whenEliminar_thenReturn204() throws Exception {
        // WHEN & THEN
        mockMvc.perform(delete("/api/v1/alerta/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void givenNonExistingId_whenEliminar_thenReturn404() throws Exception {
        // GIVEN
        org.mockito.Mockito.doThrow(new ResourceNotFoundException("Alerta no encontrada: 99"))
                .when(service).eliminar(99L);

        // WHEN & THEN
        mockMvc.perform(delete("/api/v1/alerta/99"))
                .andExpect(status().isNotFound());
    }
}