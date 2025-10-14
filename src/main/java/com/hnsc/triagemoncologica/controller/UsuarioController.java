package com.hnsc.triagemoncologica.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hnsc.triagemoncologica.dto.LoginUsuarioRequestDTO;
import com.hnsc.triagemoncologica.dto.LoginUsuarioResponseDTO;
import com.hnsc.triagemoncologica.dto.RegistroUsuarioRequestDTO;
import com.hnsc.triagemoncologica.dto.RegistroUsuarioResponseDTO;
import com.hnsc.triagemoncologica.service.UsuarioService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Usuário", description = "Endpoints para gerenciamento de usuários")
@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso"),
            @ApiResponse(responseCode = "409", description = "E-mail ou nome já em uso")
    })
    @PostMapping("/registro")
    ResponseEntity<RegistroUsuarioResponseDTO> registroUsuario(@RequestBody @Valid RegistroUsuarioRequestDTO req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registroUsuario(req));
    }

    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso"),
            @ApiResponse(responseCode = "401", description = "E-mail ou senha inválidos")
    })
    @PostMapping("/login")
    ResponseEntity<LoginUsuarioResponseDTO> loginUsuario(@RequestBody @Valid LoginUsuarioRequestDTO req) {
        return ResponseEntity.ok(service.loginUsuario(req));
    }
}
