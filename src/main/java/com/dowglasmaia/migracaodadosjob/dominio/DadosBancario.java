package com.dowglasmaia.migracaodadosjob.dominio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DadosBancario {

    private int id;
    private int pessoaId;
    private int agencia;
    private int conta;
    private int banco;
}
