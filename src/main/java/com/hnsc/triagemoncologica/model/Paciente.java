package com.hnsc.triagemoncologica.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnsc.triagemoncologica.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = """
        update paciente
        set dt_deletado = now()
        where id = ?
        """)
@SQLRestriction("dt_deletado is null")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nomeCompleto;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate dtNascimento;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Sexo sexo;

    @Column(length = 15)
    private Long cartaoSus;

    private String municipioResidencia;

    private Integer distanciaCentroOncologico;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private TipoCancer tipoCancer;

    @Min(0) @Max(4) private Integer tnm; // Estadiamento - estágio do câncer

    /*
     * # Capacidade funcional do paciente
     *
     * - ECOG 0 - Totalmente ativo
     * - ECOG 1 - Sintomas leves
     * - ECOG 2 - Sintomas moderados
     * - ECOG 3 - Sintomas graves
     * - ECOG 4 - Totalmente incapacitado
     */
    @Min(0) @Max(4) private Integer ecog;

    private Boolean presencaMetastase;

    private Boolean comorbidadesRelevantes;

    private Boolean dorIntensa;

    private Boolean sangramentoAtivo;

    // Perda de perso maior que 10%
    private Boolean perdaPeso;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate dtDiagnostico;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private LocalDate dtEncaminhamento;

    /**
     * Exames de imagem faltando
     * <p>
     * True se estiverem faltando exames de imagem. False se não estiverem faltando.
     */
    private Boolean examesImagem;

    /**
     * Biópsia faltando
     * <p>
     * True se estiver faltando biópsia. False se não estiver faltando.
     */
    private Boolean biopsia;

    /**
     * Imuno-histoquimica faltando
     * <p>
     * True se estiver faltando imuno-histoquimica. False se não estiver faltando.
     */
    private Boolean imunoHistoquimica;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private EtapaAtualCuidado etapaAtualCuidado;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private CondicaoSocioeconomica condicaoSocioeconomica;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Escolaridade escolaridade;

    private Boolean cuidadorAusente;

    @Enumerated(EnumType.STRING)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private Prioridade prioridade;

    private Integer pontuacaoGeografica;

    private Integer pontuacaoClinica;

    private Integer pontuacaoAssistencial;

    private Integer pontuacaoSociodemografica;

    private Integer pontuacaoTotal;

    @CreationTimestamp
    private OffsetDateTime dtCriacao;

    private OffsetDateTime dtDeletado;
}
