package main.repository;

import main.domain.AprovacaoRequest;
import main.domain.AprovacaoResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoriaRequestRepository {

    private final List<AprovacaoRequest> requests = new ArrayList<>();
    private final List<AprovacaoResponse> responses = new ArrayList<>();

    public void salvarRequest(AprovacaoRequest request) {
        requests.add(request);
    }

    public void salvarResponse(AprovacaoResponse response) {
        responses.add(response);
    }

    public List<AprovacaoResponse> listarResponses() {
        return Collections.unmodifiableList(responses);
    }

    public List<AprovacaoRequest> listarRequests() {
        return Collections.unmodifiableList(requests);
    }

    public int totalRequests() {
        return requests.size();
    }
}