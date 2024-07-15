package br.com.alura.veiclesearch.main;

import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import br.com.alura.veiclesearch.models.Dados;
import br.com.alura.veiclesearch.services.FipeUrlConverter;

import java.util.*;
import java.util.stream.Stream;

public class Main {
    private final FipeUrlConverter fipeUrlConverter = new FipeUrlConverter();
    private final ConverteDados converteDados = new ConverteDados();
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final Scanner scanner = new Scanner(System.in);

    private String getInput() {
        return scanner.nextLine();
    }

    private Integer getIntInRange(Integer min, Integer max, String input) {
        int intInput;

        try {
            intInput = Integer.parseInt(input);
            if (min != null && intInput >= min) {
                if (max != null && intInput <= max) return intInput;
            }
            return null;

        } catch (NumberFormatException e) {
            return null;
        }
    }

    public long numberOfOcurrances(Stream<String> streamDeStrings, String input) {
        return streamDeStrings
                .filter(s -> s.contains(input.toLowerCase()))
                .count();
    }

    public Optional<String> findAnyOcurrance(Stream<String> streamDeStrings, String input) {
        return streamDeStrings
                .filter(s -> s.contains(input.toLowerCase()))
                .findFirst();
    }

    private String identificarVeiculoPorNome() {
        String input = getInput();
        Map<String, String> map = Map.of("carro", "carros", "moto", "motos", "caminhao", "caminhoes", "caminhão", "caminhoes");
        List<String> veiculos = List.of("carro", "moto", "caminhão", "caminhao");

        Long quantidadeEncontros = numberOfOcurrances(veiculos.stream(), input);

        if (quantidadeEncontros.compareTo(1L) != 0){
            System.out.println("digite a opção corretamente");
            return null;
        }

        Optional<String> occurance = findAnyOcurrance(veiculos.stream(), input);

        String result = occurance.orElse(null);
        return map.get(result);
    }

    private String identificarVeiculoPorNumero() {
        String input = getInput();
        Integer optionNumber = getIntInRange(1, 3, input);
        if (optionNumber != null) {
            switch (optionNumber) {
                case 1:
                    return "carros";
                case 2:
                    return "motos";
                case 3:
                    return "caminhoes";
            }
        }
        System.out.println("numero invalido");
        return null;
    }

    public void menu() {
        System.out.println("""
                ==== OPÇÕES ====
                1 - Carro
                2 - Moto
                3 - Caminhão
                
                Digite uma das opções""");

        String categoriaOption = null;

        while (categoriaOption == null) {
            if (scanner.hasNextInt()) {
                categoriaOption = identificarVeiculoPorNumero();
            } else {
                categoriaOption = identificarVeiculoPorNome();
            }
        }
        var json = consumoApi.obterDados(fipeUrlConverter.search(categoriaOption));
        List<Dados> marcas = converteDados.obterLista(json, Dados.class);

        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.print("Digite o codigo da marca desejada : ");
        String input = getInput();
        while(numberOfOcurrances(marcas.stream().map(Dados::codigo), input) != 1L) {
            System.out.print("codigo invalido, tente novamente : ");
            input = getInput();
        }

        String codigoMarca = findAnyOcurrance(marcas.stream().map(Dados::codigo), input).orElse(null);

        if (codigoMarca == null) {
            System.out.println("erro inesperado 1");
            throw new RuntimeException();
        }

        json = consumoApi.obterDados(fipeUrlConverter.search(categoriaOption, codigoMarca));
        System.out.println(json);
        List<Dados> tiposVeiculo = converteDados.obterLista(json, Dados.class);
        tiposVeiculo.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);
    }
}
