package cl.aduana.ms_login.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cl.aduana.common.dto.response.UsuarioClientResponseDTO;
import cl.aduana.ms_login.mapper.UsuarioMapper;
import cl.aduana.ms_login.model.Usuario;
import cl.aduana.ms_login.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    LoginService service;
    @MockitoBean
    UsuarioMapper mapper;

    @Test
    void crearUsuario() throws Exception {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNroDocumento("12345678-9");
        when(service.registrarUsuario(any())).thenReturn(u);
        mockMvc.perform(post("/api/v1/login/usuarios").contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"tipoDocumento\":\"RUN\",\"nroDocumento\":\"12345678-9\",\"nombreCompleto\":\"Test\",\"email\":\"a@a.cl\",\"password\":\"12345678\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void crearFuncionario() throws Exception {
        Usuario u = new Usuario();
        u.setId(2L);
        u.setOrganismo("ADUANA");
        when(service.registrarFuncionario(any())).thenReturn(u);
        mockMvc.perform(post("/api/v1/login/usuarios/funcionarios").contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"tipoDocumento\":\"RUN\",\"nroDocumento\":\"22345678-9\",\"nombreCompleto\":\"Func\",\"email\":\"f@aduana.cl\",\"password\":\"12345678\",\"organismo\":\"ADUANA\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void login() throws Exception {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNroDocumento("12345678-9");
        when(service.autenticar(any(), any())).thenReturn(u);
        when(service.crearLoginResponse(any())).thenReturn(null);
        mockMvc.perform(post("/api/v1/login").contentType(MediaType.APPLICATION_JSON)
                .content("{\"nroDocumento\":\"12345678-9\",\"password\":\"MiClave123\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void listar() throws Exception {
        when(service.listarUsuarios()).thenReturn(List.of());
        mockMvc.perform(get("/api/v1/login/usuarios")).andExpect(status().isOk());
    }

    @Test
    void obtener() throws Exception {
        when(service.obtenerPorId(1L)).thenReturn(new Usuario());
        when(mapper.toClientResponse(any())).thenReturn(new UsuarioClientResponseDTO());
        mockMvc.perform(get("/api/v1/login/usuarios/1")).andExpect(status().isOk());
    }

    @Test
    void actualizar() throws Exception {
        when(service.actualizarUsuario(any(), any())).thenReturn(new Usuario());
        when(mapper.toClientResponse(any())).thenReturn(new UsuarioClientResponseDTO());
        mockMvc.perform(put("/api/v1/login/usuarios/1").contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{\"tipoDocumento\":\"RUN\",\"nroDocumento\":\"12345678-9\",\"nombreCompleto\":\"Act\",\"email\":\"a@a.cl\",\"password\":\"12345678\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void desactivar() throws Exception {
        mockMvc.perform(delete("/api/v1/login/usuarios/1")).andExpect(status().isNoContent());
    }
}