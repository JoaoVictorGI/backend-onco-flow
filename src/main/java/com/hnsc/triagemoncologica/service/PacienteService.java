package com.hnsc.triagemoncologica.service;

import static java.time.temporal.ChronoUnit.DAYS;

import com.hnsc.triagemoncologica.dto.GerarPacienteDTO;
import com.hnsc.triagemoncologica.dto.GerarPacienteDTO.Pessoa;
import com.hnsc.triagemoncologica.enums.CondicaoSocioeconomica;
import com.hnsc.triagemoncologica.enums.Escolaridade;
import com.hnsc.triagemoncologica.enums.EtapaAtualCuidado;
import com.hnsc.triagemoncologica.enums.Prioridade;
import com.hnsc.triagemoncologica.enums.Sexo;
import com.hnsc.triagemoncologica.enums.TipoCancer;
import com.hnsc.triagemoncologica.model.Paciente;
import com.hnsc.triagemoncologica.repository.PacienteRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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

    public Page<Paciente> findByNomeOrderByPrioridadeAsc(String nomeCompleto, Pageable pageable) {
        return repository.findByNomeOrderByPrioridadeAsc(nomeCompleto, pageable);
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

    @Transactional
    public Long gerarPacientesFicticios(Long qtdPacientes) {
        RestClient restClient = RestClient.create();
        GerarPacienteDTO dadosPacientes = restClient
                .get()
                .uri("https://randomuser.me/api/?results=" + qtdPacientes + "&nat=br")
                .retrieve()
                .body(GerarPacienteDTO.class);

        logger.info("QUANTIDADE DE PACIENTES: {}", qtdPacientes);
        logger.info("DADOS PACIENTES LENGHT: {}", dadosPacientes.results().size());

        List<Paciente> pacientes = new ArrayList<>();

        for (Pessoa pacienteDTO : dadosPacientes.results()) {
            Paciente paciente = new Paciente();

            paciente.setNomeCompleto(
                    pacienteDTO.name().first() + " " + pacienteDTO.name().last());

            paciente.setDtNascimento(LocalDate.ofInstant(pacienteDTO.dob().date(), ZoneId.of("America/Sao_Paulo")));

            switch (pacienteDTO.gender()) {
                case "female" -> paciente.setSexo(Sexo.FEMININO);
                case "male" -> paciente.setSexo(Sexo.MASCULINO);
            }

            paciente.setCartaoSus(Long.parseLong(gerarCartaoSus()));

            paciente.setMunicipioResidencia("Tubarão");

            paciente.setDistanciaCentroOncologico(1 + (int) (new Random().nextFloat() * (120 - 1)));

            TipoCancer[] tiposCancer = TipoCancer.values();
            int randomTipoCancerIndex = new Random().nextInt(tiposCancer.length);
            paciente.setTipoCancer(tiposCancer[randomTipoCancerIndex]);

            paciente.setTnm(1 + (int) (new Random().nextFloat() * (4 - 1)));

            paciente.setEcog(0 + (int) (new Random().nextFloat() * (4 - 0)));

            paciente.setPresencaMetastase(new Random().nextBoolean());

            paciente.setComorbidadesRelevantes(new Random().nextBoolean());

            paciente.setDorIntensa(new Random().nextBoolean());

            paciente.setSangramentoAtivo(new Random().nextBoolean());

            paciente.setPerdaPeso(new Random().nextBoolean());

            LocalDate dtAtual = LocalDate.now();

            paciente.setDtDiagnostico(gerarDataAleatoria(dtAtual.minusDays(75), dtAtual));

            paciente.setDtEncaminhamento(gerarDataAleatoria(dtAtual.minusDays(45), dtAtual));

            paciente.setExamesImagem(new Random().nextBoolean());

            paciente.setBiopsia(new Random().nextBoolean());

            paciente.setImunoHistoquimica(new Random().nextBoolean());

            EtapaAtualCuidado[] etapasCuidado = EtapaAtualCuidado.values();
            int randomEtapaAtualIndex = new Random().nextInt(etapasCuidado.length);
            paciente.setEtapaAtualCuidado(etapasCuidado[randomEtapaAtualIndex]);

            CondicaoSocioeconomica[] condicoesSocioeconomicas = CondicaoSocioeconomica.values();
            int randomCondicaoSocioeconomicaIndex = new Random().nextInt(condicoesSocioeconomicas.length);
            paciente.setCondicaoSocioeconomica(condicoesSocioeconomicas[randomCondicaoSocioeconomicaIndex]);

            Escolaridade[] escolaridades = Escolaridade.values();
            int randomEscolarideIndex = new Random().nextInt(escolaridades.length);
            paciente.setEscolaridade(escolaridades[randomEscolarideIndex]);

            paciente.setCuidadorAusente(new Random().nextBoolean());

            calculaPontuacao(paciente);
            paciente.setPrioridade(calculaPrioridade(paciente.getPontuacaoTotal()));

            pacientes.add(paciente);
        }

        var savedPacientes = repository.saveAll(pacientes);

        return Long.valueOf(savedPacientes.size());
    }

    private static LocalDate gerarDataAleatoria(LocalDate min, LocalDate max) {
        long diaMinimo = min.toEpochDay();
        long diaMaximo = max.toEpochDay();

        long diaAleatorio = ThreadLocalRandom.current().nextLong(diaMinimo, diaMaximo);
        return LocalDate.ofEpochDay(diaAleatorio);
    }

    public static String gerarCartaoSus() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        // 1. Gera o primeiro dígito (7, 8 ou 9)
        // random.nextInt(3) gera 0, 1 ou 2. Somando 7, temos 7, 8 ou 9.
        sb.append(7 + random.nextInt(3));

        // 2. Gera os próximos 10 dígitos aleatórios
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }

        String primeirosOnzeDigitos = sb.toString();
        int soma = 0;

        // 3. Cálculo da Soma Ponderada (Pesos de 15 a 5)
        for (int i = 0; i < 11; i++) {
            int digito = Character.getNumericValue(primeirosOnzeDigitos.charAt(i));
            // O peso começa em 15 e diminui conforme o índice aumenta
            // i=0 (peso 15), i=1 (peso 14), etc.
            soma += digito * (15 - i);
        }

        int resto = soma % 11;
        int digitoVerificador = 11 - resto;

        if (digitoVerificador == 11) {
            digitoVerificador = 0;
        }

        // 4. Definição do Sufixo e Retorno
        if (digitoVerificador == 10) {
            // Regra de exceção: Soma + 2 e concatena "001" + novo DV
            soma += 2;
            resto = soma % 11;
            digitoVerificador = 11 - resto;
            return primeirosOnzeDigitos + "001" + digitoVerificador;
        } else {
            // Regra padrão: concatena "000" + DV
            return primeirosOnzeDigitos + "000" + digitoVerificador;
        }
    }
}
