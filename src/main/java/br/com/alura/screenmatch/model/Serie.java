package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.ConsultaChatGPT;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, name = "nomeSerie")
    private String titulo;
    private Integer totalTemporadas;
    private Double avaliacao;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
//    private List<String> atores;
    private String atores;
    private String sinopse;
    private String poster;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();

    public Serie() {}

    public Serie(DadosSerie dadosSerie) {
        titulo = dadosSerie.titulo();
        totalTemporadas = dadosSerie.totalTemporadas();

        try {
            avaliacao = Double.parseDouble(dadosSerie.avaliacao());
        } catch (NumberFormatException e) {
            avaliacao = null;
        }

        atores = dadosSerie.atores();

        genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());

        poster = dadosSerie.poster();
        sinopse = ConsultaChatGPT.obterTraducao(dadosSerie.sinopse()).trim();
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public String getAtores() {
        return atores;
    }

    public String getSinopse() {
        return sinopse;
    }

    public String getPoster() {
        return poster;
    }

    @Override
    public String toString() {
        return
                "genero= " + genero +
                "\ntitulo= " + titulo +
                ", totalTemporadas= " + totalTemporadas +
                ", avaliacao= " + avaliacao +
                "\natores= " + atores +
                "\nsinopse= " + sinopse +
                "\nposter= " + poster;
    }
}