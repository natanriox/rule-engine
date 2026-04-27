package main.demo;

import main.domain.AprovacaoRequest;
import main.engine.RuleEngine;
import main.repository.MemoriaRequestRepository;
import main.rules.impl.RegraScoreMinimo;
import main.rules.impl.RegraRendaSobreParcela;
import main.rules.impl.RegraSemAtrasos;
import main.service.AprovacaoService;

import java.util.List;

public class RuleEngineDemo {

    public static void main(String[] args) {

        MemoriaRequestRepository repository = new MemoriaRequestRepository();

        RuleEngine engine = new RuleEngine(List.of(
                new RegraScoreMinimo(),
                new RegraRendaSobreParcela(),
                new RegraSemAtrasos()
        ));

        AprovacaoService service = new AprovacaoService(engine, repository);

        AprovacaoRequest r1 = AprovacaoRequest.builder()
                .nomeCliente("João")
                .idade(25)
                .rendaMensal(3000)
                .scoreCredito(720)
                .valorSolicitado(10000)
                .historicoAtrasos(0)
                .build();

        AprovacaoRequest r2 = AprovacaoRequest.builder()
                .nomeCliente("Maria")
                .idade(17)
                .rendaMensal(5000)
                .scoreCredito(800)
                .valorSolicitado(20000)
                .historicoAtrasos(1)
                .build();

        System.out.println(service.processar(r1));
        System.out.println(service.processar(r2));

        service.imprimirEstatisticas();
    }
}