package com.hnsc.triagemoncologica.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hnsc.triagemoncologica.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, UUID> {
    Page<Paciente> findAllByOrderByPrioridadeAsc(Pageable pageable);
}
