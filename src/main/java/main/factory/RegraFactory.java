package main.factory;

import main.exception.RuleLoadException;
import main.rules.Regra;
import main.rules.impl.*;

import java.util.*;

public class RegraFactory {
    private static final Map<String, Class<? extends Regra>> REGISTRO = new HashMap<>();

    static {
        REGISTRO.put("RegraScoreMinimo",      RegraScoreMinimo.class);
        REGISTRO.put("RegraRendaMinima",      RegraRendaMinima.class);
        REGISTRO.put("RegraIdadeValida",      RegraIdadeValida.class);
        REGISTRO.put("RegraRendaSobreParcela", RegraRendaSobreParcela.class);
        REGISTRO.put("RegraSemAtrasos",       RegraSemAtrasos.class);
        REGISTRO.put("RegraComboRiscoAlto",   RegraComboRiscoAlto.class);
    }

    public static void registrar(String nome, Class<? extends Regra> clazz) {
        REGISTRO.put(nome, clazz);
    }

    public static Regra criar(String nomeClasse) {
        Class<? extends Regra> clazz = REGISTRO.get(nomeClasse);

        if (clazz == null) {
            clazz = tentarCarregarPorReflection(nomeClasse);
        }

        if (clazz == null) {
            throw new RuleLoadException(
                    "Regra não encontrada: '" + nomeClasse + "'. " +
                            "Registre-a com RegraFactory.registrar() ou verifique o nome da classe."
            );
        }

        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuleLoadException(
                    "Falha ao instanciar regra: " + nomeClasse + ". " +
                            "Verifique se a classe possui construtor público sem argumentos.",
                    e
            );
        }
    }

    public static List<Regra> criarTodas() {
        List<Regra> regras = new ArrayList<>();
        for (Map.Entry<String, Class<? extends Regra>> entry : REGISTRO.entrySet()) {
            try {
                regras.add(criar(entry.getKey()));
            } catch (RuleLoadException e) {
                System.err.println("[WARN] Não foi possível instanciar: " + entry.getKey());
            }
        }
        regras.sort(Comparator.comparingInt(Regra::getPrioridade));
        return regras;
    }


    public static Set<String> listarRegistradas() {
        return Collections.unmodifiableSet(REGISTRO.keySet());
    }

    @SuppressWarnings("unchecked")
    private static Class<? extends Regra> tentarCarregarPorReflection(String nomeClasse) {
        String[] pacotes = {
                "main.rules.impl.",
                "main.rules.",
                ""
        };

        for (String pacote : pacotes) {
            try {
                Class<?> clazz = Class.forName(pacote + nomeClasse);
                if (Regra.class.isAssignableFrom(clazz)) {
                    return (Class<? extends Regra>) clazz;
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
        return null;
    }
}