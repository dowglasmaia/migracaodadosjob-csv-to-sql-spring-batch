package com.dowglasmaia.migracaodadosjob.writer;

import com.dowglasmaia.migracaodadosjob.dominio.Pessoa;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * Configuração do gravador de arquivo para registros inválidos de {@link Pessoa}.
 * Grava os dados inválidos de pessoas em um arquivo CSV.
 */
@Configuration
public class ArquivoPessoaInvalidasWriterConfig {

    /**
     * Cria um {@link FlatFileItemWriter} para gravar dados inválidos de {@link Pessoa} em um arquivo CSV.
     *
     * @return FlatFileItemWriter configurado para gravar dados inválidos de pessoas.
     */
    @Bean
    public FlatFileItemWriter<Pessoa> arquivoPessoaFlatFileItemReader() {
        return new FlatFileItemWriterBuilder<Pessoa>()
              .name("arquivoPessoaFlatFileItemReader") // Nome do gravador para identificação
              .resource(new FileSystemResource("files/pessoas_invalidas.csv")) // Caminho do arquivo CSV
              .delimited()
              .names("id") // Nome das colunas no CSV, por exemplo, 'id'
              .build();
    }

}
