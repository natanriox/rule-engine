package main.rules.impl;

import main.domain.AprovacaoRequest;
import main.rules.Regra;

public class RegraSemAtrasos implements Regra {

    @Override
    public boolean avaliar(AprovacaoRequest request) {
        return request.getHistoricoAtrasos() == 0;
    }

    @Override
    public String getNome() {
        return "RegraSemAtrasos";
    }

    @Override
    public int getPrioridade() {
        return 5;
    }

    @Override
    public String getMensagemFalha() {
        return "Cliente possui histórico de atrasos em pagamentos.";
    }
}