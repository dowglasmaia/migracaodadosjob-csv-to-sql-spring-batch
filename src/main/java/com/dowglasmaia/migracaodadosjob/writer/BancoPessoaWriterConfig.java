package com.dowglasmaia.migracaodadosjob.writer;

import com.dowglasmaia.migracaodadosjob.dominio.Pessoa;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Configuração do escritor para entidade Pessoa no banco de dados.
 * Utiliza {@link JdbcBatchItemWriter} para inserir registros de {@link Pessoa} no banco de dados.
 */
@Configuration
public class BancoPessoaWriterConfig {

    /**
     * Cria um {@link JdbcBatchItemWriter} para gravar dados de Pessoa em um banco de dados.
     *
     * @param dataSource Fonte de dados (DataSource) a ser utilizada pelo escritor.
     * @return JdbcBatchItemWriter configurado para inserir dados de Pessoa.
     */
    @Bean
    public JdbcBatchItemWriter<Pessoa> bancoPessoaWriter(
          @Qualifier("appDataSource") DataSource dataSource
    ) {
        return new JdbcBatchItemWriterBuilder<Pessoa>()
              .dataSource(dataSource) // Define a fonte de dados a ser utilizada
              .sql("INSERT INTO pessoa (id, nome, email, data_nascimento, idade) VALUES (?,?,?,?,?)") // SQL de inserção
              .itemPreparedStatementSetter(preparedStatementSetter()) // Configura o preparador de declarações
              .build();
    }

    /**
     * Cria um {@link ItemPreparedStatementSetter} para configurar os parâmetros da declaração SQL com os valores de Pessoa.
     *
     * @return ItemPreparedStatementSetter configurado para configurar os parâmetros da declaração SQL.
     */
    private ItemPreparedStatementSetter<Pessoa> preparedStatementSetter() {
        return new ItemPreparedStatementSetter<Pessoa>() {
            @Override
            public void setValues(Pessoa pessoa, PreparedStatement ps) throws SQLException {
                ps.setInt(1, pessoa.getId()); // Define o valor do parâmetro 1 (id)
                ps.setString(2, pessoa.getNome()); // Define o valor do parâmetro 2 (nome)
                ps.setString(3, pessoa.getEmail()); // Define o valor do parâmetro 3 (email)
                ps.setDate(4, new Date(pessoa.getDataNascimento().getTime())); // Define o valor do parâmetro 4 (data_nascimento)
                ps.setInt(5, pessoa.getIdade()); // Define o valor do parâmetro 5 (idade)
            }
        };
    }
}