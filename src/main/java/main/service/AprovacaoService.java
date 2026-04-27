package main.service;

import main.domain.AprovacaoRequest;
import main.domain.AprovacaoResponse;
import main.engine.RuleEngine;
import main.repository.MemoriaRequestRepository;

import java.util.List;
import java.util.Objects;

public class AprovacaoService {

    private final RuleEngine engine;
    private final MemoriaRequestRepository repository;

    public AprovacaoService(RuleEngine engine, MemoriaRequestRepository repository) {
        this.engine = Objects.requireNonNull(engine, "RuleEngine não pode ser nulo.");
        this.repository = new MemoriaRequestRepository();
    }

    public AprovacaoResponse processar(AprovacaoRequest request) {
        Objects.requireNonNull(request, "AprovacaoRequest não pode ser nula.");
        repository.salvarRequest(request);

        AprovacaoResponse response = engine.avaliar(request);

        repository.salvarResponse(response);
        return response;
    }


    public List<AprovacaoResponse> historico() {
        return repository.listarResponses();
    }

    public int totalProcessadas() {
        return repository.totalRequests();
    }

    public long totalAprovadas() {
        return repository.listarResponses().stream()
                .filter(r -> r.getResultado().isAprovado())
                .count();
    }


    public void imprimirEstatisticas() {
        int total = totalProcessadas();
        long aprovadas = totalAprovadas();
        long reprovadas = total - aprovadas;

        System.out.println("\n" + "═".repeat(60));
        System.out.println("  RESUMO ESTATÍSTICO");
        System.out.println("═".repeat(60));
        System.out.printf("  Total processadas : %d%n", total);
        System.out.printf("  Aprovadas         : %d (%.1f%%)%n", aprovadas,
                total > 0 ? (aprovadas * 100.0 / total) : 0);
        System.out.printf("  Reprovadas        : %d (%.1f%%)%n", reprovadas,
                total > 0 ? (reprovadas * 100.0 / total) : 0);
        System.out.printf("  Regras no engine  : %d%n", engine.totalRegras());
        System.out.println("═".repeat(60) + "\n");
    }
}