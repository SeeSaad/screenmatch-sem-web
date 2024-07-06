package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadoSerie;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		File file = new File("key/omdbKey.txt");
		String omdbApiKey;

		try {
			Scanner scanner = new Scanner(file);

			omdbApiKey = scanner.nextLine();
		} catch (FileNotFoundException | NoSuchElementException | IllegalStateException e){
			System.out.println(e.getMessage());
			throw new RuntimeException();
		}

		System.out.println("Primeiro projeto Spring sem Web.");
		ConsumoApi consumoApi = new ConsumoApi();
		String json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=" + omdbApiKey);
		ConverteDados conversor = new ConverteDados();
		DadoSerie dados = conversor.obterDados(json, DadoSerie.class);
		System.out.println(dados);
	}
}
