package main.domain;

public enum Resultado {

    APROVADO("Aprovado", true),
    REPROVADO("Reprovado", false),
    PENDENTE_ANALISE("Pendente de Análise", false);

    private final String descricao;
    private final boolean aprovado;

    Resultado(String descricao, boolean aprovado) {
        this.descricao = descricao;
        this.aprovado = aprovado;
    }

    public String getDescricao() {
        return descricao;
    }

    public boolean isAprovado() {
        return aprovado;
    }

}