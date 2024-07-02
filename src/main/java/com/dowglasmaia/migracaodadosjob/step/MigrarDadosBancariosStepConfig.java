package com.dowglasmaia.migracaodadosjob.step;

import com.dowglasmaia.migracaodadosjob.dominio.DadosBancario;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do step de migração de dados bancários.
 * Define o processamento de leitura de um arquivo CSV e gravação no banco de dados.
 */
@EnableBatchProcessing
@Configuration
public class MigrarDadosBancariosStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /**
     * Define o step para migrar dados bancários de um arquivo CSV para um banco de dados.
     *
     * @param arquivoDadosBancarioReader Leitor de dados do arquivo CSV.
     * @param bancoDadosBancarioWriter Gravador de dados para o banco de dados.
     * @return Step configurado para migração de dados bancários.
     */
    @Bean
    public Step migrarDadosBancariosStep(
          @Qualifier("arquivoDadosBancarioReader") ItemReader<DadosBancario> arquivoDadosBancarioReader,
          @Qualifier("bancoDadosBancarioWriter") ItemWriter<DadosBancario> bancoDadosBancarioWriter
    ) {
        return stepBuilderFactory
              .get("migrarDadosBancariosStep") // Nome do step
              .<DadosBancario, DadosBancario>chunk(10000) // Configura o chunk size
              .reader(arquivoDadosBancarioReader) // Define o leitor de dados
              .writer(bancoDadosBancarioWriter) // Define o gravador de dados
              .build();
    }
}
