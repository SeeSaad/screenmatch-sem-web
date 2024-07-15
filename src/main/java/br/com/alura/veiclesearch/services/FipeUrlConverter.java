package br.com.alura.veiclesearch.services;

public class FipeUrlConverter {
    private final String partialUrl = "https://parallelum.com.br/fipe/api/v1/";

    public String search(String categoria) {
        return partialUrl + categoria + "/marcas";
    }

    public String search(String categoria, String marca) {
        return search(categoria) + "/" + marca + "/modelos";
    }

    public String search(String categoria, String marca, int veiculo) {
        return search(categoria, marca) + "/" + veiculo + "/anos";
    }

    public String search(String categoria, String marca, int veiculo, String ano) {
        return search(categoria, marca, veiculo) + "/" + ano;
    }
}
