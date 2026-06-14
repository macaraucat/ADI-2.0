package cl.aduana.ms_vehicle.controller;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
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

import cl.aduana.ms_vehicle.dto.AdmisionTemporalDTO;
import cl.aduana.ms_vehicle.service.AdmisionTemporalService;

@WebMvcTest(AdmisionTemporalController.class)
public class AdmisionTemporalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdmisionTemporalService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenAdmisiones_whenGetListar_thenReturn200() throws Exception {
        // GIVEN
        AdmisionTemporalDTO dto = new AdmisionTemporalDTO();
        dto.setIdAdmisionTemporal(1L);
        dto.setEstado("ACTIVO");
        when(service.listar()).thenReturn(List.of(dto));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/admisiones"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].estado").value("ACTIVO"));
    }

    @Test
    void givenId_whenGetObtener_thenReturn200() throws Exception {
        // GIVEN
        AdmisionTemporalDTO dto = new AdmisionTemporalDTO();
        dto.setIdAdmisionTemporal(1L);
        dto.setEstado("ACTIVO");
        when(service.obtenerPorId(1L)).thenReturn(dto);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/admisiones/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.estado").value("ACTIVO"));
    }

    @Test
    void givenDto_whenPost_thenReturn201() throws Exception {
        // GIVEN
        AdmisionTemporalDTO dto = new AdmisionTemporalDTO();
        dto.setIdVehiculo(1L);
        dto.setFechaSalida(LocalDate.now());
        dto.setFechaVencimiento(LocalDate.now().plusDays(30));
        dto.setPasoSalida("Paso Los Libertadores");
        dto.setEstado("ACTIVO");

        when(service.guardar(any())).thenReturn(dto);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/admisiones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
    }

    @Test
    void givenId_whenPut_thenReturn200() throws Exception {
        // GIVEN
        AdmisionTemporalDTO dto = new AdmisionTemporalDTO();
        dto.setIdVehiculo(1L);
        dto.setFechaSalida(LocalDate.now());
        dto.setFechaVencimiento(LocalDate.now().plusDays(30));
        dto.setPasoSalida("Paso Los Libertadores");
        dto.setEstado("ACTIVO");

        when(service.actualizar(eq(1L), any())).thenReturn(dto);

        // WHEN & THEN
        mockMvc.perform(put("/api/v1/admisiones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void givenId_whenDelete_thenReturn204() throws Exception {
        // GIVEN
        doNothing().when(service).eliminar(1L);

        // WHEN & THEN
        mockMvc.perform(delete("/api/v1/admisiones/1"))
            .andExpect(status().isNoContent());
    }
}