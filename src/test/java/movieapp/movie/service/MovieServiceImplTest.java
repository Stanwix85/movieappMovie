package movieapp.movie.service;

import movieapp.movie.client.TmdbClient;
import movieapp.movie.dao.MovieRepository;
import movieapp.movie.dto.MovieResponseDto;
import movieapp.movie.dto.TmdbMovieResponse;
import movieapp.movie.entities.Movie;
import movieapp.movie.mappers.MovieMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private TmdbClient tmdbClient;

    @Spy
    private MovieMapper movieMapper;

    @InjectMocks
    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initializes @Mock, @Spy, and @InjectMocks
    }

    @Test
    @DisplayName("Cache Hit: When movie exists locally, return from DB and NEVER call TMDB")
    void shouldReturnFromDatabaseOnCacheHit() {
        Long tmdbId = 11L;
        String movieIdStr = "11";

        Movie localMovie = new Movie();
        localMovie.setId(UUID.randomUUID());
        localMovie.setMovieId(movieIdStr);
        localMovie.setTitle("Star Wars: A New Hope");

        when(movieRepository.findByMovieId(movieIdStr)).thenReturn(Optional.of(localMovie));

        Optional<MovieResponseDto> result = movieService.getMovieByTmdbId(tmdbId);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Star Wars: A New Hope");

        verify(movieRepository, times(1)).findByMovieId(movieIdStr);
        verifyNoInteractions(tmdbClient);
        verify(movieRepository, never()).save(any());
    }

    @Test
    @DisplayName("Cache Miss: When movie is NOT in DB, fetch from TMDB, save locally, and return DTO")
    void shouldFetchFromTmdbAndSaveOnCacheMiss() {
        Long tmdbId = 11L;
        String movieIdStr = "11";

        when(movieRepository.findByMovieId(movieIdStr)).thenReturn(Optional.empty());

        TmdbMovieResponse tmdbResponse = new TmdbMovieResponse(
                11L, "1a23","Star Wars", "A long time ago...", "May the Force be with you", "/poster.jpg",
                "1977-05-25", 121, 8.2,
                List.of(), null
        );
        when(tmdbClient.fetchMoviesWithCredit(tmdbId)).thenReturn(Optional.of(tmdbResponse));

        Movie savedMovie = new Movie();
        savedMovie.setId(UUID.randomUUID());
        savedMovie.setMovieId(movieIdStr);
        savedMovie.setTitle("Star Wars");
        when(movieRepository.save(any(Movie.class))).thenReturn(savedMovie);

        Optional<MovieResponseDto> result = movieService.getMovieByTmdbId(tmdbId);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Star Wars");

        verify(movieRepository, times(1)).findByMovieId(movieIdStr);
        verify(tmdbClient, times(1)).fetchMoviesWithCredit(tmdbId);
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    @DisplayName("Delete Movie: Returns true when found and deleted, false when not found")
    void shouldHandleDeleteMovie() {
        UUID id = UUID.randomUUID();

        when(movieRepository.findById(id)).thenReturn(Optional.of(new Movie()));
        boolean deleted = movieService.deleteMovie(id);

        assertThat(deleted).isTrue();
        verify(movieRepository, times(1)).deleteById(id);

        UUID unknownId = UUID.randomUUID();
        when(movieRepository.findById(unknownId)).thenReturn(Optional.empty());
        boolean notDeleted = movieService.deleteMovie(unknownId);

        assertThat(notDeleted).isFalse();
        verify(movieRepository, never()).deleteById(unknownId);
    }
}