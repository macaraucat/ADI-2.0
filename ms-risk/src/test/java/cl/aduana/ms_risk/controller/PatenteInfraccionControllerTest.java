package cl.aduana.ms_risk.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cl.aduana.ms_risk.repository.PatenteInfraccionRepository;

@WebMvcTest(PatenteInfraccionController.class)
public class PatenteInfraccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatenteInfraccionRepository repository;

    @Test
    void givenPatentes_whenListar_thenReturn200WithList() throws Exception {
        // GIVEN
        when(repository.findAll()).thenReturn(List.of());

        // WHEN & THEN
        mockMvc.perform(get("/api/v1/patentes-infraccion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}