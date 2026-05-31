package cl.aduana.ms_login.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.aduana.ms_login.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByNroDocumento(String nroDoc);
    boolean existsByEmail(String email);
    boolean existsByNroDocumento(String nroDoc);
    Optional<Usuario> findByNroDocumentoAndActivoTrue(String nroDoc);
}