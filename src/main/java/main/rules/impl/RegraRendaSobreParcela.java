package main.rules.impl;

import main.domain.AprovacaoRequest;
import main.rules.Regra;

public class RegraRendaSobreParcela implements Regra {

    private static final double COMPROMETIMENTO_MAXIMO = 0.30;
    private static final int PARCELAS = 12;

    @Override
    public boolean avaliar(AprovacaoRequest request) {
        double parcelaMensal = request.getValorSolicitado() / PARCELAS;
        double comprometimentoMaximo = parcelaMensal * COMPROMETIMENTO_MAXIMO;
        double limiteComprometimento = request.getRendaMensal() * COMPROMETIMENTO_MAXIMO;
        return request.getRendaMensal() > comprometimentoMaximo
                && parcelaMensal <= limiteComprometimento;
    }

    @Override
    public String getNome() {
        return "RegraRendaSobreParcela";
    }

    @Override
    public int getPrioridade() {
        return 4;
    }

    @Override
    public String getMensagemFalha() {
        return String.format(
                "Parcela mensal excede %d%% da renda do cliente.",
                (int) (COMPROMETIMENTO_MAXIMO * 100)
        );
    }
}