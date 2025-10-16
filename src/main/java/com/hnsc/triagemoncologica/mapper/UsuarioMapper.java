package com.hnsc.triagemoncologica.mapper;

import com.hnsc.triagemoncologica.dto.RegistroUsuarioResponseDTO;
import com.hnsc.triagemoncologica.dto.UsuarioSemSenhaDTO;
import com.hnsc.triagemoncologica.model.Usuario;

public class UsuarioMapper {
    public static UsuarioSemSenhaDTO paraUsuarioSemSenhaDto(Usuario usuario) {
        return new UsuarioSemSenhaDTO(usuario.getId(), usuario.getNomeCompleto(), usuario.getEmail(), usuario.getRole());
    }

    public static RegistroUsuarioResponseDTO paraRegistroUsuarioResponseDto(Usuario usuario) {
        return new RegistroUsuarioResponseDTO(usuario.getNomeCompleto(), usuario.getEmail(), usuario.getRole());
    }
}
