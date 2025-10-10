package com.hnsc.triagemoncologica.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hnsc.triagemoncologica.enums.CondicaoSocioeconomica;
import com.hnsc.triagemoncologica.enums.Escolaridade;
import com.hnsc.triagemoncologica.enums.EtapaAtualCuidado;
import com.hnsc.triagemoncologica.enums.Prioridade;
import com.hnsc.triagemoncologica.enums.Sexo;
import com.hnsc.triagemoncologica.enums.TipoCancer;

@Entity
public class Paciente {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	private String nomeCompleto;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate dtNascimento;

	@Enumerated(EnumType.STRING)
	private Sexo sexo;

	@Column(length = 15)
	private Long cartaoSus;

	private String municipioResidencia;

	private Integer distanciaCentroOncologico;

	@Enumerated(EnumType.STRING)
	private TipoCancer tipoCancer;

	@Min(0)
	@Max(4)
	private Integer tnm; // Estadiamento - estágio do câncer

	/*
	 * # Capacidade funcional do paciente
	 * 
	 * - ECOG 0 - Totalmente ativo
	 * - ECOG 1 - Sintomas leves
	 * - ECOG 2 - Sintomas moderados
	 * - ECOG 3 - Sintomas graves
	 * - ECOG 4 - Totalmente incapacitado
	 */
	@Min(0)
	@Max(4)
	private Integer ecog;

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
	 * 
	 * True se estiverem faltando exames de imagem.
	 * False se não estiverem faltando.
	 */
	private Boolean examesImagem;

	/**
	 * Biópsia faltando
	 * 
	 * True se estiver faltando biópsia.
	 * False se não estiver faltando.
	 */
	private Boolean biopsia;

	/**
	 * Imuno-histoquimica faltando
	 * 
	 * True se estiver faltando imuno-histoquimica.
	 * False se não estiver faltando.
	 */
	private Boolean imunoHistoquimica;

	@Enumerated(EnumType.STRING)
	private EtapaAtualCuidado etapaAtualCuidado;

	@Enumerated(EnumType.STRING)
	private CondicaoSocioeconomica condicaoSocioeconomica;

	@Enumerated(EnumType.STRING)
	private Escolaridade escolaridade;

	private Boolean cuidadorAusente;

	private Prioridade prioridade;
	
	private Integer pontuacaoGeografica;
	
	private Integer pontuacaoClinica;
	
	private Integer pontuacaoAssistencial;
	
	private Integer pontuacaoSociodemografica;

	private Integer pontuacaoTotal;

	public Paciente() {
	}

	public Paciente(UUID id, String nomeCompleto, LocalDate dtNascimento, Sexo sexo, Long cartaoSus,
			String municipioResidencia, Integer distanciaCentroOncologico, TipoCancer tipoCancer,
			@Min(0) @Max(4) Integer tnm, @Min(0) @Max(4) Integer ecog, Boolean presencaMetastase,
			Boolean comorbidadesRelevantes, Boolean dorIntensa, Boolean sangramentoAtivo, Boolean perdaPeso,
			LocalDate dtDiagnostico, LocalDate dtEncaminhamento, Boolean examesImagem, Boolean biopsia,
			Boolean imunoHistoquimica, EtapaAtualCuidado etapaAtualCuidado,
			CondicaoSocioeconomica condicaoSocioeconomica, Escolaridade escolaridade, Boolean cuidadorAusente,
			Prioridade prioridade, Integer pontuacaoGeografica, Integer pontuacaoClinica, Integer pontuacaoAssistencial,
			Integer pontuacaoSociodemografica, Integer pontuacaoTotal) {
		this.id = id;
		this.nomeCompleto = nomeCompleto;
		this.dtNascimento = dtNascimento;
		this.sexo = sexo;
		this.cartaoSus = cartaoSus;
		this.municipioResidencia = municipioResidencia;
		this.distanciaCentroOncologico = distanciaCentroOncologico;
		this.tipoCancer = tipoCancer;
		this.tnm = tnm;
		this.ecog = ecog;
		this.presencaMetastase = presencaMetastase;
		this.comorbidadesRelevantes = comorbidadesRelevantes;
		this.dorIntensa = dorIntensa;
		this.sangramentoAtivo = sangramentoAtivo;
		this.perdaPeso = perdaPeso;
		this.dtDiagnostico = dtDiagnostico;
		this.dtEncaminhamento = dtEncaminhamento;
		this.examesImagem = examesImagem;
		this.biopsia = biopsia;
		this.imunoHistoquimica = imunoHistoquimica;
		this.etapaAtualCuidado = etapaAtualCuidado;
		this.condicaoSocioeconomica = condicaoSocioeconomica;
		this.escolaridade = escolaridade;
		this.cuidadorAusente = cuidadorAusente;
		this.prioridade = prioridade;
		this.pontuacaoGeografica = pontuacaoGeografica;
		this.pontuacaoClinica = pontuacaoClinica;
		this.pontuacaoAssistencial = pontuacaoAssistencial;
		this.pontuacaoSociodemografica = pontuacaoSociodemografica;
		this.pontuacaoTotal = pontuacaoTotal;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public LocalDate getDtNascimento() {
		return dtNascimento;
	}

	public void setDtNascimento(LocalDate dtNascimento) {
		this.dtNascimento = dtNascimento;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public Long getCartaoSus() {
		return cartaoSus;
	}

	public void setCartaoSus(Long cartaoSus) {
		this.cartaoSus = cartaoSus;
	}

	public String getMunicipioResidencia() {
		return municipioResidencia;
	}

	public void setMunicipioResidencia(String municipioResidencia) {
		this.municipioResidencia = municipioResidencia;
	}

	public Integer getDistanciaCentroOncologico() {
		return distanciaCentroOncologico;
	}

	public void setDistanciaCentroOncologico(Integer distanciaCentroOncologico) {
		this.distanciaCentroOncologico = distanciaCentroOncologico;
	}

	public TipoCancer getTipoCancer() {
		return tipoCancer;
	}

	public void setTipoCancer(TipoCancer tipoCancer) {
		this.tipoCancer = tipoCancer;
	}

	public Integer getTnm() {
		return tnm;
	}

	public void setTnm(Integer tnm) {
		this.tnm = tnm;
	}

	public Integer getEcog() {
		return ecog;
	}

	public void setEcog(Integer ecog) {
		this.ecog = ecog;
	}

	public Boolean getPresencaMetastase() {
		return presencaMetastase;
	}

	public void setPresencaMetastase(Boolean presencaMetastase) {
		this.presencaMetastase = presencaMetastase;
	}

	public Boolean getComorbidadesRelevantes() {
		return comorbidadesRelevantes;
	}

	public void setComorbidadesRelevantes(Boolean comorbidadesRelevantes) {
		this.comorbidadesRelevantes = comorbidadesRelevantes;
	}

	public Boolean getDorIntensa() {
		return dorIntensa;
	}

	public void setDorIntensa(Boolean dorIntensa) {
		this.dorIntensa = dorIntensa;
	}

	public Boolean getSangramentoAtivo() {
		return sangramentoAtivo;
	}

	public void setSangramentoAtivo(Boolean sangramentoAtivo) {
		this.sangramentoAtivo = sangramentoAtivo;
	}

	public Boolean getPerdaPeso() {
		return perdaPeso;
	}

	public void setPerdaPeso(Boolean perdaPeso) {
		this.perdaPeso = perdaPeso;
	}

	public LocalDate getDtDiagnostico() {
		return dtDiagnostico;
	}

	public void setDtDiagnostico(LocalDate dtDiagnostico) {
		this.dtDiagnostico = dtDiagnostico;
	}

	public LocalDate getDtEncaminhamento() {
		return dtEncaminhamento;
	}

	public void setDtEncaminhamento(LocalDate dtEncaminhamento) {
		this.dtEncaminhamento = dtEncaminhamento;
	}

	/**
	 * {@link Paciente#examesImagem}
	 */
	public Boolean getExamesImagem() {
		return examesImagem;
	}

	/**
	 * {@link Paciente#examesImagem}
	 */
	public void setExamesImagem(Boolean examesImagem) {
		this.examesImagem = examesImagem;
	}

	/**
	 * {@link Paciente#biopsia}
	 */
	public Boolean getBiopsia() {
		return biopsia;
	}

	/**
	 * {@link Paciente#biopsia}
	 */
	public void setBiopsia(Boolean biopsia) {
		this.biopsia = biopsia;
	}

	/**
	 * {@link Paciente#imunoHistoquimica}
	 */
	public Boolean getImunoHistoquimica() {
		return imunoHistoquimica;
	}

	/**
	 * {@link Paciente#imunoHistoquimica}
	 */
	public void setImunoHistoquimica(Boolean imunoHistoquimica) {
		this.imunoHistoquimica = imunoHistoquimica;
	}

	public EtapaAtualCuidado getEtapaAtualCuidado() {
		return etapaAtualCuidado;
	}

	public void setEtapaAtualCuidado(EtapaAtualCuidado etapaAtualCuidado) {
		this.etapaAtualCuidado = etapaAtualCuidado;
	}

	public CondicaoSocioeconomica getCondicaoSocioeconomica() {
		return condicaoSocioeconomica;
	}

	public void setCondicaoSocioeconomica(CondicaoSocioeconomica condicaoSocioeconomica) {
		this.condicaoSocioeconomica = condicaoSocioeconomica;
	}

	public Escolaridade getEscolaridade() {
		return escolaridade;
	}

	public void setEscolaridade(Escolaridade escolaridade) {
		this.escolaridade = escolaridade;
	}

	public Boolean getCuidadorAusente() {
		return cuidadorAusente;
	}

	public void setCuidadorAusente(Boolean cuidadorAusente) {
		this.cuidadorAusente = cuidadorAusente;
	}

	public Prioridade getPrioridade() {
		return prioridade;
	}

	public void setPrioridade(Prioridade prioridade) {
		this.prioridade = prioridade;
	}

	public Integer getPontuacaoGeografica() {
		return pontuacaoGeografica;
	}

	public void setPontuacaoGeografica(Integer pontuacaoGeografica) {
		this.pontuacaoGeografica = pontuacaoGeografica;
	}

	public Integer getPontuacaoClinica() {
		return pontuacaoClinica;
	}

	public void setPontuacaoClinica(Integer pontuacaoClinica) {
		this.pontuacaoClinica = pontuacaoClinica;
	}

	public Integer getPontuacaoAssistencial() {
		return pontuacaoAssistencial;
	}

	public void setPontuacaoAssistencial(Integer pontuacaoAssistencial) {
		this.pontuacaoAssistencial = pontuacaoAssistencial;
	}

	public Integer getPontuacaoSociodemografica() {
		return pontuacaoSociodemografica;
	}

	public void setPontuacaoSociodemografica(Integer pontuacaoSociodemografica) {
		this.pontuacaoSociodemografica = pontuacaoSociodemografica;
	}

	public Integer getPontuacaoTotal() {
		return pontuacaoTotal;
	}

	public void setPontuacaoTotal(Integer pontuacaoTotal) {
		this.pontuacaoTotal = pontuacaoTotal;
	}
}
