package br.com.alura.screenmatch.service;

public class OmdbApiUrlConverter {
    private final String omdbapi = "&apikey=" + System.getenv("OMDB_KEY");
    private final String partialUrl = "https://www.omdbapi.com/?";

    public String search(String title) {
        String titleUrl = "t=" + title.replace(' ', '+');
        return partialUrl + titleUrl + omdbapi;
    }

    public String search(String title, int season) {
        String titleUrl = "t=" + title.replace(' ', '+');
        String seasonUrl = "&season=" + String.valueOf(season);
        return partialUrl + titleUrl + seasonUrl + omdbapi;
    }

    public String search(String title, int season, int episode) {
        String titleUrl = "t=" + title.replace(' ', '+');
        String seasonUrl = "&season=" + String.valueOf(season);
        String episodeUrl = "&episode=" + String.valueOf(episode);
        return partialUrl + titleUrl + seasonUrl + omdbapi;
    }

}
