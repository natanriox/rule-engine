package main.domain;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public final class AprovacaoResponse {

    private final String requestId;
    private final String nomeCliente;
    private final Resultado resultado;
    private final String motivo;
    private final List<String> regrasFalhas;
    private final List<String> regrasPassadas;
    private final LocalDateTime processadoEm;

    public AprovacaoResponse(
            String requestId,
            String nomeCliente,
            Resultado resultado,
            String motivo,
            List<String> regrasPassadas,
            List<String> regrasFalhas) {
        this.requestId = requestId;
        this.nomeCliente = nomeCliente;
        this.resultado = resultado;
        this.motivo = motivo;
        this.regrasPassadas = Collections.unmodifiableList(regrasPassadas);
        this.regrasFalhas = Collections.unmodifiableList(regrasFalhas);
        this.processadoEm = LocalDateTime.now();
    }

    public String getRequestId()              { return requestId; }
    public String getNomeCliente()            { return nomeCliente; }
    public Resultado getResultado()           { return resultado; }
    public String getMotivo()                 { return motivo; }
    public List<String> getRegrasFalhas()     { return regrasFalhas; }
    public List<String> getRegrasPassadas()   { return regrasPassadas; }
    public LocalDateTime getProcessadoEm()    { return processadoEm; }

    @Override
    public String toString() {
        return String.format(
                "AprovacaoResponse{id='%s', cliente='%s', resultado=%s, motivo='%s'}",
                requestId, nomeCliente, resultado, motivo
        );
    }
}