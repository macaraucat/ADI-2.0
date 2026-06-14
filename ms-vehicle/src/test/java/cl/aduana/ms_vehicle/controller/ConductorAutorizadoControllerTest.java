package cl.aduana.ms_vehicle.controller;

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

import cl.aduana.ms_vehicle.dto.ConductorAutorizadoDTO;
import cl.aduana.ms_vehicle.service.ConductorAutorizadoService;

@WebMvcTest(ConductorAutorizadoController.class)
public class ConductorAutorizadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConductorAutorizadoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenConductores_whenGetListar_thenReturn200() throws Exception {
        // GIVEN
        ConductorAutorizadoDTO dto = new ConductorAutorizadoDTO();
        dto.setIdConductor(1L);
        dto.setNombre("Juan Pérez");
        when(service.listar()).thenReturn(List.of(dto));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/conductores"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));
    }

    @Test
    void givenId_whenGetObtener_thenReturn200() throws Exception {
        // GIVEN
        ConductorAutorizadoDTO dto = new ConductorAutorizadoDTO();
        dto.setIdConductor(1L);
        dto.setNombre("Juan Pérez");
        when(service.obtenerPorId(1L)).thenReturn(dto);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/conductores/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
    }

    @Test
    void givenDto_whenPost_thenReturn201() throws Exception {
        // GIVEN
        ConductorAutorizadoDTO dto = new ConductorAutorizadoDTO();
        dto.setNombre("Juan Pérez");
        dto.setNroDocumento("12345678");
        dto.setIdAdmisionTemporal(1L);

        when(service.guardar(any())).thenReturn(dto);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/conductores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
    }

    @Test
    void givenId_whenPut_thenReturn200() throws Exception {
        // GIVEN
        ConductorAutorizadoDTO dto = new ConductorAutorizadoDTO();
        dto.setNombre("Juan Pérez");
        dto.setNroDocumento("12345678");
        dto.setIdAdmisionTemporal(1L);

        when(service.actualizar(eq(1L), any())).thenReturn(dto);

        // WHEN & THEN
        mockMvc.perform(put("/api/v1/conductores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void givenId_whenDelete_thenReturn204() throws Exception {
        // GIVEN
        doNothing().when(service).eliminar(1L);

        // WHEN & THEN
        mockMvc.perform(delete("/api/v1/conductores/1"))
            .andExpect(status().isNoContent());
    }
}