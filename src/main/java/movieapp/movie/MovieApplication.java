package movieapp.movie;

import io.github.cdimascio.dotenv.Dotenv;
import movieapp.movie.client.TmdbClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MovieApplication {

    @Bean
    CommandLineRunner run(TmdbClient tmdbClient) {
        return args -> {
            System.out.println("--- TESTING TMDB CLIENT ---");

            // 1. Test search
            var searchResult = tmdbClient.searchMovies("Alien");
            searchResult.ifPresent(res ->
                    System.out.println("Search Found: " + res.results().size() + " films. First: " + res.results().get(0).title())
            );

            // 2. Test movie details with credits (e.g. Star Wars ID = 11)
            var movieResult = tmdbClient.fetchMoviesWithCredit(11L);
            movieResult.ifPresent(movie -> {
                System.out.println("Fetched Movie: " + movie.title());
                System.out.println("Tagline: " + movie.tagline());
            });

            System.out.println("--- TEST COMPLETE ---");
        };
    }

    static {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry -> {
            if (System.getProperty(entry.getKey()) == null) {
                System.setProperty(entry.getKey(), entry.getValue());
            }
        });
    }

    public static void main(String[] args) {
        SpringApplication.run(MovieApplication.class, args);
    }
}
