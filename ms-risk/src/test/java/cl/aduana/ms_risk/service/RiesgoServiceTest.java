package cl.aduana.ms_risk.service;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.aduana.common.client.TravelClient;
import cl.aduana.common.dto.request.EvaluacionRiesgoRequestDTO;
import cl.aduana.common.dto.response.EvaluacionRiesgoResponseDTO;
import cl.aduana.common.dto.response.TramiteClientResponseDTO;
import cl.aduana.common.exception.BadRequestException;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_risk.repository.PatenteInfraccionRepository;

@ExtendWith(MockitoExtension.class)
public class RiesgoServiceTest {

    @Mock
    private PatenteInfraccionRepository patenteRepo;

    @Mock
    private TravelClient travelClient;

    @InjectMocks
    private RiesgoService service;

    @Test
    void givenDatosNulos_whenEvaluar_thenThrowBadRequestException() {
        // GIVEN
        EvaluacionRiesgoRequestDTO req = EvaluacionRiesgoRequestDTO.builder().build();

        // WHEN & THEN
        assertThrows(BadRequestException.class, () -> service.evaluar(req));
    }

    @Test
    void givenTramiteNoExiste_whenEvaluar_thenThrowResourceNotFoundException() {
        // GIVEN
        EvaluacionRiesgoRequestDTO req = EvaluacionRiesgoRequestDTO.builder()
                .nroDocumentoSujeto("12345678")
                .patente("AB1234")
                .idTramite(1L)
                .build();

        when(travelClient.obtenerTramitePorId(1L)).thenReturn(null);

        // WHEN & THEN
        assertThrows(ResourceNotFoundException.class, () -> service.evaluar(req));
    }

    @Test
    void givenDocumentoBajo_whenEvaluar_thenReturnNivelBajo() {
        // GIVEN
        EvaluacionRiesgoRequestDTO req = EvaluacionRiesgoRequestDTO.builder()
                .nroDocumentoSujeto("12345678")
                .patente("AB1234")
                .idTramite(1L)
                .build();

        TramiteClientResponseDTO tramite = new TramiteClientResponseDTO();
        tramite.setIdTramite(1L);
        tramite.setEstadoGlobal("EN_PROCESO");

        when(travelClient.obtenerTramitePorId(1L)).thenReturn(tramite);
        when(patenteRepo.existsById("AB1234")).thenReturn(false);

        // WHEN
        EvaluacionRiesgoResponseDTO resultado = service.evaluar(req);

        // THEN
        assertNotNull(resultado);
        assertEquals("BAJO", resultado.getNivelRiesgo());
    }

    @Test
    void givenDocumentoMedio_whenEvaluar_thenReturnNivelMedio() {
        // GIVEN
        EvaluacionRiesgoRequestDTO req = EvaluacionRiesgoRequestDTO.builder()
                .nroDocumentoSujeto("56789012")
                .patente("AB1234")
                .idTramite(1L)
                .build();

        TramiteClientResponseDTO tramite = new TramiteClientResponseDTO();
        tramite.setIdTramite(1L);
        tramite.setEstadoGlobal("EN_PROCESO");

        when(travelClient.obtenerTramitePorId(1L)).thenReturn(tramite);
        when(patenteRepo.existsById("AB1234")).thenReturn(false);

        // WHEN
        EvaluacionRiesgoResponseDTO resultado = service.evaluar(req);

        // THEN
        assertEquals("MEDIO", resultado.getNivelRiesgo());
    }

    @Test
    void givenDocumentoAlto_whenEvaluar_thenReturnNivelAlto() {
        // GIVEN
        EvaluacionRiesgoRequestDTO req = EvaluacionRiesgoRequestDTO.builder()
                .nroDocumentoSujeto("89999999")
                .patente("AB1234")
                .idTramite(1L)
                .build();

        TramiteClientResponseDTO tramite = new TramiteClientResponseDTO();
        tramite.setIdTramite(1L);
        tramite.setEstadoGlobal("EN_PROCESO");

        when(travelClient.obtenerTramitePorId(1L)).thenReturn(tramite);
        when(patenteRepo.existsById("AB1234")).thenReturn(false);

        // WHEN
        EvaluacionRiesgoResponseDTO resultado = service.evaluar(req);

        // THEN
        assertEquals("ALTO", resultado.getNivelRiesgo());
    }

    @Test
    void givenAcompananteConMayorRiesgo_whenEvaluar_thenReturnNivelMasAlto() {
        // GIVEN
        EvaluacionRiesgoRequestDTO req = EvaluacionRiesgoRequestDTO.builder()
                .nroDocumentoSujeto("12345678") // BAJO
                .patente("AB1234")
                .idTramite(1L)
                .nroDocumentoAcompanantes(List.of("89999999")) // ALTO
                .build();

        TramiteClientResponseDTO tramite = new TramiteClientResponseDTO();
        tramite.setIdTramite(1L);
        tramite.setEstadoGlobal("EN_PROCESO");

        when(travelClient.obtenerTramitePorId(1L)).thenReturn(tramite);
        when(patenteRepo.existsById("AB1234")).thenReturn(false);

        // WHEN
        EvaluacionRiesgoResponseDTO resultado = service.evaluar(req);

        // THEN
        assertEquals("ALTO", resultado.getNivelRiesgo());
    }

    @Test
    void givenPatenteConInfracciones_whenEvaluar_thenReturnNivelMedioMinimo() {
        // GIVEN
        EvaluacionRiesgoRequestDTO req = EvaluacionRiesgoRequestDTO.builder()
                .nroDocumentoSujeto("12345678") // BAJO
                .patente("ab1234")
                .idTramite(1L)
                .build();

        TramiteClientResponseDTO tramite = new TramiteClientResponseDTO();
        tramite.setIdTramite(1L);
        tramite.setEstadoGlobal("EN_PROCESO");

        when(travelClient.obtenerTramitePorId(1L)).thenReturn(tramite);
        when(patenteRepo.existsById("AB1234")).thenReturn(true);

        // WHEN
        EvaluacionRiesgoResponseDTO resultado = service.evaluar(req);

        // THEN
        assertEquals("MEDIO", resultado.getNivelRiesgo());
    }
}