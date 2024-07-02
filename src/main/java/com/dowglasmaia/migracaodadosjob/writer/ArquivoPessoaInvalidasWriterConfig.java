package com.dowglasmaia.migracaodadosjob.writer;

import com.dowglasmaia.migracaodadosjob.dominio.Pessoa;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
public class ArquivoPessoaInvalidasWriterConfig {

    @Bean
    public FlatFileItemWriter<Pessoa> arquivoPessoaFlatFileItemReader(){
        return new FlatFileItemWriterBuilder<Pessoa>()
              .name("arquivoPessoaFlatFileItemReader")
              .resource(new FileSystemResource("files/pessoas_invalidas.csv"))
              .delimited()
              .names("id")
              .build();
    }

}
