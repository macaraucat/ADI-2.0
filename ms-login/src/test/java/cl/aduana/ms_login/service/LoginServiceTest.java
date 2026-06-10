package cl.aduana.ms_login.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.aduana.common.exception.BadRequestException;
import cl.aduana.common.exception.ResourceNotFoundException;
import cl.aduana.ms_login.dto.FuncionarioDTO;
import cl.aduana.ms_login.dto.LoginResponseDTO;
import cl.aduana.ms_login.dto.UsuarioDTO;
import cl.aduana.ms_login.mapper.UsuarioMapper;
import cl.aduana.ms_login.model.Rol;
import cl.aduana.ms_login.model.Usuario;
import cl.aduana.ms_login.repository.RolRepository;
import cl.aduana.ms_login.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UsuarioRepository repo;
    @Mock
    private RolRepository rolRepo;
    @Mock
    private UsuarioMapper mapper;

    @InjectMocks
    private LoginService service;

    @Test
    void autenticar_usuarioActivo_deberiaRetornarUsuario() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNroDocumento("12345678-9");
        u.setPassword("clave123");
        u.setActivo(true);

        when(repo.findByNroDocumentoAndActivoTrue("12345678-9")).thenReturn(Optional.of(u));
        when(repo.save(any(Usuario.class))).thenAnswer(inv -> inv.getArgument(0));
        
        Usuario resultado = service.autenticar("12345678-9", "clave123");
        assertEquals(1L, resultado.getId());
        verify(repo).save(u);
    }

    @Test
    void autenticar_passwordIncorrecta_deberiaLanzarBadRequest() {
        Usuario u = new Usuario();
        u.setPassword("otra");

        when(repo.findByNroDocumentoAndActivoTrue("123"))
                .thenReturn(Optional.of(u));

        assertThrows(BadRequestException.class, () -> service.autenticar("123", "mala"));
    }

    @Test
    void registrarUsuario_nuevo_deberiaCrearPasajeroConRol() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNroDocumento("11111-1"); dto.setEmail("t@t.cl");
        dto.setTipoDocumento("RUN"); dto.setNombreCompleto("Test"); dto.setPassword("12345678");
        
        when(repo.existsByNroDocumento(any())).thenReturn(false);
        when(repo.existsByEmail(any())).thenReturn(false);
        when(mapper.toUsuario(any())).thenReturn(new Usuario());
        when(rolRepo.findByCodigo("PASAJERO")).thenReturn(Optional.of(new Rol()));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        
        Usuario r = service.registrarUsuario(dto);
        assertNotNull(r);
    }

    @Test
    void registrarUsuario_documentoDuplicado_deberiaLanzarBadRequest() {
        when(repo.existsByNroDocumento("dup")).thenReturn(true);
        UsuarioDTO dto = new UsuarioDTO(); dto.setNroDocumento("dup"); dto.setEmail("x@x.cl");
        assertThrows(BadRequestException.class, () -> service.registrarUsuario(dto));
    }

    @Test
    void registrarUsuario_emailDuplicado_deberiaLanzarBadRequest() {
        when(repo.existsByNroDocumento(any())).thenReturn(false);
        when(repo.existsByEmail("dup@x.cl")).thenReturn(true);
        UsuarioDTO dto = new UsuarioDTO(); dto.setNroDocumento("1"); dto.setEmail("dup@x.cl");
        dto.setTipoDocumento("RUN"); dto.setNombreCompleto("X"); dto.setPassword("12345678");
        assertThrows(BadRequestException.class, () -> service.registrarUsuario(dto));
    }


    @Test
    void registrarFuncionario_organismoInvalido_deberiaLanzarBadRequest() {
        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setNroDocumento("1"); dto.setEmail("a@a.cl"); dto.setOrganismo("INVALIDO");
        dto.setTipoDocumento("RUN"); dto.setNombreCompleto("X"); dto.setPassword("12345678");

        when(repo.existsByNroDocumento(any())).thenReturn(false);
        when(repo.existsByEmail(any())).thenReturn(false);
        
        assertThrows(BadRequestException.class, () -> service.registrarFuncionario(dto));
    }    

    @Test
    void registrarFuncionario_aduana_deberiaAsignarRoles() {
        FuncionarioDTO dto = new FuncionarioDTO();
        dto.setTipoDocumento("RUN");
        dto.setNroDocumento("2"); 
        dto.setNombreCompleto("Aduanito");
        dto.setEmail("a@aduana.cl"); 
        dto.setPassword("12345678");
        dto.setOrganismo("ADUANA");

        when(repo.existsByNroDocumento(any())).thenReturn(false);
        when(repo.existsByEmail(any())).thenReturn(false);
        when(mapper.toUsuario(any())).thenReturn(new Usuario());
        when(rolRepo.findByCodigo("FUNC_ADUANA")).thenReturn(Optional.of(new Rol()));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        
        Usuario r = service.registrarFuncionario(dto);
        assertNotNull(r);
        assertEquals("ADUANA", r.getOrganismo());
    }

    @Test
    void registrarFuncionario_pdi_deberiaAsignarRoles() {
        FuncionarioDTO dtoPdi = new FuncionarioDTO();         
        dtoPdi.setTipoDocumento("RUN"); 
        dtoPdi.setNroDocumento("3");
        dtoPdi.setNombreCompleto("Pedei");
        dtoPdi.setEmail("p@pdi.cl");
        dtoPdi.setPassword("12345678");
        dtoPdi.setOrganismo("PDI"); 
        
        when(repo.existsByNroDocumento(any())).thenReturn(false);
        when(repo.existsByEmail(any())).thenReturn(false);
        when(mapper.toUsuario(any())).thenReturn(new Usuario());
        when(rolRepo.findByCodigo("FUNC_PDI")).thenReturn(Optional.of(new Rol()));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        service.registrarFuncionario(dtoPdi);       
    }

    @Test
    void registrarFuncionario_sag_deberiaAsignarRoles() {
        FuncionarioDTO dtoSag = new FuncionarioDTO(); 
        dtoSag.setTipoDocumento("RUN"); 
        dtoSag.setNroDocumento("4");
        dtoSag.setNombreCompleto("Sag"); 
        dtoSag.setEmail("s@sag.cl"); 
        dtoSag.setPassword("12345678");
        dtoSag.setOrganismo("SAG"); 

        when(repo.existsByNroDocumento(any())).thenReturn(false);
        when(repo.existsByEmail(any())).thenReturn(false);
        when(mapper.toUsuario(any())).thenReturn(new Usuario());
        when(rolRepo.findByCodigo("FUNC_SAG")).thenReturn(Optional.of(new Rol()));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        service.registrarFuncionario(dtoSag);    
    }

    @Test
    void registrarFuncionario_rolNoExiste_deberiaLanzarException() {
        FuncionarioDTO dto = new FuncionarioDTO(); 
        dto.setTipoDocumento("RUN");
        dto.setNroDocumento("5"); 
        dto.setNombreCompleto("X");
        dto.setEmail("x@x.cl");
        dto.setPassword("12345678");
        dto.setOrganismo("ADUANA");         

        when(repo.existsByNroDocumento(any())).thenReturn(false);
        when(repo.existsByEmail(any())).thenReturn(false);
        when(mapper.toUsuario(any())).thenReturn(new Usuario());
        when(rolRepo.findByCodigo("FUNC_ADUANA")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> service.registrarFuncionario(dto));
    }

    @Test
    void listarUsuarios_deberiaRetornarLista() {
        when(repo.findAll()).thenReturn(List.of(new Usuario(), new Usuario()));
        assertEquals(2, service.listarUsuarios().size());
    }

    @Test
    void obtenerPorId_noExiste_deberiaLanzar() {
        when(repo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.obtenerPorId(99L));
    }

    @Test
    void actualizarUsuario_emailEnUso_deberiaLanzarBadRequest() {
        Usuario existente = new Usuario(); existente.setId(1L); existente.setEmail("viejo@x.cl");
        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.existsByEmail("nuevo@x.cl")).thenReturn(true);
        
        UsuarioDTO dto = new UsuarioDTO(); dto.setEmail("nuevo@x.cl");
        dto.setTipoDocumento("RUN"); dto.setNombreCompleto("X");
        assertThrows(BadRequestException.class, () -> service.actualizarUsuario(1L, dto));
    }

    @Test
    void actualizarUsuario_exitoso_deberiaActualizar() {
        Usuario u = new Usuario(); u.setId(1L); u.setEmail("old@x.cl");
        when(repo.findById(1L)).thenReturn(Optional.of(u));
        when(repo.existsByEmail("new@x.cl")).thenReturn(false);
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        
        UsuarioDTO dto = new UsuarioDTO(); dto.setEmail("new@x.cl");
        dto.setTipoDocumento("RUN"); dto.setNombreCompleto("X");
        Usuario r = service.actualizarUsuario(1L, dto);
        assertEquals("new@x.cl", r.getEmail());
    }    

    @Test
    void desactivarUsuario_deberiaPonerActivoFalse() {
        Usuario u = new Usuario(); u.setId(1L); u.setActivo(true);
        when(repo.findById(1L)).thenReturn(Optional.of(u));
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));
        service.desactivarUsuario(1L);
        assertFalse(u.isActivo());
    }

    @Test
    void crearLoginResponse_deberiaMapearRoles() {
        Usuario u = new Usuario(); u.setId(5L); u.setRoles(Set.of());
        when(mapper.toLoginResponse(u)).thenReturn(new LoginResponseDTO());
        assertNotNull(service.crearLoginResponse(u));
    }

}