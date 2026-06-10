package cl.aduana.ms_login.mapper;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

import cl.aduana.common.dto.response.UsuarioClientResponseDTO;
import cl.aduana.ms_login.dto.LoginResponseDTO;
import cl.aduana.ms_login.dto.UsuarioDTO;
import cl.aduana.ms_login.model.Rol;
import cl.aduana.ms_login.model.Usuario;
import lombok.extern.slf4j.Slf4j;

@Component @Slf4j
public class UsuarioMapper {
    public Usuario toUsuario(UsuarioDTO dto) {
        Usuario u = new Usuario();
        u.setTipoDocumento(dto.getTipoDocumento());
        u.setNroDocumento(dto.getNroDocumento());
        u.setNombreCompleto(dto.getNombreCompleto());
        u.setEmail(dto.getEmail());
        u.setPassword(dto.getPassword());
        return u;
    }

    public LoginResponseDTO toLoginResponse(Usuario usuario) {
        var roles = usuario.getRoles().stream().map(Rol::getCodigo).collect(Collectors.toSet());
        return new LoginResponseDTO(
            usuario.getId(),
            usuario.getTipoDocumento(),
            usuario.getNroDocumento(),
            usuario.getNombreCompleto(),
            usuario.getEmail(),
            usuario.getOrganismo(),
            roles
        );
    }

    public UsuarioClientResponseDTO toClientResponse(Usuario usuario) {
        UsuarioClientResponseDTO dto = new UsuarioClientResponseDTO();
        dto.setNombreCompleto(usuario.getNombreCompleto());
        dto.setTipoDocumento(usuario.getTipoDocumento());
        dto.setNroDocumento(usuario.getNroDocumento());
        dto.setEmail(usuario.getEmail());
        return dto;
    }   
}