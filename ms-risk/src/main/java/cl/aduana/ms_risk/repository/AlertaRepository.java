package cl.aduana.ms_risk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.aduana.ms_risk.model.Alerta;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {}