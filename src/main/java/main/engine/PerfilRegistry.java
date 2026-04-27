package main.engine;

import java.util.*;

class PerfilRegistry {

    private static final Map<String, List<String>> PERFIS = new HashMap<>();

    static {
        PERFIS.put("credito-pf", List.of(
                "RegraScoreMinimo",
                "RegraRendaMinima",
                "RegraIdadeValida",
                "RegraRendaSobreParcela",
                "RegraSemAtrasos",
                "RegraComboRiscoAlto"
        ));

        PERFIS.put("credito-pj", List.of(
                "RegraScoreMinimo",
                "RegraRendaMinima",
                "RegraRendaSobreParcela",
                "RegraComboRiscoAlto"
        ));

        PERFIS.put("antifraude", List.of(
                "RegraSemAtrasos",
                "RegraComboRiscoAlto"
        ));

        PERFIS.put("simples", List.of(
                "RegraScoreMinimo",
                "RegraRendaMinima"
        ));

        PERFIS.put("todas", List.of(
                "RegraScoreMinimo",
                "RegraRendaMinima",
                "RegraIdadeValida",
                "RegraRendaSobreParcela",
                "RegraSemAtrasos",
                "RegraComboRiscoAlto"
        ));
    }

    static List<String> obterClassesPorPerfil(String perfil) {
        return PERFIS.getOrDefault(perfil.toLowerCase(), Collections.emptyList());
    }

    static Set<String> listarPerfis() {
        return Collections.unmodifiableSet(PERFIS.keySet());
    }
}