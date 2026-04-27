package main.rules;

import main.domain.AprovacaoRequest;

public interface Regra {
    boolean avaliar(AprovacaoRequest request);

    String getNome();

    int getPrioridade();

    default String getMensagemFalha() {
        return "Regra não atendida: " + getNome();
    }
}