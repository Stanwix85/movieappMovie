package movieapp.movie;

import io.github.cdimascio.dotenv.Dotenv;
import movieapp.movie.client.TmdbClient;
import movieapp.movie.dao.MovieRepository;
import movieapp.movie.dto.MovieResponseDto;
import movieapp.movie.dto.TmdbMovieResponse;
import movieapp.movie.entities.Movie;
import movieapp.movie.mappers.MovieMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.context.annotation.Bean;
import movieapp.movie.service.MovieService;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class MovieApplication {






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
