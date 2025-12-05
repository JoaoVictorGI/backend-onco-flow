package com.hnsc.triagemoncologica.dto;

import java.time.Instant;
import java.util.Set;
import org.springframework.lang.NonNull;

public record GerarPacienteDTO(@NonNull Set<Pessoa> results) {
    public record Pessoa(String gender, Nome name, DtNascimento dob) {}

    public record Nome(String first, String last) {}

    public record DtNascimento(Instant date) {}
}
