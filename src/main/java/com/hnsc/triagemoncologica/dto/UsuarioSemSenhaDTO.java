package com.hnsc.triagemoncologica.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;

public record UsuarioSemSenhaDTO(UUID id, String nomeCompleto, @Email String email) {
}
