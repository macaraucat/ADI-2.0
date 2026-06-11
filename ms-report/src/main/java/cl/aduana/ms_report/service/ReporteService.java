package cl.aduana.ms_report.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import cl.aduana.common.client.*;
import cl.aduana.common.exception.BadRequestException;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_report.model.*;
import cl.aduana.ms_report.repository.ReporteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @RequiredArgsConstructor @Slf4j
public class ReporteService {
    private final ReporteRepository repo;
    private final UsuarioClient usuarioClient;
    private final VehiculoClient vehiculoClient;
    private final TravelClient travelClient;
    private final RiskClient riskClient;

    public List<Reporte> listar() {
        log.debug("Listando todos los reportes");
        List<Reporte> reportes = repo.findAll();
        log.debug("Total de reportes encontrados: {}", reportes.size());
        return reportes;
    }

    public Reporte obtener(Long id) {
        log.debug("Buscando reporte por ID: {}", id);
        return repo.findById(id).orElseThrow(() -> {
                log.warn("Reporte no encontrado - ID: {}", id);
                return new ResourceNotFoundException("Reporte id " + id + " no encontrado");
            });
    }

    @Transactional
    public Reporte generar(TipoReporte tipo, String generadoPor) {
        log.info("Iniciando generación de reporte - Tipo: {}, Generado por: {}", tipo, generadoPor);
        long inicioTiempo = System.currentTimeMillis();
        
        if (tipo == null) {
            log.error("Tipo de reporte nulo");
            throw new BadRequestException("Tipo obligatorio");
        }
        
        int total = 0;
        try {
            switch(tipo) {
                case USUARIOS:
                    log.debug("Consultando ms-login para obtener usuarios...");
                    List<?> usuarios = usuarioClient.listarTodos();
                    total = usuarios.size();
                    log.debug("Usuarios obtenidos: {} registros", total);
                    break;
                    
                case VEHICULOS:
                    log.debug("Consultando ms-vehiculo para obtener vehículos...");
                    List<?> vehiculos = vehiculoClient.listarTodos();
                    total = vehiculos.size();
                    log.debug("Vehículos obtenidos: {} registros", total);
                    break;
                    
                case TRAMITES:
                    log.debug("Consultando ms-travel para obtener trámites...");
                    List<?> tramites = travelClient.listarTodos();
                    total = tramites.size();
                    log.debug("Trámites obtenidos: {} registros", total);
                    break;
                    
                case ALERTAS:
                    log.debug("Consultando ms-risk para obtener alertas...");
                    List<?> alertas = riskClient.listarAlertas();
                    total = alertas.size();
                    log.debug("Alertas obtenidas: {} registros", total);
                    break;
            }
            
            log.debug("Procesando datos para reporte de tipo: {}", tipo);
            
            Reporte reporte = repo.save(Reporte.builder()
                .tipo(tipo)
                .fechaGeneracion(LocalDateTime.now())
                .totalRegistros(total)
                .generadoPor(generadoPor)
                .formato("JSON")
                .build());
            
            long tiempoTranscurrido = System.currentTimeMillis() - inicioTiempo;
            log.info("Reporte generado exitosamente - ID: {}, Tipo: {}, Registros: {}, Tiempo: {}ms", 
                reporte.getId(), tipo, total, tiempoTranscurrido);
            
            return reporte;
            
        } catch (Exception e) {
            long tiempoTranscurrido = System.currentTimeMillis() - inicioTiempo;
            log.error("Error al generar reporte de tipo {} - Razón: {} - Tiempo: {}ms", 
                tipo, e.getMessage(), tiempoTranscurrido);
            throw e;
        }
    }
    
    public void eliminar(Long id) {
        log.warn("Eliminando reporte: {}", id);
        
        if (!repo.existsById(id)) {
            log.warn("Reporte no encontrado para eliminar - ID: {}", id);
            throw new ResourceNotFoundException("Reporte id " + id + " no encontrado");
        }
        
        repo.deleteById(id);
        log.info("Reporte eliminado correctamente - ID: {}", id);
    }
}