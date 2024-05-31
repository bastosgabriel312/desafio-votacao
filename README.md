# Desafio Votação

Neste projeto, foram desenvolvidos diversos controllers e serviços para gerenciar diferentes entidades relacionadas ao sistema de votação. Abaixo, estão as explicações de cada controller e como funciona o agendamento de tarefas com Spring Schedule.

## Iniciando a Aplicação Localmente

Para acessar a documentação da API, siga os passos abaixo:

1. Acesse o diretório `/docker` e execute o comando `docker-compose up` para iniciar o banco de dados.
2. Em seguida em outro terminal, execute o comando `mvn clean install` para buildar o projeto.
3. Por fim, inicie a aplicação Spring executando a classe `DesafioVotacaoApplication`.

Após seguir esses passos, você poderá acessar a documentação da API por meio do seguinte link: [http://localhost:8080/desafioVotacao/swagger-ui/index.html#/](http://localhost:8080/desafioVotacao/swagger-ui/index.html#/)

## Controllers

### AssociadoController

Este controller é responsável por lidar com as operações relacionadas aos associados do sistema de votação.

- **`POST /desafioVotacao/api/v1/associados`:** Cria um novo associado.
- **`GET /desafioVotacao/api/v1/associados`:** Lista todos os associados cadastrados.
- **`GET /desafioVotacao/api/v1/associados/{id}`:** Busca um associado pelo ID.

### PautaController

O PautaController gerencia as operações relacionadas às pautas das sessões de votação.

- **`POST /desafioVotacao/api/v1/pautas`:** Cria uma nova pauta.
- **`GET /desafioVotacao/api/v1/pautas`:** Lista todas as pautas cadastradas.
- **`GET /desafioVotacao/api/v1/pautas/{id}`:** Busca uma pauta pelo ID.

### SessaoVotacaoController

Este controller lida com as operações relacionadas às sessões de votação.

- **`POST /desafioVotacao/api/v1/sessoes`:** Abre uma nova sessão de votação.
- **`GET /desafioVotacao/api/v1/sessoes`:** Lista todas as sessões de votação.
- **`GET /desafioVotacao/api/v1/sessoes/{id}`:** Busca uma sessão de votação pelo ID.

### VotoController

O VotoController é responsável por gerenciar as operações relacionadas aos votos dos associados.

- **`POST /desafioVotacao/api/v1/votos`:** Registra um novo voto.
- **`GET /desafioVotacao/api/v1/votos`:** Lista todos os votos cadastrados.
- **`GET /desafioVotacao/api/v1/votos/{id}`:** Busca um voto pelo ID.

## Spring Schedule

O agendamento de tarefas é feito utilizando a anotação `@Scheduled` do Spring Framework. No método `verificarSessoes()`, é agendada uma tarefa que executa a cada segundo para verificar se há sessões de votação ativas que já expiraram. Caso existam, essas sessões são encerradas automaticamente.

```java
@Scheduled(fixedRate = 1000) // Executa a cada segundo
@Override
public void verificarSessoes() {
    List<SessaoVotacao> sessoes = sessaoVotacaoRepository.findAllByDataFimBeforeAndAtiva(LocalDateTime.now(), true);
    sessoes.forEach(sessaoVotacao -> {
        sessaoVotacao.setAtiva(false);
        sessaoVotacaoRepository.save(sessaoVotacao);
        pautaServiceV1.encerrarSessaoVotacao(sessaoVotacao);
        logger.debug("Encerrando sessão: {}", sessaoVotacao);
    });
}
```
# Resultados de Performance

Na pasta `/results`, estão armazenados os resultados dos testes de performance realizados. Abaixo, estão os resultados obtidos:

```plaintext
Benchmark                                                       Mode  Cnt    Score   Error  Units
AssociadoServicePerformanceTest.testSalvarAssociadoPerformance  avgt    5    0,576 ± 2,516  ms/op
PautaServicePerformanceTest.testSalvarPautaPerformance          avgt    5    1,614 ± 5,911  ms/op
SessaoVotacaoServicePerformanceTest.testAbrirSessaoPerformance  avgt    5    1,280 ± 4,677  ms/op
VotoServicePerformanceTest.testSalvarVotoPerformance            avgt    5    1,444 ± 4,959  ms/op
VotoServicePerformanceTest.testSalvarMilVotosNao                  ss       416,945          ms/op
VotoServicePerformanceTest.testSalvarMilVotosSim                  ss       435,578          ms/op
```

## Interpretação dos Resultados
    AssociadoServicePerformanceTest: O tempo médio para salvar um associado foi de aproximadamente 0,576 ms.
    PautaServicePerformanceTest: O tempo médio para salvar uma pauta foi de aproximadamente 1,614 ms.
    SessaoVotacaoServicePerformanceTest: O tempo médio para abrir uma sessão de votação foi de aproximadamente 1,280 ms.
    VotoServicePerformanceTest (Salvar Voto): O tempo médio para salvar um voto foi de aproximadamente 1,444 ms.
    VotoServicePerformanceTest (Salvar Mil Votos Não): O tempo para salvar mil votos "Não" foi de aproximadamente 416,945 ms.
    VotoServicePerformanceTest (Salvar Mil Votos Sim): O tempo para salvar mil votos "Sim" foi de aproximadamente 435,578 ms.

