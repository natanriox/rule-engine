package main.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public final class AprovacaoRequest {

    private final String id;
    private final String nomeCliente;
    private final int idade;
    private final double rendaMensal;
    private final int scoreCredito;
    private final double valorSolicitado;
    private final int historicoAtrasos;
    private final LocalDateTime dataHora;

    private AprovacaoRequest(Builder builder) {
        this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.nomeCliente = builder.nomeCliente;
        this.idade = builder.idade;
        this.rendaMensal = builder.rendaMensal;
        this.scoreCredito = builder.scoreCredito;
        this.valorSolicitado = builder.valorSolicitado;
        this.historicoAtrasos = builder.historicoAtrasos;
        this.dataHora = LocalDateTime.now();
    }


    public String getId() {
        return id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public int getIdade() {
        return idade;
    }

    public double getRendaMensal() {
        return rendaMensal;
    }

    public int getScoreCredito() {
        return scoreCredito;
    }

    public double getValorSolicitado() {
        return valorSolicitado;
    }

    public int getHistoricoAtrasos() {
        return historicoAtrasos;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return String.format(
                "AprovacaoRequest{id='%s', cliente='%s', idade=%d, renda=%.2f, score=%d, valor=%.2f, atrasos=%d}",
                id, nomeCliente, idade, rendaMensal, scoreCredito, valorSolicitado, historicoAtrasos
        );
    }

    public static class Builder {
        private String nomeCliente = "Não Informado";
        private int idade;
        private double rendaMensal;
        private int scoreCredito;
        private double valorSolicitado;
        private int historicoAtrasos;

        public Builder nomeCliente(String nomeCliente) {
            this.nomeCliente = nomeCliente;
            return this;
        }

        public Builder idade(int idade) {
            this.idade = idade;
            return this;
        }

        public Builder rendaMensal(double rendaMensal) {
            this.rendaMensal = rendaMensal;
            return this;
        }

        public Builder scoreCredito(int scoreCredito) {
            this.scoreCredito = scoreCredito;
            return this;
        }

        public Builder valorSolicitado(double valorSolicitado) {
            this.valorSolicitado = valorSolicitado;
            return this;
        }

        public Builder historicoAtrasos(int historicoAtrasos) {
            this.historicoAtrasos = historicoAtrasos;
            return this;
        }

        public AprovacaoRequest build() {
            validate();
            return new AprovacaoRequest(this);
        }

        private void validate() {
            if (rendaMensal < 0) throw new IllegalArgumentException("Renda mensal não pode ser negativa.");
            if (scoreCredito < 0 || scoreCredito > 1000)
                throw new IllegalArgumentException("Score de crédito deve estar entre 0 e 1000.");
            if (valorSolicitado < 0) throw new IllegalArgumentException("Valor solicitado não pode ser negativo.");
            if (historicoAtrasos < 0) throw new IllegalArgumentException("Histórico de atrasos não pode ser negativo.");
        }
    }
}