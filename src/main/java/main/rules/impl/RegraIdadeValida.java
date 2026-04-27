package main.rules.impl;

import main.domain.AprovacaoRequest;
import main.rules.Regra;

public class RegraIdadeValida implements Regra {

    private static final int IDADE_MINIMA = 18;
    private static final int IDADE_MAXIMA = 75;

    @Override
    public boolean avaliar(AprovacaoRequest request) {
        int idade = request.getIdade();
        return idade >= IDADE_MINIMA && idade <= IDADE_MAXIMA;
    }

    @Override
    public String getNome() {
        return "RegraIdadeValida";
    }

    @Override
    public int getPrioridade() {
        return 3;
    }

    @Override
    public String getMensagemFalha() {
        return String.format("Idade fora da faixa permitida (%d a %d anos).", IDADE_MINIMA, IDADE_MAXIMA);
    }
}