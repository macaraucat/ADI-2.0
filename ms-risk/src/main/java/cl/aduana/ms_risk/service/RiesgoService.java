package cl.aduana.ms_risk.service;

import java.util.List;

import org.springframework.stereotype.Service;

import cl.aduana.common.client.TravelClient;
import cl.aduana.common.dto.request.EvaluacionRiesgoRequestDTO;
import cl.aduana.common.dto.response.EvaluacionRiesgoResponseDTO;
import cl.aduana.common.dto.response.TramiteClientResponseDTO;
import cl.aduana.common.exception.BadRequestException;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_risk.repository.PatenteInfraccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class RiesgoService {

    private final PatenteInfraccionRepository patenteRepo;
    private final TravelClient travelClient;

    public EvaluacionRiesgoResponseDTO evaluar(EvaluacionRiesgoRequestDTO req) {
        if (req.getNroDocumentoSujeto() == null || req.getPatente() == null) {
            throw new BadRequestException("Documento y patente son obligatorios");
        }

        log.info("Evaluando riesgo tramite={}, doc={}, patente={}", 
            req.getIdTramite(), req.getNroDocumentoSujeto(), req.getPatente());

        // Validar que el trámite existe
        TramiteClientResponseDTO tramite = travelClient.obtenerTramitePorId(req.getIdTramite());
        if (tramite == null) throw new ResourceNotFoundException("Trámite no encontrado: " + req.getIdTramite());
        log.info("Trámite encontrado: id={}, estado={}", tramite.getIdTramite(), tramite.getEstadoGlobal());
        log.info("Evaluando riesgo tramite={}, doc={}, patente={}", req.getIdTramite(), req.getNroDocumentoSujeto(), req.getPatente());

        String nivel = calcularNivel(req.getNroDocumentoSujeto());

        // acompañantes
        List<String> acomp = req.getNroDocumentoAcompanantes();
        if (acomp != null) {
            for (String doc : acomp) {
                String n = calcularNivel(doc);
                nivel = maxNivel(nivel, n);
            }
        }

        // patente con infracciones -> sube a MEDIO mínimo
        if (patenteRepo.existsById(req.getPatente().toUpperCase())) {
            log.warn("Patente con historial: {}", req.getPatente());
            nivel = maxNivel(nivel, "MEDIO");
        }

        EvaluacionRiesgoResponseDTO resp = new EvaluacionRiesgoResponseDTO();
        resp.setNivelRiesgo(nivel);
        resp.setDetalle("Evaluación automática completada");
        return resp;
    }

    // Regla original: 1-4 BAJO, 5-7 MEDIO, 8-9 ALTO (por primer dígito)
    private String calcularNivel(String doc) {
        if (doc == null || doc.isEmpty()) return "BAJO";
        char c = doc.charAt(0);
        if (c >= '8') return "ALTO";
        if (c >= '5') return "MEDIO";
        return "BAJO";
    }

    private String maxNivel(String a, String b) {
        if ("ALTO".equals(a) || "ALTO".equals(b)) return "ALTO";
        if ("MEDIO".equals(a) || "MEDIO".equals(b)) return "MEDIO";
        return "BAJO";
    }
}