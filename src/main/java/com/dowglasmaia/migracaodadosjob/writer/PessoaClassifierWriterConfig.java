package com.dowglasmaia.migracaodadosjob.writer;

import com.dowglasmaia.migracaodadosjob.dominio.Pessoa;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.builder.ClassifierCompositeItemWriterBuilder;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do escritor classificador para entidade Pessoa.
 * Utiliza {@link ClassifierCompositeItemWriter} para direcionar registros de {@link Pessoa} para diferentes escritores com base em um classificador.
 */
@Configuration
public class PessoaClassifierWriterConfig {

    /**
     * Cria um {@link ClassifierCompositeItemWriter} para classificar e direcionar dados de {@link Pessoa} para diferentes escritores.
     *
     * @param pessoaJdbcBatchItemWriter   Escritor JDBC para dados válidos de Pessoa.
     * @param pessoaFlatFileItemWriter    Escritor de arquivo plano para dados inválidos de Pessoa.
     * @return ClassifierCompositeItemWriter configurado para direcionar dados de Pessoa.
     */
    @Bean
    public ClassifierCompositeItemWriter<Pessoa> pessoaClassifierCompositeItemWriter(
          JdbcBatchItemWriter<Pessoa> pessoaJdbcBatchItemWriter,
          FlatFileItemWriter<Pessoa> pessoaFlatFileItemWriter
    ) {
        return new ClassifierCompositeItemWriterBuilder<Pessoa>()
              .classifier(classifier(pessoaJdbcBatchItemWriter, pessoaFlatFileItemWriter)) // Define o classificador
              .build();
    }

    /**
     * Cria um classificador {@link Classifier} para decidir qual escritor usar com base nos dados de Pessoa.
     *
     * @param pessoaJdbcBatchItemWriter   Escritor JDBC para dados válidos de Pessoa.
     * @param pessoaFlatFileItemWriter    Escritor de arquivo plano para dados inválidos de Pessoa.
     * @return Classifier configurado para classificar os dados de Pessoa.
     */
    private Classifier<Pessoa, ItemWriter<? super Pessoa>> classifier(
          JdbcBatchItemWriter<Pessoa> pessoaJdbcBatchItemWriter,
          FlatFileItemWriter<Pessoa> pessoaFlatFileItemWriter
    ) {
        return new Classifier<Pessoa, ItemWriter<? super Pessoa>>() {
            @Override
            public ItemWriter<? super Pessoa> classify(Pessoa pessoa) {
                if (pessoa.isValida()) {
                    return pessoaJdbcBatchItemWriter; // Usa o escritor JDBC para pessoas válidas
                } else {
                    return pessoaFlatFileItemWriter; // Usa o escritor de arquivo para pessoas inválidas
                }
            }
        };
    }
}