package br.com.alura.screenmatch.user;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import br.com.alura.screenmatch.service.OmdbApiUrlConverter;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private final Scanner scanner = new Scanner(System.in);
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    private final OmdbApiUrlConverter omdbUrlConverter = new OmdbApiUrlConverter();

    private final SerieRepository repositorio;
    private List<Serie> listaDeSeries = new ArrayList<>();

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    private String getInput() {
        return scanner.nextLine();
    }

    private void buscarSerie() {
        System.out.print("Digite o nome da série para a busca : ");
        String nomeSerie = getInput();

        String json = consumoApi.obterDados(omdbUrlConverter.search(nomeSerie));
        DadosSerie dadosSerie = conversor.obterDados(json, DadosSerie.class);

        if (dadosSerie.titulo() == null) {
            System.out.println("Erro na busca");
        } else if (dadosSerie.totalTemporadas() == null) {
            System.out.println("Não é um filme!");
        } else {
            System.out.println(dadosSerie);
            Serie serie = new Serie(dadosSerie);
            repositorio.save(serie);
        }
    }

    private void buscarEpisodios() {
        listaDeSeries.stream()
                .map(s -> s.getTitulo().toUpperCase())
                .forEach(System.out::println);

        System.out.print("Digite o nome da série para a busca : ");
        String nomeSerie = getInput();

        String json;

        if (listaDeSeries.stream().filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase())).count() != 1) {
            System.out.println("Nenhuma serie específica encontrada");
            return;
        }

        Optional<Serie> optionalSerie = listaDeSeries.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase())).findFirst();

        if (optionalSerie.isPresent()) {

            var serie = optionalSerie.get();

            List<DadosTemporada> listaDeTemporadas = new ArrayList<>();
            for (int i = 1; i <= serie.getTotalTemporadas(); i++) {
                json = consumoApi.obterDados(omdbUrlConverter.search(nomeSerie, i));
                listaDeTemporadas.add(conversor.obterDados(json, DadosTemporada.class));
            }

            List<Episodio> listaDeEpisodios = listaDeTemporadas.stream()
                    .flatMap(t -> t.episodios().stream().map(e -> new Episodio(t.numero(), e)))
                    .toList();

            serie.setEpisodios(listaDeEpisodios);
            repositorio.save(serie);

            listaDeEpisodios
                    .stream().sorted(Comparator.comparing(Episodio::getTemporada))
                    .forEach(System.out::println);
        }
    }

    private void listarSeriesBuscadas() {
        listaDeSeries.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach( s -> System.out.println(s + "\n\n"));
    }

    private void waitForInput() {
        System.out.print("[Enter] para continuar");
        scanner.nextLine();
    }

    public void menu() {
        int opcao = -1;

        while(opcao != 0) {
            listaDeSeries = repositorio.findAll();
            System.out.println("""
                    ============================
                    1 - Buscar séries
                    2 - Buscar episodios
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                    5 - Buscar série por ator
                    6 - Buscar Top 5 séries
                    7 - Buscar por qtd de temporadas e avaliação
                    8 - Buscar episodio por trecho
                    
                    0 - Sair
                    ============================""");

            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine();
            } else scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerie();
                    waitForInput();
                    break;
                case 2:
                    buscarEpisodios();
                    waitForInput();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    waitForInput();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    waitForInput();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    waitForInput();
                    break;
                case 6:
                    buscarTop5Series();
                    waitForInput();
                    break;
                case 7:
                    buscarTemporadasEAvaliacao();
                    waitForInput();
                    break;
                case 8:
                    buscarEpisodioPorTrecho();
                    waitForInput();
                    break;
                case 0:
                    break;
            }
        }
    }

    private void topEpisodiosPorSerie() {
        System.out.print("Digite o nome da série para a busca : ");
        String nomeSerie = getInput();

        Optional<Serie> optionalSerie =
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite o nome do episódio para a busca: ");
        String trechoEpisodio = getInput();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);

        episodiosEncontrados.forEach(e -> System.out.println(e.getSerie().getTitulo().toUpperCase() + " - " + e));
    }

    private void buscarTemporadasEAvaliacao() {
        Integer numTemporadas;
        Double avaliacao;

        System.out.println("Digite o numero de temporadas");
        while (!scanner.hasNextInt()) {
            System.out.println("Invalido, tente novamente");
            scanner.nextLine();
        }
        numTemporadas = scanner.nextInt();

        System.out.println("Digite a avaliação a ser buscada");
        while (!scanner.hasNextDouble()) {
            System.out.println("Invalido, tente novamente");
            scanner.nextLine();
        }
        avaliacao = scanner.nextDouble();

        List<Serie> listaEncontrada = repositorio.buscaPorTemporadaEAvaliacaoJPQL(numTemporadas, avaliacao);

        listaEncontrada.forEach(s -> System.out.println(s.getTitulo() + " " + s.getTotalTemporadas() + " " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> top5series = repositorio.findTop5ByOrderByAvaliacaoDesc();
        top5series.forEach(s -> System.out.println(s.getTitulo().toUpperCase() + " [" + s.getAvaliacao() + "]"));
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome do ator para a busca: ");
        String nomeAtor = getInput();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCase(nomeAtor);
        System.out.println("Series encontradas:");
        seriesEncontradas.forEach(s -> System.out.println(s.getTitulo()));
    }

    private void buscarSeriePorTitulo() {
        System.out.print("Digite o nome da série para a busca : ");
        String nomeSerie = getInput();

        Optional<Serie> optionalSerie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (optionalSerie.isPresent()) {
            System.out.println("Dados da série: " + optionalSerie.get());
        } else {
            System.out.println("Serie não encontrada");
        }
    }
}
