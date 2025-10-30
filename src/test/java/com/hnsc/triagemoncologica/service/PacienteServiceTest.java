package com.hnsc.triagemoncologica.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.hnsc.triagemoncologica.enums.CondicaoSocioeconomica;
import com.hnsc.triagemoncologica.enums.Escolaridade;
import com.hnsc.triagemoncologica.enums.EtapaAtualCuidado;
import com.hnsc.triagemoncologica.model.Paciente;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class PacienteServiceTest {
    @InjectMocks
    private PacienteService pacienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculaPontuacao_TodosFatoresMaximos() {
        Paciente paciente = new Paciente();
        paciente.setDistanciaCentroOncologico(150); // +2
        paciente.setTnm(4); // +3
        paciente.setPresencaMetastase(true); // +3
        paciente.setEcog(3); // +2
        paciente.setComorbidadesRelevantes(true); // +1
        paciente.setDorIntensa(true); // +2
        paciente.setSangramentoAtivo(true); // +2
        paciente.setPerdaPeso(true); // +1
        paciente.setDtDiagnostico(LocalDate.now().minusDays(90)); // +2
        paciente.setDtEncaminhamento(LocalDate.now().minusDays(60)); // +2
        paciente.setExamesImagem(true); // +1
        paciente.setBiopsia(true); // +1
        paciente.setImunoHistoquimica(true); // +1
        paciente.setEtapaAtualCuidado(EtapaAtualCuidado.INICIO_TRATAMENTO); // +3
        paciente.setCondicaoSocioeconomica(CondicaoSocioeconomica.VULNERAVEL); // +2
        paciente.setCuidadorAusente(true); // +1
        paciente.setEscolaridade(Escolaridade.FUNDAMENTAL_INCOMPLETO); // +1
        // Total: 2+3+3+2+1+2+2+1+2+2+1+1+1+3+2+1+1 = 30

        Integer pontuacao = pacienteService.calculaPontuacao(paciente).getPontuacaoTotal();
        assertEquals(30, pontuacao);
    }

    @Test
    void testCalculaPontuacao_TodosFatoresMinimos() {
        Paciente paciente = new Paciente();
        paciente.setDistanciaCentroOncologico(30); // 0
        paciente.setTnm(1); // 0
        paciente.setPresencaMetastase(false); // 0
        paciente.setEcog(0); // 0
        paciente.setComorbidadesRelevantes(false); // 0
        paciente.setDorIntensa(false); // 0
        paciente.setSangramentoAtivo(false); // 0
        paciente.setPerdaPeso(false); // 0
        paciente.setDtDiagnostico(LocalDate.now().minusDays(30)); // 0
        paciente.setDtEncaminhamento(LocalDate.now().minusDays(15)); // 0
        paciente.setExamesImagem(false); // 0
        paciente.setBiopsia(false); // 0
        paciente.setImunoHistoquimica(false); // 0
        paciente.setEtapaAtualCuidado(EtapaAtualCuidado.EM_TRATAMENTO); // 0
        paciente.setCondicaoSocioeconomica(CondicaoSocioeconomica.REGULAR); // 0
        paciente.setCuidadorAusente(false); // 0
        paciente.setEscolaridade(Escolaridade.ENSINO_SUPERIOR); // 0
        // Total: 0

        Integer pontuacao = pacienteService.calculaPontuacao(paciente).getPontuacaoTotal();
        assertEquals(0, pontuacao);
    }
}
