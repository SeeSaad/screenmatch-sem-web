package br.com.alura.veiclesearch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosVeiculoAno(@JsonAlias("Valor") String valor,
                              @JsonAlias("Marca") String marca,
                              @JsonAlias("Modelo") String modelo,
                              @JsonAlias("AnoModelo") Integer ano,
                              @JsonAlias("Combustivel") String combustivel,
                              @JsonAlias("MesReferencia") String mesReferencia) {
}
