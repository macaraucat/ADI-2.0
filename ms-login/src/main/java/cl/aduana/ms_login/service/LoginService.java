package cl.aduana.ms_login.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.aduana.common.exception.BadRequestException;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_login.dto.*;
import cl.aduana.ms_login.mapper.UsuarioMapper;
import cl.aduana.ms_login.model.Rol;
import cl.aduana.ms_login.model.Usuario;
import cl.aduana.ms_login.repository.RolRepository;
import cl.aduana.ms_login.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service @RequiredArgsConstructor @Slf4j
public class LoginService {
    private final UsuarioRepository repo;
    private final RolRepository rolRepo;
    private final UsuarioMapper mapper;

    public Usuario autenticar(String doc, String password) {
        log.info("Iniciando autenticación para documento: {}", doc);
        
        Usuario u = repo.findByNroDocumentoAndActivoTrue(doc).orElseThrow(() -> {
                    log.warn("Usuario no encontrado o inactivo: {}", doc);
                    return new ResourceNotFoundException("Usuario no encontrado o inactivo");
                });
        
        if (!u.getPassword().equals(password)) {
            log.warn("Contraseña incorrecta para usuario: {}", doc);
            throw new BadRequestException("Contraseña inválida");
        }
        
        u.setUltimoAcceso(LocalDateTime.now());
        Usuario autenticado = repo.save(u);
        log.info("Autenticación exitosa - Usuario: {}, ID: {}, Último acceso actualizado", doc, autenticado.getId());
        
        return autenticado;
    }

    @Transactional
    public Usuario registrarUsuario(UsuarioDTO dto) {
        log.info("Registrando nuevo PASAJERO - Documento: {}, Email: {}", dto.getNroDocumento(), dto.getEmail());
        
        validarNuevo(dto);
        
        Usuario u = mapper.toUsuario(dto);
        u.setOrganismo("PASAJERO");
        u.setActivo(true);
        u.setFechaCreacion(LocalDateTime.now());
        
        Rol rol = rolRepo.findByCodigo("PASAJERO").orElseThrow(() -> {
                    log.error("Error crítico: Rol PASAJERO no configurado en BD");
                    return new ResourceNotFoundException("Rol PASAJERO no configurado");
                });
        
        u.getRoles().add(rol);
        Usuario guardado = repo.save(u);
        
        log.info("Pasajero registrado exitosamente - ID: {}, Doc: {}, Rol: PASAJERO", 
            guardado.getId(), guardado.getNroDocumento());
        
        return guardado;
    }

    @Transactional
    public Usuario registrarFuncionario(FuncionarioDTO dto) {
        log.info("Registrando nuevo FUNCIONARIO - Documento: {}, Organismo: {}", 
            dto.getNroDocumento(), dto.getOrganismo());
        
        validarNuevo(dto);
        
        if (!Set.of("ADUANA", "PDI", "SAG").contains(dto.getOrganismo())) {
            log.warn("Organismo inválido para funcionario {}: {}", dto.getNroDocumento(), dto.getOrganismo());
            throw new BadRequestException("Organismo debe ser ADUANA, PDI o SAG");
        }
        
        Usuario u = mapper.toUsuario(dto);
        u.setOrganismo(dto.getOrganismo());
        u.setActivo(true);
        u.setFechaCreacion(LocalDateTime.now());

        String codigoRol = switch (dto.getOrganismo()) {
            case "ADUANA" -> "AGENTE_ADUANA";
            case "PDI" -> "AGENTE_PDI";
            case "SAG" -> "AGENTE_SAG";
            default -> throw new BadRequestException("Organismo no soportado");
        };
        
        log.debug("Asignando rol: {} para organismo: {}", codigoRol, dto.getOrganismo());
        
        Rol rol = rolRepo.findByCodigo(codigoRol)
                .orElseThrow(() -> {
                    log.error("Error: Rol {} no configurado en BD", codigoRol);
                    return new ResourceNotFoundException("Rol no configurado: " + codigoRol);
                });
        
        u.getRoles().add(rol);
        Usuario guardado = repo.save(u);
        
        log.info("Funcionario registrado - ID: {}, Doc: {}, Organismo: {}, Rol: {}", 
            guardado.getId(), guardado.getNroDocumento(), guardado.getOrganismo(), codigoRol);
        
        return guardado;
    }

    public List<Usuario> listarUsuarios() {
        log.debug("Listando todos los usuarios");
        List<Usuario> usuarios = repo.findAll();
        log.debug("Total de usuarios en BD: {}", usuarios.size());
        return usuarios;
    }

    public Usuario obtenerPorId(Long id) {
        log.debug("Buscando usuario por ID: {}", id);
        return repo.findById(id)
            .orElseThrow(() -> {
                log.warn("Usuario no encontrado - ID: {}", id);
                return new ResourceNotFoundException("Usuario id " + id + " no encontrado");
            });
    }

    public Usuario obtenerPorDocumento(String doc) {
        log.debug("🔍 Buscando usuario por documento: {}", doc);
        return repo.findByNroDocumento(doc)
            .orElseThrow(() -> {
                log.warn("Usuario no encontrado - Documento: {}", doc);
                return new ResourceNotFoundException("Usuario " + doc + " no encontrado");
            });
    }

    @Transactional
    public Usuario actualizarUsuario(Long id, UsuarioDTO dto) {
        log.info("✏️  Actualizando usuario ID: {}", id);
        Usuario u = obtenerPorId(id);
        
        if (dto.getEmail() != null && !dto.getEmail().equals(u.getEmail()) && repo.existsByEmail(dto.getEmail())) {
            log.warn("Email ya registrado: {} (intento de update en usuario {})", dto.getEmail(), id);
            throw new BadRequestException("Email ya registrado");
        }
        
        u.setTipoDocumento(dto.getTipoDocumento());
        u.setNombreCompleto(dto.getNombreCompleto());
        u.setEmail(dto.getEmail());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            u.setPassword(dto.getPassword());
            log.debug("Contraseña actualizada para usuario: {}", id);
        }
        
        Usuario actualizado = repo.save(u);
        log.info("✅ Usuario actualizado - ID: {}, NombreCompleto: {}, Email: {}", 
            actualizado.getId(), actualizado.getNombreCompleto(), actualizado.getEmail());
        
        return actualizado;
    }

    @Transactional
    public void desactivarUsuario(Long id) {
        log.warn("Desactivando usuario: {}", id);
        Usuario u = obtenerPorId(id);
        u.setActivo(false);
        repo.save(u);
        log.info("Usuario desactivado - ID: {}, Documento: {}", id, u.getNroDocumento());
    }

    public Set<String> obtenerPermisos(String doc) {
        log.debug("Obteniendo permisos para usuario: {}", doc);
        Usuario u = obtenerPorDocumento(doc);
        Set<String> permisos = u.getRoles().stream()
            .flatMap(r -> r.getPermisos().stream())
            .map(p -> p.getCodigo())
            .collect(Collectors.toSet());
        log.debug("Permisos encontrados para {}: {} permisos", doc, permisos.size());
        return permisos;
    }

    public boolean validarPermiso(String doc, String permiso) {
        log.debug("Validando permiso '{}' para usuario: {}", permiso, doc);
        boolean tienePermiso = obtenerPermisos(doc).contains(permiso);
        if (tienePermiso) {
            log.debug("Usuario {} TIENE permiso: {}", doc, permiso);
        } else {
            log.warn("Usuario {} NO TIENE permiso: {}", doc, permiso);
        }
        return tienePermiso;
    }

    public LoginResponseDTO crearLoginResponse(Usuario usuario) {
        return mapper.toLoginResponse(usuario);
    }

    private void validarNuevo(UsuarioDTO dto) {
        log.debug("Validando nuevo usuario - Doc: {}, Email: {}", dto.getNroDocumento(), dto.getEmail());
        
        if (repo.existsByNroDocumento(dto.getNroDocumento())) {
            log.warn("RUT ya registrado: {}", dto.getNroDocumento());
            throw new BadRequestException("RUT ya registrado");
        }
        
        if (dto.getEmail() != null && repo.existsByEmail(dto.getEmail())) {
            log.warn("Email ya registrado: {}", dto.getEmail());
            throw new BadRequestException("Email ya registrado");
        }
        
        log.debug("Validación de nuevo usuario exitosa");
    }
}