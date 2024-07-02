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
    public Job migracaoDadosJob(Step migrarPessoaStep, Step migrarDadosBancariosStep){
        return jobBuilderFactory.get("migracaoDadosJob")
              .incrementer(new RunIdIncrementer())
              .start(parallelStepsFlow(migrarPessoaStep, migrarDadosBancariosStep))
              .end()
              .build();
    }

    /**
     * Define o fluxo de steps que serão executados em paralelo.
     *
     * @param migrarPessoaStep         Step para migrar dados de pessoas.
     * @param migrarDadosBancariosStep Step para migrar dados bancários.
     * @return Flow que define os steps a serem executados em paralelo.
     */
    private Flow parallelStepsFlow(Step migrarPessoaStep, Step migrarDadosBancariosStep){
        return new FlowBuilder<Flow>("parallelStepsFlow")
              .split(new SimpleAsyncTaskExecutor())
              .add(migrarPessoaFlow(migrarPessoaStep), migrarDadosBancariosFlow(migrarDadosBancariosStep))
              .build();
    }

    /**
     * Define o fluxo para o step de migração de dados de pessoas.
     *
     * @param migrarPessoaStep Step para migrar dados de pessoas.
     * @return Flow que envolve o step de migração de dados de pessoas.
     */
    private Flow migrarPessoaFlow(Step migrarPessoaStep){
        return new FlowBuilder<Flow>("migrarPessoaFlow")
              .start(migrarPessoaStep)
              .build();
    }

    /**
     * Define o fluxo para o step de migração de dados bancários.
     *
     * @param migrarDadosBancariosStep Step para migrar dados bancários.
     * @return Flow que envolve o step de migração de dados bancários.
     */
    private Flow migrarDadosBancariosFlow(Step migrarDadosBancariosStep){
        return new FlowBuilder<Flow>("migrarDadosBancariosFlow")
              .start(migrarDadosBancariosStep)
              .build();
    }

}
