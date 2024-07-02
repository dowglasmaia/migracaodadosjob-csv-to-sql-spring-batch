package com.dowglasmaia.migracaodadosjob.step;

import com.dowglasmaia.migracaodadosjob.dominio.Pessoa;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do step de migração de dados de pessoas.
 * Define o processamento de leitura de um arquivo CSV e escrita em diferentes destinos.
 */
@EnableBatchProcessing
@Configuration
public class MigrarPessoaStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /**
     * Define o step para migrar dados de pessoas de um arquivo CSV.
     *
     * @param arquivoPessoaReader Leitor de dados do arquivo CSV.
     * @param bancoPessoaWriter Gravador de dados para o banco de dados.
     * @param pessoaClassifierCompositeItemWriter Classificador que direciona os dados para o escritor apropriado.
     * @param arquivoPessoaFlatFileItemReader Gravador de dados para um arquivo plano.
     * @return Step configurado para migração de dados de pessoas.
     */
    @Bean
    public Step migrarPessoaStep(
          @Qualifier("arquivoPessoaReader") ItemReader<Pessoa> arquivoPessoaReader,
          @Qualifier("bancoPessoaWriter") ItemWriter<Pessoa> bancoPessoaWriter,
          ClassifierCompositeItemWriter<Pessoa> pessoaClassifierCompositeItemWriter,
          FlatFileItemWriter<Pessoa> arquivoPessoaFlatFileItemReader
    ) {
        return stepBuilderFactory
              .get("migrarPessoaStep") // Nome do step
              .<Pessoa, Pessoa>chunk(10000) // Configura o chunk size
              .reader(arquivoPessoaReader) // Define o leitor de dados
              .writer(pessoaClassifierCompositeItemWriter) // Define o gravador de dados, com classificação
              .stream(arquivoPessoaFlatFileItemReader) // Adiciona fluxo do escritor de arquivo
              .build();
    }
}