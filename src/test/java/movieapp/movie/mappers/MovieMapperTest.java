package movieapp.movie.mappers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import movieapp.movie.dto.TmdbMovieResponse;
import movieapp.movie.dto.MovieResponseDto;
import movieapp.movie.entities.Movie;

import java.util.List;

public class MovieMapperTest {
    private MovieMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new MovieMapper();
    }

    @Test
    void shouldExtractDirector(){
        var crew = List.of(
                new TmdbMovieResponse.TmdbCrewMember("Jane Writer", "Writer"),
                new TmdbMovieResponse.TmdbCrewMember("Chris Nolan", "Director"),
                new TmdbMovieResponse.TmdbCrewMember("John Producer", "Producer")
        );
        var credits = new TmdbMovieResponse.TmdbCredits(List.of(), crew);
        var tmdbMovie = new TmdbMovieResponse(
                101L, "tt1234", "INception", "overview", "Tagline", "/poster.jpg", "2010-07-16", 148, 8.8, List.of(),credits
        );
        String director = mapper.extractDirector(tmdbMovie);

        assertEquals("Chris Nolan", director);
    }

    @Test
    void shouldLimitActorsToSix(){
        var cast = List.of(
                new TmdbMovieResponse.TmdbCastMember("Actor 1", "Char 1"),
                new TmdbMovieResponse.TmdbCastMember("Actor 2", "Char 2"),
                new TmdbMovieResponse.TmdbCastMember("Actor 3", "Char 3"),
                new TmdbMovieResponse.TmdbCastMember("Actor 4", "Char 4"),
                new TmdbMovieResponse.TmdbCastMember("Actor 5", "Char 5"),
                new TmdbMovieResponse.TmdbCastMember("Actor 6", "Char 6"),
                new TmdbMovieResponse.TmdbCastMember("Actor 7", "Char 7"),
                new TmdbMovieResponse.TmdbCastMember("Actor 8", "Char 8")
        );
        var credits = new TmdbMovieResponse.TmdbCredits(cast, List.of());
        var tmdbMovie = new TmdbMovieResponse(
                101L, "tt123", "Movie", "Overview", "Tagline",
                "/poster.jpg", "2020-01-01", 120, 7.5, List.of(), credits
        );
        List<String> actors = mapper.extractActors(tmdbMovie);

        assertEquals(6, actors.size());
        assertEquals("Actor 1", actors.get(0));
        assertEquals("Actor 6", actors.get(5));
    }

    @Test
    void shouldLimitGenresToThree(){
        var genres = List.of(
                new TmdbMovieResponse.TmdbGenre(1, "Action"),
                new TmdbMovieResponse.TmdbGenre(2, "Sci-Fi"),
                new TmdbMovieResponse.TmdbGenre(3, "Thriller"),
                new TmdbMovieResponse.TmdbGenre(4, "Adventure"),
                new TmdbMovieResponse.TmdbGenre(5, "Drama")
        );
        var tmdbMovie = new TmdbMovieResponse(
                101L, "tt123", "Movie", "Overview", "Tagline",
                "/poster.jpg", "2020-01-01", 120, 7.5, genres, null
        );
        List<String> result = mapper.extractedGenres(tmdbMovie);

        assertEquals(3, result.size());
        assertEquals(List.of("Action", "Sci-Fi", "Thriller"), result);
    }
    @Test
    void shouldHandleNullCreditsAndGenresGracefully(){
        var tmdbMovie = new TmdbMovieResponse(
                101L, "tt123", "Empty Movie", "Overview", "Tagline",
                "/poster.jpg", "2020-01-01", 120, 7.5, null, null
        );
        assertDoesNotThrow(()-> {
            assertNull(mapper.extractDirector(tmdbMovie));
            assertTrue(mapper.extractActors(tmdbMovie).isEmpty());
            assertTrue(mapper.extractedGenres(tmdbMovie).isEmpty());
        });
    }
}
