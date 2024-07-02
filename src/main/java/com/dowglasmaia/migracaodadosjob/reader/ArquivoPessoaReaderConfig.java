package com.dowglasmaia.migracaodadosjob.reader;

import com.dowglasmaia.migracaodadosjob.dominio.Pessoa;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.validation.BindException;

import java.util.Date;

@Configuration
public class ArquivoPessoaReaderConfig {

    @Bean
    public FlatFileItemReader<Pessoa> arquivoPessoaReader(){
        return new FlatFileItemReaderBuilder<Pessoa>()
              .name("arquivoPessoaReader")
              .resource(new FileSystemResource("files/pessoas.csv"))
              .delimited()
              .names("nome", "email", "dataNascimento", "idade", "id")
              .addComment("--")
              .fieldSetMapper(fieldSetMapper())
              .build();

    }

    private FieldSetMapper<Pessoa> fieldSetMapper(){
        return new FieldSetMapper<Pessoa>() {
            @Override
            public Pessoa mapFieldSet(FieldSet fieldSet) throws BindException{
                return Pessoa.builder()
                      .id(fieldSet.readInt("id"))
                      .nome(fieldSet.readString("nome"))
                      .email(fieldSet.readString("email"))
                      .idade(fieldSet.readInt("idade"))
                      .dataNascimento(new Date(fieldSet.readDate("dataNascimento", "yyyy-MM-dd HH:mm:ss").getTime()))
                      .build();
            }
        };
    }
}
