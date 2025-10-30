package com.hnsc.triagemoncologica.controller;

import com.hnsc.triagemoncologica.model.Paciente;
import com.hnsc.triagemoncologica.service.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Tag(name = "Paciente", description = "Endpoints para gerenciamento de pacientes")
@RestController
@RequestMapping("/api/paciente")
public class PacienteController {

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @Operation(summary = "Cria um novo paciente")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Paciente criado com sucesso"),
    })
    @PostMapping
    public ResponseEntity<Paciente> create(@RequestBody Paciente paciente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(paciente));
    }

    @Operation(summary = "Busca um paciente pelo seu ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paciente encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> findById(@PathVariable UUID id) {
        Paciente paciente = service.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Paciente com ID: " + id + " não encontrado"));

        return ResponseEntity.ok(paciente);
    }

    @Operation(summary = "Lista todos os pacientes com paginação")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")})
    @GetMapping
    public ResponseEntity<Page<Paciente>> findAll(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(service.findAll(pageable));
    }

    @Operation(summary = "Lista todos os pacientes com paginação e ordenados por prioridade")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")})
    @GetMapping("/prioridade")
    public ResponseEntity<Page<Paciente>> findAllOrderByPrioridade(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(service.findAllOrderByPrioridade(pageable));
    }

    @Operation(summary = "Busca pacientes por nome")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de pacientes com {nome} encontrado com sucesso")
    })
    @GetMapping("/nome/{nome}")
    public ResponseEntity<Page<Paciente>> findByNomeOrderByPrioridadeAsc(
            @PathVariable String nome,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(service.findByNomeOrderByPrioridadeAsc(nome, pageable));
    }

    @Operation(summary = "Deleta um paciente pelo seu ID")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Paciente deletado com sucesso")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}
