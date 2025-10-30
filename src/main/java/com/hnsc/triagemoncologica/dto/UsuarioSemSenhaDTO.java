package com.hnsc.triagemoncologica.dto;

import com.hnsc.triagemoncologica.enums.Role;
import jakarta.validation.constraints.Email;
import java.util.UUID;

public record UsuarioSemSenhaDTO(UUID id, String nomeCompleto, @Email String email, Role role) {}
