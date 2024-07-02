package com.dowglasmaia.migracaodadosjob.reader;

import com.dowglasmaia.migracaodadosjob.dominio.DadosBancario;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * Configuração do leitor de arquivo para objetos do tipo {@link DadosBancario}.
 * Lê os dados de um arquivo CSV e os converte em instâncias de {@link DadosBancario}.
 */
@Configuration
public class ArquivoDadosBancarioReaderConfig {

    /**
     * Cria um {@link FlatFileItemReader} para ler o arquivo CSV e mapear os dados para objetos {@link DadosBancario}.
     *
     * @return FlatFileItemReader configurado para ler dados bancários.
     */
    @Bean
    public FlatFileItemReader<DadosBancario> arquivoDadosBancarioReader() {
        return new FlatFileItemReaderBuilder<DadosBancario>()
              .name("arquivoDadosBancarioReader") // Nome do leitor para identificação
              .resource(new FileSystemResource("files/dados_bancarios.csv")) // Caminho do arquivo CSV
              .delimited()
              .names("pessoaId", "agencia", "conta", "banco", "id") // Nomes das colunas no CSV
              .addComment("--") // Define o caractere de comentário
              .targetType(DadosBancario.class) // Mapeia diretamente para a classe DadosBancario
              .build();
    }
}
