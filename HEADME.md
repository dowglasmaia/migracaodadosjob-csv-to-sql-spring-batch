# Documentação do Spring Batch

## Componentes Principais

### Job

Um Job no Spring Batch é uma sequência de passos (Steps) que juntos formam uma unidade de trabalho completa. Cada Job é configurado para realizar uma tarefa específica, como migrar dados de um banco de dados para outro.

Exemplo de configuração de Job:

```java
@Configuration
public class MigracaoDadosJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job migracaoDadosJob(
          Step migrarPessoaStep,
          Step migrarDadosBancariosStep
    ){
        return jobBuilderFactory
              .get("migracaoDadosJob")
              .incrementer(new RunIdIncrementer())
              .start(migrarPessoaStep)
              .next(migrarDadosBancariosStep)
              .build();
    }
}
```

### Step

Um Step define uma etapa individual dentro de um Job, descrevendo como os dados devem ser lidos, processados e gravados. Cada Step é composto por um leitor (Reader), processador (Processor) opcional e escritor (Writer).

Exemplo de configuração de Step:

```java
@Bean
public Step migrarDadosBancariosStep(
      @Qualifier("arquivoDadosBancarioReader") ItemReader<DadosBancario> arquivoDadosBancarioReader,
      @Qualifier("bancoDadosBancarioWriter") ItemWriter<DadosBancario> bancoDadosBancarioWriter
){
    return stepBuilderFactory
          .get("migrarDadosBancariosStep")
          .<DadosBancario, DadosBancario>chunk(10000)
          .reader(arquivoDadosBancarioReader)
          .writer(bancoDadosBancarioWriter)
          .build();
}
```

### Reader

Um Reader é responsável por ler os dados de uma fonte externa, como um arquivo CSV ou um banco de dados, e convertê-los em objetos que podem ser processados pelo Spring Batch.

Exemplo de configuração de Reader:

```java
@Bean
public FlatFileItemReader<DadosBancario> arquivoDadosBancarioReader(){
    return new FlatFileItemReaderBuilder<DadosBancario>()
          .name("arquivoDadosBancarioReader")
          .resource(new FileSystemResource("files/dados_bancarios.csv"))
          .delimited()
          .names("pessoaId", "agencia", "conta", "banco", "id")
          .targetType(DadosBancario.class)
          .build();
}
```

### Writer

Um Writer é responsável por gravar os dados processados pelo Spring Batch em uma fonte de destino, como um banco de dados ou um arquivo.

Exemplo de configuração de Writer:

```java
@Bean
public JdbcBatchItemWriter<DadosBancario> bancoDadosBancarioWriter(
      @Qualifier("appDataSource") DataSource dataSource
){
    return new JdbcBatchItemWriterBuilder<DadosBancario>()
          .dataSource(dataSource)
          .sql("INSERT INTO public.dados_bancarios (id, pessoa_id, agencia, conta, banco) VALUES(:id, :pessoaId, :agencia, :conta, :banco);")
          .beanMapped()
          .build();
}
```

### Configurações

As configurações incluem todas as classes de configuração que definem Beans Spring necessários para o funcionamento correto do Spring Batch, como Datasources, Builders e outros componentes personalizados.

Exemplo de configuração geral:

```java
@Configuration
public class DataSourceConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource springDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.datasource")
    public DataSource appDataSource(){
        return DataSourceBuilder.create().build();
    }
}
```

### chunk
No contexto de Spring Batch, o método `chunk()` dentro da definição de um Step (`migrarDadosBancariosStep` neste caso) determina como os itens serão processados em lotes. Aqui está o que acontece com o chunking:

### Funcionamento do Chunking

1. **Configuração do Chunk Size (`chunk(10000)`):**
    - O método `chunk(10000)` especifica que cada lote de processamento conterá até 10.000 itens do tipo `DadosBancario`. Isso significa que o Spring Batch lerá até 10.000 itens do leitor (`arquivoDadosBancarioReader`), processará esses itens e então os gravará no escritor (`bancoDadosBancarioWriter`).

2. **Entrada e Saída:**
    - **Entrada (`arquivoDadosBancarioReader`)**: É o componente responsável por ler os dados de alguma fonte, como um arquivo CSV (`dados_bancarios.csv`, por exemplo). Cada item lido será do tipo `DadosBancario`.

    - **Processamento em Lotes (`chunk`)**: O Spring Batch processará os itens lidos em lotes de até 10.000 itens. Cada item do lote será passado para o processamento, onde você pode aplicar transformações ou validações, se necessário.

    - **Saída (`bancoDadosBancarioWriter`)**: É o componente que recebe os itens processados e os escreve no banco de dados (`dados_bancarios` no PostgreSQL, por exemplo). O `ItemWriter` `bancoDadosBancarioWriter` é responsável por gravar os dados processados no banco de dados.

3. **Quantidade de Requisições por Tempo:**
    - O número de requisições por tempo depende de vários fatores, incluindo a velocidade de leitura dos dados (`arquivoDadosBancarioReader`), o processamento de cada item dentro do chunk, e a velocidade de gravação (`bancoDadosBancarioWriter`).
    - Em geral, o Spring Batch é otimizado para processar grandes volumes de dados de forma eficiente, minimizando o número de acessos ao banco de dados e maximizando o uso de memória e CPU.

4. **Fluxo de Execução:**
    - O fluxo de execução é controlado pelo Spring Batch, que gerencia automaticamente o início, o processamento e o término do step `migrarDadosBancariosStep`. Ele pode ser iniciado manualmente ou configurado para iniciar automaticamente como parte de um job maior.

5. **Benefícios do Chunking:**
    - O uso de chunking oferece benefícios como melhor controle de transações, menor overhead de comunicação com o banco de dados (por lidar com lotes em vez de itens individuais), e melhor performance geral para grandes volumes de dados.

Em resumo, o método `chunk()` configura como os dados serão processados em lotes dentro do Spring Batch, controlando a entrada, o processamento em lotes e a saída dos itens do step `migrarDadosBancariosStep`.


## Fluxo Paralelo

### Configuração do Job

```java
@Bean
public Job migracaoDadosJob(Step migrarPessoaStep, Step migrarDadosBancariosStep){
    return jobBuilderFactory.get("migracaoDadosJob")
          .incrementer(new RunIdIncrementer())
          .start(parallelStepsFlow(migrarPessoaStep, migrarDadosBancariosStep))
          .end()
          .build();
}
```

Nesta configuração:

- **Job**: `migracaoDadosJob` é configurado para iniciar o fluxo de Steps definido em `parallelStepsFlow` e utiliza um `RunIdIncrementer` para garantir que cada execução do Job tenha um identificador único.

- **Flow**: `parallelStepsFlow` é o fluxo principal do Job, onde os Steps `migrarPessoaStep` e `migrarDadosBancariosStep` serão executados em paralelo.

### Método `parallelStepsFlow`

```java
private Flow parallelStepsFlow(Step migrarPessoaStep, Step migrarDadosBancariosStep){
    return new FlowBuilder<Flow>("parallelStepsFlow")
          .split(new SimpleAsyncTaskExecutor())
          .add(migrarPessoaFlow(migrarPessoaStep), migrarDadosBancariosFlow(migrarDadosBancariosStep))
          .build();
}
```

Neste método:

- **Split**: `split(new SimpleAsyncTaskExecutor())` define que os Steps adicionados a seguir serão executados em paralelo usando um executor de tarefas simples assíncrono. Isso significa que cada Step será executado em sua própria thread, permitindo processamento concorrente.

- **Adição de Flows**: `.add(migrarPessoaFlow(migrarPessoaStep), migrarDadosBancariosFlow(migrarDadosBancariosStep))` adiciona os Flows correspondentes aos Steps `migrarPessoaStep` e `migrarDadosBancariosStep` ao fluxo paralelo. Cada um desses Flows define como o Step associado será executado.

### Métodos `migrarPessoaFlow` e `migrarDadosBancariosFlow`

```java
private Flow migrarPessoaFlow(Step migrarPessoaStep){
    return new FlowBuilder<Flow>("migrarPessoaFlow")
          .start(migrarPessoaStep)
          .build();
}

private Flow migrarDadosBancariosFlow(Step migrarDadosBancariosStep){
    return new FlowBuilder<Flow>("migrarDadosBancariosFlow")
          .start(migrarDadosBancariosStep)
          .build();
}
```

- **Flows Individuais**: Cada método cria um Flow (`migrarPessoaFlow` e `migrarDadosBancariosFlow`) que encapsula um único Step (`migrarPessoaStep` e `migrarDadosBancariosStep`, respectivamente). Isso é necessário para que cada Step seja configurado dentro de um Flow antes de ser adicionado ao fluxo paralelo.

### Funcionamento do Fluxo Paralelo

No momento da execução do Job `migracaoDadosJob`:

- O Spring Batch iniciará o fluxo `parallelStepsFlow`.
- O método `split` garante que os Steps dentro deste fluxo (neste caso, `migrarPessoaStep` e `migrarDadosBancariosStep`) sejam executados em paralelo.
- Cada Step será executado independentemente em threads separadas, permitindo processamento simultâneo dos dados.

Portanto, o fluxo configurado permite que os Steps `migrarPessoaStep` e `migrarDadosBancariosStep` executem em paralelo, aproveitando a capacidade de processamento concorrente oferecida pelo Spring Batch. Isso é útil para melhorar o desempenho de jobs que precisam processar grandes volumes de dados de forma eficiente.