package main.engine;

import main.domain.AprovacaoRequest;
import main.domain.AprovacaoResponse;
import main.domain.Resultado;
import main.exception.RuleLoadException;
import main.factory.RegraFactory;
import main.rules.Regra;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RuleEngine {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public RuleEngine(List<Regra> regras) {
        if (regras == null || regras.isEmpty()) {
            throw new IllegalStateException("Engine precisa de pelo menos 1 regra.");
        }

        this.regras = new ArrayList<>(regras);

        System.out.println("[ENGINE] Regras carregadas: " + this.regras.size());
    }

    public enum ModoAvaliacao {
        TODOS,
        QUALQUER,
        FALHA_RAPIDA
    }

    private List<Regra> regras = new ArrayList<>();
    private ModoAvaliacao modo = ModoAvaliacao.TODOS;
    private boolean verboso = true;


    public RuleEngine modoAvaliacao(ModoAvaliacao modo) {
        this.modo = modo;
        return this;
    }

    public RuleEngine verboso(boolean verboso) {
        this.verboso = verboso;
        return this;
    }


    public RuleEngine adicionarRegra(Regra regra) {
        Objects.requireNonNull(regra, "Regra não pode ser nula.");
        regras.add(regra);
        regras.sort(Comparator.comparingInt(Regra::getPrioridade));
        return this;
    }


    public RuleEngine limparRegras() {
        regras.clear();
        return this;
    }


    public RuleEngine carregarRegras(String perfil) {
        limparRegras();
        List<String> classesRegras = PerfilRegistry.obterClassesPorPerfil(perfil);

        if (classesRegras.isEmpty()) {
            throw new RuleLoadException("Perfil desconhecido: '" + perfil + "'. " +
                    "Perfis disponíveis: " + PerfilRegistry.listarPerfis());
        }

        for (String nomeClasse : classesRegras) {
            Regra regra = RegraFactory.criar(nomeClasse);
            adicionarRegra(regra);
        }

        log("Engine", String.format("Perfil '%s' carregado com %d regras.", perfil, regras.size()));
        return this;
    }


    public RuleEngine carregarRegrasJson(String caminhoRecurso) {
        limparRegras();
        String json = lerRecurso(caminhoRecurso);
        JsonRuleConfigParser.RuleConfig config = JsonRuleConfigParser.parse(json);

        try {
            this.modo = ModoAvaliacao.valueOf(config.modo().toUpperCase());
        } catch (IllegalArgumentException e) {
            log("WARN", "Modo '" + config.modo() + "' desconhecido. Usando TODOS.");
        }

        for (JsonRuleConfigParser.RegraConfig rc : config.regras()) {
            Regra regra = RegraFactory.criar(rc.classe());
            adicionarRegra(regra);
        }

        log("Engine", String.format(
                "JSON '%s' carregado. Perfil: '%s', Modo: %s, Regras: %d.",
                caminhoRecurso, config.perfil(), this.modo, regras.size()
        ));
        return this;
    }


    public AprovacaoResponse avaliar(AprovacaoRequest request) {
        if (regras.isEmpty()) {
            throw new IllegalStateException(
                    "Nenhuma regra configurada no engine. " +
                            "Use adicionarRegra(), carregarRegras() ou carregarRegrasJson()."
            );
        }

        log("Processando", String.format(
                "Cliente: %s | ID: %s | Score: %d | Renda: R$ %.2f | Valor: R$ %.2f",
                request.getNomeCliente(), request.getId(),
                request.getScoreCredito(), request.getRendaMensal(), request.getValorSolicitado()
        ));
        log("Engine", String.format("Modo: %s | %d regras ativas", modo, regras.size()));

        List<String> passadas = new ArrayList<>();
        List<String> falhas = new ArrayList<>();

        for (Regra regra : regras) {
            boolean passou = false;
            try {
                passou = regra.avaliar(request);
            } catch (Exception e) {
                log("ERROR", "Exceção na regra " + regra.getNome() + ": " + e.getMessage());
                falhas.add(regra.getNome() + " [ERRO: " + e.getMessage() + "]");
                continue;
            }

            String icone = passou ? "✓ OK" : "✗ FALHOU";
            logRegra(regra, icone, passou ? null : regra.getMensagemFalha());

            if (passou) {
                passadas.add(regra.getNome());
            } else {
                falhas.add(regra.getNome() + ": " + regra.getMensagemFalha());
                if (modo == ModoAvaliacao.FALHA_RAPIDA) {
                    log("Engine", "Falha rápida: interrompendo na primeira falha.");
                    break;
                }
            }
        }

        return construirResposta(request, passadas, falhas);
    }


    private AprovacaoResponse construirResposta(
            AprovacaoRequest request,
            List<String> passadas,
            List<String> falhas) {

        Resultado resultado;
        String motivo;

        switch (modo) {
            case QUALQUER -> {
                if (!passadas.isEmpty()) {
                    resultado = Resultado.APROVADO;
                    motivo = "Ao menos uma regra atendida.";
                } else {
                    resultado = Resultado.REPROVADO;
                    motivo = "Nenhuma regra atendida.";
                }
            }
            case TODOS, FALHA_RAPIDA -> {
                if (falhas.isEmpty()) {
                    resultado = Resultado.APROVADO;
                    motivo = "Todas as " + passadas.size() + " regras atendidas.";
                } else {
                    resultado = Resultado.REPROVADO;
                    motivo = "Falhas em " + falhas.size() + " regra(s): " +
                            String.join("; ", falhas.stream()
                                    .map(f -> f.split(":")[0].trim())
                                    .toList());
                }
            }
            default -> {
                resultado = Resultado.PENDENTE_ANALISE;
                motivo = "Modo de avaliação desconhecido.";
            }
        }

        log("Resultado", String.format("%s | %s | %s",
                request.getNomeCliente(), resultado, motivo));
        logDivisor();

        return new AprovacaoResponse(
                request.getId(),
                request.getNomeCliente(),
                resultado,
                motivo,
                passadas,
                falhas
        );
    }


    public List<Regra> getRegras() {
        return Collections.unmodifiableList(regras);
    }

    public int totalRegras() {
        return regras.size();
    }


    private void log(String categoria, String mensagem) {
        if (verboso) {
            System.out.printf("[%s] [%-15s] %s%n",
                    LocalDateTime.now().format(FMT), categoria, mensagem);
        }
    }

    private void logRegra(Regra regra, String status, String detalhe) {
        if (verboso) {
            String linha = String.format("[%s] [%-15s] (%d) %-30s %s",
                    LocalDateTime.now().format(FMT),
                    regra.getNome(),
                    regra.getPrioridade(),
                    regra.getNome(),
                    status);
            if (detalhe != null) linha += " → " + detalhe;
            System.out.println(linha);
        }
    }

    private void logDivisor() {
        if (verboso) {
            System.out.println("─".repeat(80));
        }
    }


    private String lerRecurso(String caminho) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(caminho)) {
            if (is != null) {
                return new String(is.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuleLoadException("Falha ao ler recurso do classpath: " + caminho, e);
        }

        try {
            java.nio.file.Path path = java.nio.file.Path.of(caminho);
            if (java.nio.file.Files.exists(path)) {
                return java.nio.file.Files.readString(path, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new RuleLoadException("Falha ao ler arquivo: " + caminho, e);
        }

        throw new RuleLoadException("Recurso não encontrado: '" + caminho + "'.");
    }
}