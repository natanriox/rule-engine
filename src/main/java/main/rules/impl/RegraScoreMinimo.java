package main.rules.impl;

import main.domain.AprovacaoRequest;
import main.rules.Regra;

public class RegraScoreMinimo implements Regra {

    private static final int SCORE_MINIMO = 600;

    @Override
    public boolean avaliar(AprovacaoRequest request) {
        return request.getScoreCredito() >= SCORE_MINIMO;
    }

    @Override
    public String getNome() {
        return "RegraScoreMinimo";
    }

    @Override
    public int getPrioridade() {
        return 1;
    }

    @Override
    public String getMensagemFalha() {
        return String.format("Score de crédito abaixo do mínimo exigido (%d).", SCORE_MINIMO);
    }
}