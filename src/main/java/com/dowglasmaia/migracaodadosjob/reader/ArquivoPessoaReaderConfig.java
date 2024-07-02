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


/**
 * Configuração do leitor de arquivo para objetos do tipo {@link Pessoa}.
 * Lê os dados de um arquivo CSV e os converte em instâncias de {@link Pessoa}.
 */
@Configuration
public class ArquivoPessoaReaderConfig {

    /**
     * Cria um {@link FlatFileItemReader} para ler o arquivo CSV e mapear os dados para objetos {@link Pessoa}.
     *
     * @return FlatFileItemReader configurado para ler dados de pessoas.
     */
    @Bean
    public FlatFileItemReader<Pessoa> arquivoPessoaReader() {
        return new FlatFileItemReaderBuilder<Pessoa>()
              .name("arquivoPessoaReader") // Nome do leitor para identificação
              .resource(new FileSystemResource("files/pessoas.csv")) // Caminho do arquivo CSV
              .delimited()
              .names("nome", "email", "dataNascimento", "idade", "id") // Nomes das colunas no CSV
              .addComment("--") // Define o caractere de comentário
              .fieldSetMapper(fieldSetMapper()) // Define o mapeador de campos
              .build();
    }

    /**
     * Cria um {@link FieldSetMapper} para mapear os dados de cada linha do CSV para um objeto {@link Pessoa}.
     *
     * @return FieldSetMapper para mapear os campos do CSV para um objeto {@link Pessoa}.
     */
    private FieldSetMapper<Pessoa> fieldSetMapper() {
        return new FieldSetMapper<Pessoa>() {
            @Override
            public Pessoa mapFieldSet(FieldSet fieldSet) throws BindException {
                return Pessoa.builder()
                      .id(fieldSet.readInt("id")) // Lê o campo 'id' como inteiro
                      .nome(fieldSet.readString("nome")) // Lê o campo 'nome' como string
                      .email(fieldSet.readString("email")) // Lê o campo 'email' como string
                      .idade(fieldSet.readInt("idade")) // Lê o campo 'idade' como inteiro
                      .dataNascimento(new Date(fieldSet.readDate("dataNascimento", "yyyy-MM-dd HH:mm:ss").getTime())) // Lê o campo 'dataNascimento' como data
                      .build();
            }
        };
    }
}
