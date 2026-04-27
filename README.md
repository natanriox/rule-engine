# ️ Rule Engine (Java Core)

Motor de regras pra simulação de decisão de crédito.

Sem framework. Sem dependência. Só Java puro e arquitetura limpa.

---

##  O que isso faz

- Avalia crédito com regras plugáveis
- Decide: APROVADO / REPROVADO
- Explica o motivo da decisão
- Roda engine com múltiplos modos (TODOS, QUALQUER, FAIL FAST)
- Suporta configuração via JSON ou perfil
- Registra histórico em memória

---

##  Arquitetura

- Domain-driven (Request / Response / Result)
- Rule Engine desacoplado
- Strategy pattern nas regras
- Factory pra instanciar regras
- Repository em memória
- Service layer pra orquestração
- Parser de JSON pra configuração dinâmica

---

##  Engine modes

- TODOS → todas regras devem passar
- QUALQUER → apenas uma regra suficiente
- FALHA_RAPIDA → para no primeiro erro

---

##  Regras

- Score mínimo
- Renda mínima
- Idade válida
- Atrasos no histórico
- Capacidade de pagamento
- Regras combinadas de risco

---

##  Execução

Rodar: main.demo.RuleEngineDemo

---

##  Exemplo

Entrada:

```text
João - score 720 - renda 3000 - valor 10000

Saída:
APROVADO

OU:
REPROVADO (renda insuficiente + histórico de atrasos)

