package movieapp.movie.dao;

import movieapp.movie.entities.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MovieRespositoryJdbcImplTest {

    @Autowired
    private MovieRepository movieRepository;

    private Movie testMovie;

    @BeforeEach
    void setUp() {
        testMovie = new Movie();
        testMovie.setMovieId("550");
        testMovie.setTitle("Fight Club");
        testMovie.setDirector("David Fincher");
        testMovie.setRunTime(139);
        testMovie.setRating(8.4);
        testMovie.setReleaseDate(LocalDate.of(1999, 10, 15));
        testMovie.setTagline("Mischief. Mayhem. Soap.");
        testMovie.setSynopsis("An insomniac office worker and a devil-may-care soap maker form an underground fight club.");
        testMovie.setPoster("/pB8BM7pdSp6B6Ih7QZ4DrQ3PmJK.jpg");
        testMovie.setGenre(List.of("Drama"));
        testMovie.setActors(List.of("Brad Pitt", "Edward Norton", "Helena Bonham Carter"));
    }

    @Test
    @DisplayName("Should save a movie and retrieve it by TMDB movie_id with actors and genres")
    void shouldSaveAndFindByMovieId() {
        Movie saved = movieRepository.save(testMovie);

        assertThat(saved.getId()).isNotNull();

        Optional<Movie> found = movieRepository.findByMovieId("550");

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Fight Club");
        assertThat(found.get().getGenre()).contains("Drama");
        assertThat(found.get().getActors()).contains("Brad Pitt", "Edward Norton");
    }

    @Test
    @DisplayName("Should delete a movie and cascade remove join records")
    void shouldDeleteById() {
        Movie saved = movieRepository.save(testMovie);
        UUID id = saved.getId();

        movieRepository.deleteById(id);

        Optional<Movie> found = movieRepository.findById(id);
        assertThat(found).isEmpty();
    }
}