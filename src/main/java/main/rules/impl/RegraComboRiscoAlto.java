package main.rules.impl;

import main.domain.AprovacaoRequest;
import main.rules.Regra;

public class RegraComboRiscoAlto implements Regra {

    private static final int SCORE_CRITICO = 500;
    private static final int ATRASOS_CRITICOS = 2;

    @Override
    public boolean avaliar(AprovacaoRequest request) {
        boolean scoreAbaixoCritico = request.getScoreCredito() < SCORE_CRITICO;
        boolean atrasosAcimaLimite = request.getHistoricoAtrasos() > ATRASOS_CRITICOS;
        return !(scoreAbaixoCritico && atrasosAcimaLimite);
    }

    @Override
    public String getNome() {
        return "RegraComboRiscoAlto";
    }

    @Override
    public int getPrioridade() {
        return 6;
    }

    @Override
    public String getMensagemFalha() {
        return String.format(
                "Perfil de risco alto crítico: score < %d e mais de %d atrasos.",
                SCORE_CRITICO, ATRASOS_CRITICOS
        );
    }
}