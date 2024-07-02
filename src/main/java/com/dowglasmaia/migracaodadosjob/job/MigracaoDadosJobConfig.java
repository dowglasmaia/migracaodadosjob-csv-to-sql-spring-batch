package com.dowglasmaia.migracaodadosjob.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;



@Configuration
public class MigracaoDadosJobConfig {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public Job migracaoDadosJob(
          Step migrarPessoaStep,
          Step migrarDadosBancariosStep
    ){
        return jobBuilderFactory
              .get("migracaoDadosJob")
              .incrementer(new RunIdIncrementer())
              .start(stepsParalelos(migrarPessoaStep,migrarDadosBancariosStep))
              .end()
              .build();
    }


    private Flow stepsParalelos(Step migrarPessoaStep, Step migrarDadosBancariosStep){

        Flow migraDadosBancariosFlow = new FlowBuilder<Flow>("migraDadosBancariosFlow")
              .start(migrarDadosBancariosStep)
              .build();

        Flow stepsParalelos = new FlowBuilder<Flow>("stepsParalelos")
              .start(migrarPessoaStep)
              .split(new SimpleAsyncTaskExecutor()) // responsavel por divider o fluxo para ocorer em paralelo
              .add(migraDadosBancariosFlow)
              .build();

        return stepsParalelos;


    }

}
