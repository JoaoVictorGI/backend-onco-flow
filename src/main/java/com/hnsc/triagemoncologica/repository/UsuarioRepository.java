package com.hnsc.triagemoncologica.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hnsc.triagemoncologica.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByNomeCompleto(String nomeCompleto);
}
