package cl.aduana.ms_travel.controller;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import cl.aduana.ms_travel.dto.estado.EstadoTramiteResponseDTO;
import cl.aduana.ms_travel.dto.pasajero.PasajeroRequestDTO;
import cl.aduana.ms_travel.dto.pasajero.PasajeroResponseDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteRequestDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteResponseDTO;
import cl.aduana.ms_travel.dto.tramite.TramiteResumenResponseDTO;
import cl.aduana.ms_travel.model.TipoEstado;
import cl.aduana.ms_travel.model.TipoTramite;
import cl.aduana.ms_travel.service.TramiteService;

@WebMvcTest(TramiteController.class)
class TramiteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TramiteService tramiteService;

    @Test
    @DisplayName("GET /api/v1/tramites debe devolver lista de trámites")
    void listarTramites_DeberiaRetornarLista() throws Exception {
        TramiteResumenResponseDTO resumen = TramiteResumenResponseDTO.builder()
                .idTramite(1L)
                .idUsuario(100L)
                .idVehiculo(200L)
                .tipoTramite(TipoTramite.INGRESO)
                .estadoGlobal(TipoEstado.INGRESADO)
                .build();

        when(tramiteService.listarTodos(null)).thenReturn(List.of(resumen));

        mockMvc.perform(get("/api/v1/tramites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTramite").value(1))
                .andExpect(jsonPath("$[0].idUsuario").value(100))
                .andExpect(jsonPath("$[0].idVehiculo").value(200));
    }

    @Test
    @DisplayName("GET /api/v1/tramites/{id} debe devolver trámite por id")
    void obtenerTramite_PorId_DeberiaRetornarDto() throws Exception {
        TramiteResponseDTO response = TramiteResponseDTO.builder()
                .idTramite(1L)
                .idUsuario(100L)
                .idVehiculo(200L)
                .tipoTramite(TipoTramite.INGRESO)
                .estadoGlobal(TipoEstado.INGRESADO)
                .build();

        when(tramiteService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/tramites/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTramite").value(1))
                .andExpect(jsonPath("$.idUsuario").value(100))
                .andExpect(jsonPath("$.idVehiculo").value(200));
    }

    @Test
    @DisplayName("POST /api/v1/tramites debe crear un trámite")
    void crearTramite_DeberiaRetornarCreated() throws Exception {
        PasajeroRequestDTO pasajero = PasajeroRequestDTO.builder()
                .tipoDocumento("DNI")
                .nroDocumento("12345678")
                .nacionalidad("CHILE")
                .fechaNacimiento(LocalDate.parse("1990-01-01"))
                .build();

        TramiteRequestDTO request = TramiteRequestDTO.builder()
                .idUsuario(100L)
                .idVehiculo(200L)
                .tipoTramite(TipoTramite.INGRESO)
                .pasajeros(List.of(pasajero))
                .build();

        TramiteResponseDTO response = TramiteResponseDTO.builder()
                .idTramite(1L)
                .idUsuario(100L)
                .idVehiculo(200L)
                .tipoTramite(TipoTramite.INGRESO)
                .estadoGlobal(TipoEstado.INGRESADO)
                .build();

        when(tramiteService.crearNuevoTramite(any(TramiteRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/tramites")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idTramite").value(1))
                .andExpect(jsonPath("$.estadoGlobal").value("INGRESADO"));
    }

    @Test
    @DisplayName("PATCH /api/v1/tramites/{id}/cancelar debe cancelar el trámite")
    void cancelarTramite_DeberiaRetornarEstado() throws Exception {
        EstadoTramiteResponseDTO estadoResponse = EstadoTramiteResponseDTO.builder()
                .tipoEstado(TipoEstado.CANCELADO_POR_USUARIO)
                .comentario("Tramite cancelado por el usuario")
                .build();

        when(tramiteService.cancelarTramite(1L)).thenReturn(estadoResponse);

        mockMvc.perform(patch("/api/v1/tramites/1/cancelar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoEstado").value("CANCELADO_POR_USUARIO"));
    }

    @Test
    @DisplayName("GET /api/v1/tramites/buscar debe buscar por parámetros")
    void buscarTramites_DeberiaRetornarResultados() throws Exception {
        TramiteResumenResponseDTO resumen = TramiteResumenResponseDTO.builder()
                .idTramite(1L)
                .idUsuario(100L)
                .idVehiculo(200L)
                .tipoTramite(TipoTramite.INGRESO)
                .estadoGlobal(TipoEstado.INGRESADO)
                .build();

        when(tramiteService.buscarPorParametros(eq(100L), eq(200L))).thenReturn(List.of(resumen));

        mockMvc.perform(get("/api/v1/tramites/buscar?idUsuario=100&idVehiculo=200"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idTramite").value(1));
    }

    @Test
    @DisplayName("POST /api/v1/tramites/{id}/pasajeros debe agregar pasajero")
    void agregarPasajero_DeberiaRetornarCreated() throws Exception {
        PasajeroRequestDTO request = PasajeroRequestDTO.builder()
                .tipoDocumento("DNI")
                .nroDocumento("87654321")
                .nacionalidad("CHILE")
                .fechaNacimiento(LocalDate.parse("1985-05-10"))
                .build();

        PasajeroResponseDTO response = PasajeroResponseDTO.builder()
                .idPasajero(1L)
                .tipoDocumento("DNI")
                .nroDocumento("87654321")
                .nacionalidad("CHILE")
                .build();

        when(tramiteService.agregarPasajero(eq(1L), any(PasajeroRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/v1/tramites/1/pasajeros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPasajero").value(1))
                .andExpect(jsonPath("$.nroDocumento").value("87654321"));
    }

    @Test
    @DisplayName("DELETE /api/v1/tramites/{id}/pasajeros/{idPasajero} debe eliminar pasajero")
    void quitarPasajero_DeberiaRetornarMensaje() throws Exception {
        when(tramiteService.quitarPasajero(1L, 2L)).thenReturn("Pasajero Eliminado de forma exitosa");

        mockMvc.perform(delete("/api/v1/tramites/1/pasajeros/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Pasajero Eliminado de forma exitosa"));
    }
}
