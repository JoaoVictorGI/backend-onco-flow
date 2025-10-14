package com.hnsc.triagemoncologica.dto;

import com.hnsc.triagemoncologica.enums.Role;

public record RegistroUsuarioResponseDTO(String nomeCompleto, String email, Role role) {
}
