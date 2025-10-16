package com.hnsc.triagemoncologica.service;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hnsc.triagemoncologica.enums.CondicaoSocioeconomica;
import com.hnsc.triagemoncologica.enums.Escolaridade;
import com.hnsc.triagemoncologica.enums.Prioridade;
import com.hnsc.triagemoncologica.model.Paciente;
import com.hnsc.triagemoncologica.repository.PacienteRepository;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;

@Service
public class PacienteService {

	Logger logger = LoggerFactory.getLogger(PacienteService.class);

	private final PacienteRepository repository;

	public PacienteService(PacienteRepository repository) {
		this.repository = repository;
	}

	public Paciente create(Paciente paciente) {
		calculaPontuacao(paciente);
		paciente.setPrioridade(calculaPrioridade(paciente.getPontuacaoTotal()));
		return repository.save(paciente);
	}

	public Optional<Paciente> findById(UUID id) {
		return repository.findById(id);
	}

	public Page<Paciente> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public Page<Paciente> findAllOrderByPrioridade(Pageable pageable) {
		return repository.findAllByOrderByPrioridadeAsc(pageable);
	}

	public Paciente update(Paciente paciente) {
		return repository.save(paciente);
	}

	public void delete(UUID id) {
		repository.deleteById(id);
	}

	Paciente calculaPontuacao(Paciente paciente) {
		int pontuacaoGeografica = 0;
		int pontuacaoClinica = 0;
		int pontuacaoAssistencial = 0;
		int pontuacaoSociodemografica = 0;
		int pontuacaoTotal = 0;

		/*
		 * Distância > 100 km -> +2 pontos
		 * Distância entre 50-100 km -> +1 ponto
		 * Menos de 50 km -> 0 pontos
		 */
		if (paciente.getDistanciaCentroOncologico() > 100) {
			pontuacaoGeografica += 2;
		} else if (paciente.getDistanciaCentroOncologico() > 50 && paciente.getDistanciaCentroOncologico() < 100) {
			pontuacaoGeografica += 1;
		}

		// Estadiamento avançado (III-IV) -> +3 pontos
		if (paciente.getTnm() == 3 || paciente.getTnm() == 4) {
			pontuacaoClinica += 3;
		}

		// Metástase presente -> +3 pontos
		if (paciente.getPresencaMetastase()) {
			pontuacaoClinica += 3;
		}

		// Performance status ECOG >= 2 -> +2 pontos
		if (paciente.getEcog() >= 2) {
			pontuacaoClinica += 2;
		}

		// Comorbidades graves (ex: insuficiência cardíaca, renal) -> +1 ponto
		if (paciente.getComorbidadesRelevantes()) {
			pontuacaoClinica += 1;
		}

		// Dor intensa -> +2 pontos
		if (paciente.getDorIntensa()) {
			pontuacaoClinica += 2;
		}

		// Sangramento ativo -> +2 pontos
		if (paciente.getSangramentoAtivo()) {
			pontuacaoClinica += 2;
		}

		// Perda de peso > 10% -> +1 ponto
		if (paciente.getPerdaPeso()) {
			pontuacaoClinica += 1;
		}

		// Tempo desde diagnóstico > 60 dias -> +2 pontos
		if (DAYS.between(paciente.getDtDiagnostico(), LocalDate.now()) > 60) {
			pontuacaoAssistencial += 2;
		}

		// Tempo desde encaminhamento > 30 dias -> +2 pontos
		if (DAYS.between(paciente.getDtEncaminhamento(), LocalDate.now()) > 30) {
			pontuacaoAssistencial += 2;
		}

		// Exames incompletos (faltando exames de imagem, biópsia ou imuno-histoquímica)
		// -> +1 ponto

		/**
		 * Exames de imagem faltando
		 * 
		 * True se estiverem faltando exames de imagem.
		 * False se não estiverem faltando.
		 */
		if (paciente.getExamesImagem()) {
			pontuacaoAssistencial += 1;
		}

		/**
		 * Biópsia faltando
		 * 
		 * True se estiver faltando biópsia.
		 * False se não estiver faltando.
		 */
		if (paciente.getBiopsia()) {
			pontuacaoAssistencial += 1;
		}

		/**
		 * Imuno-histoquímica faltando
		 * 
		 * True se estiver faltando biópsia.
		 * False se não estiver faltando.
		 */
		if (paciente.getImunoHistoquimica()) {
			pontuacaoAssistencial += 1;
		}

		/*
		 * Etapa atual de cuidado:
		 * 
		 * Diagnóstico -> +1 ponto
		 * Estadiamento -> +2 pontos
		 * Início de tratamento -> +3 pontos
		 * Recidiva -> +2 pontos
		 */
		switch (paciente.getEtapaAtualCuidado()) {
			case DIAGNOSTICO -> pontuacaoAssistencial += 1;
			case ESTADIAMENTO -> pontuacaoAssistencial += 2;
			case INICIO_TRATAMENTO -> pontuacaoAssistencial += 3;
			case RECIDIVA -> pontuacaoAssistencial += 2;
			case EM_TRATAMENTO -> pontuacaoAssistencial += 0;
		}

		// Vulnerabilidade social (baixa renda / cadastro em programas sociais) -> +2
		// pontos
		if (paciente.getCondicaoSocioeconomica().equals(CondicaoSocioeconomica.VULNERAVEL)) {
			pontuacaoSociodemografica += 2;
		}

		// Ausência de cuidador -> +1 ponto
		if (paciente.getCuidadorAusente()) {
			pontuacaoSociodemografica += 1;
		}

		// Baixa escolaridade (fundamental incompleto) -> +1 ponto
		if (paciente.getEscolaridade().equals(Escolaridade.FUNDAMENTAL_INCOMPLETO)) {
			pontuacaoSociodemografica += 1;
		}

		pontuacaoTotal = pontuacaoGeografica + pontuacaoClinica + pontuacaoAssistencial + pontuacaoSociodemografica;

		paciente.setPontuacaoGeografica(pontuacaoGeografica);
		paciente.setPontuacaoClinica(pontuacaoClinica);
		paciente.setPontuacaoAssistencial(pontuacaoAssistencial);
		paciente.setPontuacaoSociodemografica(pontuacaoSociodemografica);
		paciente.setPontuacaoTotal(pontuacaoTotal);

		return paciente;
	}

	/*
	 * Cálculo de prioridade (0-20+):
	 * 
	 * Quanto maior a pontuação, maior a urgência de atendimento
	 * 
	 * Alta prioridade (>=12 pontos)
	 * Média prioridade (6-11 pontos)
	 * Baixa prioridade (<=5 pontos)
	 */
	private Prioridade calculaPrioridade(Integer pontuacao) {
		logger.info("PONTUAÇÃO PRIORIDADE: " + pontuacao);

		if (pontuacao >= 12) {
			logger.info("PRIORIDADE: ALTA");
			return Prioridade.ALTA;
		} else if (pontuacao > 6 && pontuacao <= 11) {
			logger.info("PRIORIDADE: MEDIA");
			return Prioridade.MEDIA;
		}

		logger.info("PRIORIDADE: BAIXA");
		return Prioridade.BAIXA;
	}
}
