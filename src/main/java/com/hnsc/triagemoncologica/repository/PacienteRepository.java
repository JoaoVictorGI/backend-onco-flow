package com.hnsc.triagemoncologica.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.NativeQuery;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hnsc.triagemoncologica.model.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, UUID> {
        Page<Paciente> findAllByOrderByPrioridadeAsc(Pageable pageable);

        // https://www.youtube.com/watch?v=2kARdnzB5Gg
        // https://github.com/g-stone7/fuzzy-search-with-postgres-trigram/blob/main/Spring_IO_2025_Fuzzy-Search-with-PostgreSQL-and-Spring-Data.pdf
        @NativeQuery("""
                        select *
                        from paciente p
                        where :nome <% nome_completo
                        order by prioridade
                        """)
        Page<Paciente> findByNomeOrderByPrioridadeAsc(
                        @Param("nome") String nomeCompleto,
                        Pageable pageable);
}
