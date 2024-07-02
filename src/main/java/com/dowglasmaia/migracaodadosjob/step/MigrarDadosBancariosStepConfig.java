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

@EnableBatchProcessing
@Configuration
public class MigrarDadosBancariosStepConfig {

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    @Bean
    public Step migrarDadosBancariosStep(
          @Qualifier("arquivoDadosBancarioReader") ItemReader<DadosBancario> arquivoDadosBancarioReader,
          @Qualifier("bancoDadosBancarioWriter")ItemWriter<DadosBancario> bancoDadosBancarioWriter
    ){
        return stepBuilderFactory
              .get("migrarDadosBancariosStep")
              .<DadosBancario, DadosBancario>chunk(10000)
              .reader(arquivoDadosBancarioReader)
              .writer(bancoDadosBancarioWriter)
              .build();
    }
}
