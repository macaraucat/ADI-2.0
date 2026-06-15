package cl.aduana.ms_risk.controller;

import java.time.LocalDate;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.aduana.common.exception.BadRequestException;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_risk.dto.InspeccionRequestDTO;
import cl.aduana.ms_risk.dto.InspeccionResponseDTO;
import cl.aduana.ms_risk.dto.PatchInspeccionDTO;
import cl.aduana.ms_risk.model.Inspeccion.EstadoInspeccion;
import cl.aduana.ms_risk.service.InspeccionService;

@WebMvcTest(InspeccionController.class)
public class InspeccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private InspeccionService service;

    @Test
    void givenInspecciones_whenListar_thenReturn200WithList() throws Exception {
        InspeccionResponseDTO dto = new InspeccionResponseDTO();
        dto.setIdInspeccion(1L);
        dto.setIdTramite(10L);
        dto.setIdFuncionario("5");
        dto.setFecha(LocalDate.now());
        dto.setEstado(EstadoInspeccion.INICIADA);

        when(service.listar()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/inspeccion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idInspeccion").value(1L));
    }

    @Test
    void givenDto_whenGuardar_thenReturn201() throws Exception {
        InspeccionRequestDTO req = new InspeccionRequestDTO();
        req.setIdTramite(10L);
        req.setIdFuncionario("5");
        req.setFecha(LocalDate.now());
        req.setComentarios("Sin novedad");

        InspeccionResponseDTO saved = new InspeccionResponseDTO();
        saved.setIdInspeccion(1L);
        saved.setIdTramite(10L);
        saved.setIdFuncionario("5");
        saved.setEstado(EstadoInspeccion.INICIADA);

        when(service.guardar(any())).thenReturn(saved);

        mockMvc.perform(post("/api/v1/inspeccion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idInspeccion").value(1L));
    }

    @Test
    void givenInvalidIdTramite_whenGuardar_thenReturn400() throws Exception {
        InspeccionRequestDTO req = new InspeccionRequestDTO();
        req.setIdTramite(0L);
        req.setIdFuncionario("5");

        when(service.guardar(any())).thenThrow(new BadRequestException("El id del trámite debe ser mayor a 0"));

        mockMvc.perform(post("/api/v1/inspeccion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenId_whenObtener_thenReturn200() throws Exception {
        InspeccionResponseDTO dto = new InspeccionResponseDTO();
        dto.setIdInspeccion(1L);
        dto.setIdTramite(10L);
        dto.setIdFuncionario("5");
        dto.setEstado(EstadoInspeccion.INICIADA);

        when(service.obtenerPorId(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/inspeccion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idInspeccion").value(1L));
    }

    @Test
    void givenNonExistingId_whenObtener_thenReturn404() throws Exception {
        when(service.obtenerPorId(99L)).thenThrow(new ResourceNotFoundException("Inspección no encontrada: 99"));

        mockMvc.perform(get("/api/v1/inspeccion/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenId_whenFinalizar_thenReturn200() throws Exception {
        PatchInspeccionDTO patch = new PatchInspeccionDTO();
        patch.setResultado("APROBADO");
        patch.setComentarios("Todo correcto");

        InspeccionResponseDTO finalizada = new InspeccionResponseDTO();
        finalizada.setIdInspeccion(1L);
        finalizada.setEstado(EstadoInspeccion.FINALIZADA);

        when(service.finalizar(eq(1L), any())).thenReturn(finalizada);

        mockMvc.perform(patch("/api/v1/inspeccion/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("FINALIZADA"));
    }

    @Test
    void givenNonExistingId_whenFinalizar_thenReturn404() throws Exception {
        PatchInspeccionDTO patch = new PatchInspeccionDTO();
        patch.setResultado("APROBADO");
        patch.setComentarios("Sin novedad");

        when(service.finalizar(eq(99L), any())).thenThrow(new ResourceNotFoundException("Inspección no encontrada: 99"));

        mockMvc.perform(patch("/api/v1/inspeccion/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patch)))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenId_whenEliminar_thenReturn204() throws Exception {
        mockMvc.perform(delete("/api/v1/inspeccion/1"))
                .andExpect(status().isNoContent());

        verify(service).eliminar(1L);
    }

    @Test
    void givenNonExistingId_whenEliminar_thenReturn404() throws Exception {
        org.mockito.Mockito.doThrow(new ResourceNotFoundException("Inspección no encontrada: 99"))
                .when(service).eliminar(99L);

        mockMvc.perform(delete("/api/v1/inspeccion/99"))
                .andExpect(status().isNotFound());
    }
}