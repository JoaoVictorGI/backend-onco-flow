package com.hnsc.triagemoncologica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUsuarioRequestDTO(@NotBlank @Email String email, @NotBlank String senha) {
}
