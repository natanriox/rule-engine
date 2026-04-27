package main.rules.impl;

import main.domain.AprovacaoRequest;
import main.rules.Regra;

public class RegraRendaMinima implements Regra {

    private static final double RENDA_MINIMA = 2000.0;

    @Override
    public boolean avaliar(AprovacaoRequest request) {
        return request.getRendaMensal() >= RENDA_MINIMA;
    }

    @Override
    public String getNome() {
        return "RegraRendaMinima";
    }

    @Override
    public int getPrioridade() {
        return 2;
    }

    @Override
    public String getMensagemFalha() {
        return String.format("Renda mensal abaixo do mínimo exigido (R$ %.2f).", RENDA_MINIMA);
    }
}