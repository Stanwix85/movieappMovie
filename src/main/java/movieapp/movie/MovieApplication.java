package movieapp.movie;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import movieapp.movie.service.MovieService;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MovieApplication {

    @Bean
    CommandLineRunner run(MovieService movieService) {
        return args -> {
            System.out.println("=== Testing MovieService Live Mapping ===");

            // Fetch Movie ID 11 (Star Wars) with credits and map to MovieResponseDto
            movieService.getMovieByTmdbId(11L).ifPresent(dto -> {
                System.out.println("Title: " + dto.getTitle());
                System.out.println("Director: " + dto.getDirector());
                System.out.println("Release Date: " + dto.getReleaseDate());
                System.out.println("Genres (capped at 3): " + dto.getGenre());
                System.out.println("Actors (capped at 6): " + dto.getActors());
                System.out.println("Rating: " + dto.getRating());
            });

            System.out.println("=========================================");
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
