package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String titulo);

    List<Serie> findByAtoresContainingIgnoreCase(String nomeAtor);
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double valor);

    List<Serie> findTop5ByOrderByAvaliacao();

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    @Query(value = "select * from series WHERE series.total_temporadas <= 5 AND series.avaliacao >= 7.5", nativeQuery = true)
    List<Serie> buscaPorTemporadaEAvaliacao();

    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie> buscaPorTemporadaEAvaliacaoJPQL(int totalTemporadas, double avaliacao);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s == :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosDaSerie(Serie serie);
}
