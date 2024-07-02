package com.dowglasmaia.migracaodadosjob.writer;

import com.dowglasmaia.migracaodadosjob.dominio.DadosBancario;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Configuração do escritor para dados bancários no banco de dados.
 * Utiliza {@link JdbcBatchItemWriter} para inserir registros de {@link DadosBancario} no banco de dados.
 */
@Configuration
public class BancoDadosBancariosWriterConfig {

    /**
     * Cria um {@link JdbcBatchItemWriter} para gravar dados bancários em um banco de dados.
     *
     * @param dataSource Fonte de dados (DataSource) a ser utilizada pelo escritor.
     * @return JdbcBatchItemWriter configurado para inserir dados bancários.
     */
    @Bean
    public JdbcBatchItemWriter<DadosBancario> bancoDadosBancarioWriter(
          @Qualifier("appDataSource") DataSource dataSource
    ) {
        return new JdbcBatchItemWriterBuilder<DadosBancario>()
              .dataSource(dataSource) // Define a fonte de dados a ser utilizada
              .sql("INSERT INTO public.dados_bancarios (id, pessoa_id, agencia, conta, banco) VALUES (:id, :pessoaId, :agencia, :conta, :banco);") // SQL de inserção
              .beanMapped() // Mapeia os campos do objeto DadosBancario diretamente para os parâmetros SQL
              .build();
    }
    

}
