package cl.aduana.ms_risk.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.aduana.ms_risk.model.PatenteInfraccion;
import cl.aduana.ms_risk.repository.PatenteInfraccionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/patentes-infraccion")
@RequiredArgsConstructor
@Tag(name = "Patentes Infracción", description = "Gestión de patentes con infracción en el sistema ADI")
public class PatenteInfraccionController {

    private final PatenteInfraccionRepository repository;

    @Operation(summary = "Listar todas las patentes con infracción")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<PatenteInfraccion>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }
}