package com.dowglasmaia.migracaodadosjob.reader;

import com.dowglasmaia.migracaodadosjob.dominio.DadosBancario;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ArquivoDadosBancariosReaderConfig {

    @Bean
    public FlatFileItemReader<DadosBancario> arquivoDadosBancarioReader(){
        return new FlatFileItemReaderBuilder<DadosBancario>()
              .name("arquivoDadosBancarioReader")
              .resource(new FileSystemResource("files/dados_bancarios.csv"))
              .delimited()
              .names("pessoaId", "agencia", "conta", "banco","id")
              .addComment("--")
              .targetType(DadosBancario.class)
              .build();

    }
}
