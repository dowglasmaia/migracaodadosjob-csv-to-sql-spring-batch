package com.dowglasmaia.migracaodadosjob.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * Configuração das fontes de dados para a aplicação.
 * Define duas fontes de dados: springDataSource e appDataSource.
 */
@Configuration
public class DataSourceConfig {

    /**
     * Configura a fonte de dados principal (springDataSource) usando as propriedades definidas em 'spring.datasource'.
     *
     * @return DataSource configurado como a fonte de dados principal.
     */
    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource springDataSource() {
        return DataSourceBuilder.create().build();
    }

    /**
     * Configura a fonte de dados secundária (appDataSource) usando as propriedades definidas em 'app.datasource'.
     *
     * @return DataSource configurado como a fonte de dados secundária.
     */
    @Bean
    @ConfigurationProperties(prefix = "app.datasource")
    public DataSource appDataSource() {
        return DataSourceBuilder.create().build();
    }
}