package cl.aduana.ms_login.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.aduana.ms_login.dto.*;
import cl.aduana.ms_login.model.Usuario;
import cl.aduana.ms_login.service.LoginService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
@Slf4j
public class LoginController {

    private final LoginService service;

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO req) {
        log.info("🔐 POST /login - Intento de autenticación para usuario: {}", req.getNroDocumento());
        try {
            Usuario u = service.autenticar(req.getNroDocumento(), req.getPassword());
            log.info("Autenticación exitosa para usuario: {} (id={})", u.getNroDocumento(), u.getId());
            return ResponseEntity.ok(service.crearLoginResponse(u));
        } catch (Exception e) {
            log.warn("Fallo de autenticación para usuario: {} - Razón: {}", req.getNroDocumento(), e.getMessage());
            throw e;
        }
    }

    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody UsuarioDTO dto) {
        log.info("👤 POST /usuarios - Registrando nuevo usuario: {} ({})", 
            dto.getNroDocumento(), dto.getNombreCompleto());
        try {
            Usuario creado = service.registrarUsuario(dto);
            log.info("Usuario creado exitosamente: id={}, nroDoc={}, email={}", creado.getId(), creado.getNroDocumento(), creado.getEmail());
            return ResponseEntity.status(201).body(Map.of("id", creado.getId(), "nroDocumento", creado.getNroDocumento()));
        } catch (Exception e) {
            log.error("Error al crear usuario {}: {}", dto.getNroDocumento(), e.getMessage());
            throw e;
        }
    }

    @PostMapping("/usuarios/funcionarios")
    public ResponseEntity<?> crearFuncionario(@Valid @RequestBody FuncionarioDTO dto) {
        log.info("POST /usuarios/funcionarios - Registrando funcionario: {} ({}, organismo={})", dto.getNroDocumento(), dto.getNombreCompleto(), dto.getOrganismo());
        try {
            Usuario creado = service.registrarFuncionario(dto);
            log.info("Funcionario creado: id={}, organismo={}, rol asignado", 
                creado.getId(), creado.getOrganismo());
            return ResponseEntity.status(201).body(
                Map.of("id", creado.getId(), "organismo", creado.getOrganismo())
            );
        } catch (Exception e) {
            log.error("Error al crear funcionario {}: {}", dto.getNroDocumento(), e.getMessage());
            throw e;
        }
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<Usuario>> listar() {
        log.debug("GET /usuarios - Listando todos los usuarios");
        List<Usuario> usuarios = service.listarUsuarios();
        log.debug("Total de usuarios encontrados: {}", usuarios.size());
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        log.debug("GET /usuarios/{} - Buscando usuario por ID", id);
        Usuario u = service.obtenerPorId(id);
        log.debug("Usuario encontrado: {} ({})", u.getNroDocumento(), u.getNombreCompleto());
        return ResponseEntity.ok(u);
    }

    @GetMapping("/usuarios/documento/{nroDocumento}")
    public ResponseEntity<Usuario> obtenerPorDocumento(@PathVariable String nroDocumento) {
        log.debug("GET /usuarios/documento/{} - Buscando usuario por documento", nroDocumento);
        Usuario usuario = service.obtenerPorDocumento(nroDocumento);
        log.debug("Usuario encontrado por documento: {} (id={})", nroDocumento, usuario.getId());
        return ResponseEntity.ok(usuario);
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        log.info("✏️  PUT /usuarios/{} - Actualizando usuario: {}", id, dto.getNroDocumento());
        try {
            Usuario actualizado = service.actualizarUsuario(id, dto);
            log.info("Usuario actualizado: id={}, nroDoc={}", actualizado.getId(), actualizado.getNroDocumento());
            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            log.error("Error al actualizar usuario {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        log.warn("DELETE /usuarios/{} - Desactivando usuario", id);
        try {
            service.desactivarUsuario(id);
            log.info("Usuario desactivado: id={}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al desactivar usuario {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @GetMapping("/usuario/{nroDocumento}/permiso")
    public Set<String> listarPermisos(@PathVariable String nroDocumento) {
        log.debug("GET /usuario/{}/permiso - Listando permisos", nroDocumento);
        Set<String> permisos = service.obtenerPermisos(nroDocumento);
        log.debug("Permisos encontrados para {}: {}", nroDocumento, permisos.size());
        return permisos;
    }

    @GetMapping("/usuario/{nroDocumento}/permiso/{permiso}")
    public ResponseEntity<?> tienePermiso(@PathVariable String nroDocumento, @PathVariable String permiso) {
        log.debug("GET /usuario/{}/permiso/{} - Validando permiso", nroDocumento, permiso);
        boolean ok = service.validarPermiso(nroDocumento, permiso);
        if (ok) {
            log.debug("Usuario {} tiene permiso: {}", nroDocumento, permiso);
            return ResponseEntity.ok(true);
        } else {
            log.warn("Usuario {} NO tiene permiso: {}", nroDocumento, permiso);
            return ResponseEntity.status(403).body(false);
        }
    }
}