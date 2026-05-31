package cl.aduana.ms_travel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.aduana.ms_travel.model.EstadoTramite;

@Repository
public interface EstadoTramiteRepository extends JpaRepository<EstadoTramite, Long> {}