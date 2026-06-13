package cl.aduana.ms_login.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import cl.aduana.common.dto.response.UsuarioClientResponseDTO;
import cl.aduana.ms_login.dto.*;
import cl.aduana.ms_login.mapper.UsuarioMapper;
import cl.aduana.ms_login.model.Usuario;
import cl.aduana.ms_login.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Autenticación y Usuarios", description = "API para gestión de usuarios y autenticación")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
@Slf4j
public class LoginController {

    private final LoginService service;
    private final UsuarioMapper mapper;

    // AUTENTICACIÓN
    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario basado en su número de documento y contraseña.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login exitoso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas o usuario inactivo"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO req) {
        log.info("POST /login - Intento de autenticación para usuario: {}", req.getNroDocumento());
        try {
            Usuario u = service.autenticar(req.getNroDocumento(), req.getPassword());
            log.info("Autenticación exitosa para usuario: {} (id={})", u.getNroDocumento(), u.getId());
            return ResponseEntity.ok(service.crearLoginResponse(u));
        } catch (Exception e) {
            log.warn("Fallo de autenticación para usuario: {} - Razón: {}", req.getNroDocumento(), e.getMessage());
            throw e;
        }
    }

    // REGISTRO DE USUARIOS
    @Operation(summary = "Registrar nuevo usuario estándar", 
            description = "Crea un nuevo usuario con rol de PASAJERO.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Email o documento ya registrado"),
            @ApiResponse(responseCode = "422", description = "Datos de validación inválidos")
    })
    @PostMapping("/usuarios")
    public ResponseEntity<?> crearUsuario(@Valid @RequestBody UsuarioDTO dto) {
        log.info("POST /usuarios - Registrando nuevo usuario: {} ({})",
                dto.getNroDocumento(), dto.getNombreCompleto());
        try {
            Usuario u = service.registrarUsuario(dto);
            log.info("Usuario creado exitosamente: id={}, nroDoc={}, email={}", u.getId(), u.getNroDocumento(), u.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", u.getId(), "nroDocumento", u.getNroDocumento()));
        } catch (Exception e) {
            log.error("Error al crear usuario {}: {}", dto.getNroDocumento(), e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "Registrar nuevo funcionario", 
            description = "Crea un nuevo usuario con rol de funcionario (FUNC_ADUANA, FUNC_PDI o FUNC_SAG).")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Funcionario creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FuncionarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Organismo inválido o email ya registrado"),
            @ApiResponse(responseCode = "422", description = "Datos de validación inválidos")
    })
    @PostMapping("/usuarios/funcionarios")
    public ResponseEntity<?> crearFuncionario(@Valid @RequestBody FuncionarioDTO dto) {
        log.info("POST /usuarios/funcionarios - Registrando funcionario: {} ({}, organismo={})",
                dto.getNroDocumento(), dto.getNombreCompleto(), dto.getOrganismo());
        try {
            Usuario creado = service.registrarFuncionario(dto);
            log.info("Funcionario creado: id={}, organismo={}, rol asignado",
                    creado.getId(), creado.getOrganismo());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("id", creado.getId(), "organismo", creado.getOrganismo()));
        } catch (Exception e) {
            log.error("Error al crear funcionario {}: {}", dto.getNroDocumento(), e.getMessage());
            throw e;
        }
    }
    
    // GESTIÓN DE USUARIOS
    @Operation(summary = "Listar todos los usuarios", description = "Obtiene la lista completa de usuarios.")
    @ApiResponse(responseCode = "200", description = "Listado exitoso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioClientResponseDTO.class)))
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioClientResponseDTO>> listar() {
        log.debug("GET /usuarios - Listando todos los usuarios");
        List<Usuario> usuarios = service.listarUsuarios();
        
        List<UsuarioClientResponseDTO> dtos = usuarios.stream()
                .map(mapper::toClientResponse)
                .toList();
                
        log.debug("Total de usuarios encontrados: {}", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @Operation(summary = "Obtener usuario por ID", description = "Busca un usuario específico usando su ID.")
    @Parameter(name = "id", description = "ID interno del usuario", example = "1", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioClientResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioClientResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.debug("GET /usuarios/{} - Buscando usuario por ID", id);
        Usuario usuario = service.obtenerPorId(id);
        log.debug("Usuario encontrado: {} ({})", usuario.getNroDocumento(), usuario.getNombreCompleto());
        return ResponseEntity.ok(mapper.toClientResponse(usuario));
    }

    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos de un usuario específico.")
    @Parameter(name = "id", description = "ID del usuario a actualizar", example = "1", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioClientResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado"),
    })
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioClientResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody UsuarioDTO dto) {
        log.info("PUT /usuarios/{} - Actualizando usuario: {}", id, dto.getNroDocumento());
        try {
            Usuario actualizado = service.actualizarUsuario(id, dto);
            log.info("Usuario actualizado: id={}, nroDoc={}", actualizado.getId(), actualizado.getNroDocumento());
            return ResponseEntity.ok(mapper.toClientResponse(actualizado));
        } catch (Exception e) {
            log.error("Error al actualizar usuario {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Operation(summary = "Desactivar usuario", description = "Desactiva un usuario realizando un soft delete.")
    @Parameter(name = "id", description = "ID del usuario a desactivar", example = "1", required = true)
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario desactivado correctamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
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
}