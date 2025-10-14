package com.hnsc.triagemoncologica.dto;

import com.hnsc.triagemoncologica.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistroUsuarioRequestDTO(
        @NotBlank(message = "Nome completo é obrigatório") String nomeCompleto,
        @NotBlank(message = "E-mail completo é obrigatório") @Email(message = "E-mail inválido") String email,
        @NotBlank(message = "Senha é obrigatória") String senha,
        @NotBlank(message = "Função é obrigatória") Role role) {
}
