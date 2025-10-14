package com.hnsc.triagemoncologica.dto;

import jakarta.validation.constraints.Email;

public record UsuarioSemIdESenhaDTO(String nomeCompleto, @Email String email) {

}
