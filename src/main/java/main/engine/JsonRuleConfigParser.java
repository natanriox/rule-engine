package main.engine;

import main.exception.RuleLoadException;

import java.util.ArrayList;
import java.util.List;


class JsonRuleConfigParser {

    record RegraConfig(String classe, int prioridade) {}

    record RuleConfig(String perfil, String modo, List<RegraConfig> regras) {}

    static RuleConfig parse(String json) {
        try {
            String perfil = extrairString(json, "perfil");
            String modo = extrairString(json, "modo");
            if (modo == null || modo.isBlank()) modo = "TODOS";

            List<RegraConfig> regras = extrairRegras(json);
            return new RuleConfig(perfil, modo, regras);
        } catch (Exception e) {
            throw new RuleLoadException("Falha ao parsear JSON de configuração de regras: " + e.getMessage(), e);
        }
    }

    private static String extrairString(String json, String chave) {
        String pattern = "\"" + chave + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return null;

        int colon = json.indexOf(":", idx + pattern.length());
        int start = json.indexOf("\"", colon + 1);
        if (start < 0) return null;
        int end = json.indexOf("\"", start + 1);
        if (end < 0) return null;

        return json.substring(start + 1, end);
    }

    private static List<RegraConfig> extrairRegras(String json) {
        List<RegraConfig> lista = new ArrayList<>();

        int regraIdx = json.indexOf("\"regras\"");
        if (regraIdx < 0) return lista;

        int arrayStart = json.indexOf("[", regraIdx);
        int arrayEnd = json.indexOf("]", arrayStart);
        if (arrayStart < 0 || arrayEnd < 0) return lista;

        String arrayContent = json.substring(arrayStart + 1, arrayEnd);

        int pos = 0;
        while (pos < arrayContent.length()) {
            int objStart = arrayContent.indexOf("{", pos);
            if (objStart < 0) break;
            int objEnd = arrayContent.indexOf("}", objStart);
            if (objEnd < 0) break;

            String obj = arrayContent.substring(objStart, objEnd + 1);
            String classe = extrairString(obj, "classe");
            int prioridade = extrairInt(obj, "prioridade");

            if (classe != null && !classe.isBlank()) {
                lista.add(new RegraConfig(classe, prioridade));
            }

            pos = objEnd + 1;
        }

        return lista;
    }

    private static int extrairInt(String json, String chave) {
        String pattern = "\"" + chave + "\"";
        int idx = json.indexOf(pattern);
        if (idx < 0) return 0;

        int colon = json.indexOf(":", idx + pattern.length());
        if (colon < 0) return 0;

        int start = colon + 1;
        while (start < json.length() && Character.isWhitespace(json.charAt(start))) start++;

        int end = start;
        while (end < json.length() && (Character.isDigit(json.charAt(end)) || json.charAt(end) == '-')) end++;

        try {
            return Integer.parseInt(json.substring(start, end));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}