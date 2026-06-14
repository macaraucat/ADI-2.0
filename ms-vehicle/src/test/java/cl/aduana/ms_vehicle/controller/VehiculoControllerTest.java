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

import cl.aduana.ms_vehicle.dto.VehiculoDTO;
import cl.aduana.ms_vehicle.service.VehiculoService;

@WebMvcTest(VehiculoController.class)
public class VehiculoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehiculoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void givenVehiculos_whenGetListar_thenReturn200() throws Exception {
        // GIVEN
        VehiculoDTO dto = new VehiculoDTO();
        dto.setIdVehiculo(1L);
        dto.setPatente("AB1234");
        when(service.listar()).thenReturn(List.of(dto));

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/vehiculos"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].patente").value("AB1234"));
    }

    @Test
    void givenId_whenGetObtener_thenReturn200() throws Exception {
        // GIVEN
        VehiculoDTO dto = new VehiculoDTO();
        dto.setIdVehiculo(1L);
        dto.setPatente("AB1234");
        when(service.obtenerPorId(1L)).thenReturn(dto);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/vehiculos/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.patente").value("AB1234"));
    }

    @Test
    void givenPatente_whenGetObtenerPorPatente_thenReturn200() throws Exception {
        // GIVEN
        VehiculoDTO dto = new VehiculoDTO();
        dto.setIdVehiculo(1L);
        dto.setPatente("AB1234");
        when(service.obtenerPorPatente("AB1234")).thenReturn(dto);

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/vehiculos/patente/AB1234"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.patente").value("AB1234"));
    }

    @Test
    void givenDto_whenPost_thenReturn201() throws Exception {
        // GIVEN
        VehiculoDTO dto = new VehiculoDTO();
        dto.setPatente("AB1234");
        dto.setPaisRegistro("Chile");
        dto.setVin("VIN123");
        dto.setMarca("Toyota");
        dto.setModelo("Corolla");

        when(service.guardar(any())).thenReturn(dto);

        // WHEN & THEN
        mockMvc.perform(post("/api/v1/vehiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated());
    }

    @Test
    void givenId_whenPut_thenReturn200() throws Exception {
        // GIVEN
        VehiculoDTO dto = new VehiculoDTO();
        dto.setPatente("AB1234");
        dto.setPaisRegistro("Chile");
        dto.setVin("VIN123");
        dto.setMarca("Toyota");
        dto.setModelo("Corolla");

        when(service.actualizar(eq(1L), any())).thenReturn(dto);

        // WHEN & THEN
        mockMvc.perform(put("/api/v1/vehiculos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk());
    }

    @Test
    void givenId_whenDelete_thenReturn204() throws Exception {
        // GIVEN
        doNothing().when(service).eliminar(1L);

        // WHEN & THEN
        mockMvc.perform(delete("/api/v1/vehiculos/1"))
            .andExpect(status().isNoContent());
    }
}