package cl.aduana.ms_risk.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.aduana.ms_risk.model.PatenteInfraccion;
import cl.aduana.ms_risk.repository.PatenteInfraccionRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/patentes-infraccion")
@RequiredArgsConstructor
public class PatenteInfraccionController {

    private final PatenteInfraccionRepository repository;

    @GetMapping
    public ResponseEntity<List<PatenteInfraccion>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }
}