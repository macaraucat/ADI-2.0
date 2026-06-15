package cl.aduana.ms_risk.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import cl.aduana.common.dto.request.EvaluacionRiesgoRequestDTO;
import cl.aduana.common.dto.response.EvaluacionRiesgoResponseDTO;
import cl.aduana.common.exception.BadRequestException;
import cl.aduana.ms_risk.service.RiesgoService;

@WebMvcTest(RiesgoController.class)
public class RiesgoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RiesgoService service;

    @Test
    void givenRequest_whenEvaluar_thenReturn200() throws Exception {
        EvaluacionRiesgoRequestDTO req = EvaluacionRiesgoRequestDTO.builder()
                .nroDocumentoSujeto("12345678")
                .patente("AB1234")
                .idTramite(1L)
                .build();

        EvaluacionRiesgoResponseDTO resp = new EvaluacionRiesgoResponseDTO();
        resp.setNivelRiesgo("BAJO");
        resp.setDetalle("Evaluación automática completada");

        when(service.evaluar(any())).thenReturn(resp);

        mockMvc.perform(post("/api/v1/riesgo/evaluar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nivelRiesgo").value("BAJO"));
    }

    @Test
    void givenDatosInvalidos_whenEvaluar_thenReturn400() throws Exception {
        EvaluacionRiesgoRequestDTO req = EvaluacionRiesgoRequestDTO.builder().build();

        when(service.evaluar(any())).thenThrow(new BadRequestException("Documento y patente son obligatorios"));

        mockMvc.perform(post("/api/v1/riesgo/evaluar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}