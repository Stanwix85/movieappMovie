package movieapp.movie.service;

import movieapp.movie.client.TmdbClient;
import movieapp.movie.dto.MovieResponseDto;
import movieapp.movie.dto.TmdbMovieResponse;
import movieapp.movie.mappers.MovieMapper;
import movieapp.movie.service.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceImplTest {

    @Mock
    private TmdbClient tmdbClient;

    @Mock
    private MovieMapper movieMapper;

    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieServiceImpl(tmdbClient, movieMapper);
    }

    @Test
    void shouldReturnMovieResponseDtoWhenMovieExists() {
        // Given
        Long tmdbId = 11L;
        TmdbMovieResponse mockTmdbResponse = mock(TmdbMovieResponse.class);
        MovieResponseDto expectedDto = new MovieResponseDto();
        expectedDto.setTitle("Star Wars");

        when(tmdbClient.fetchMoviesWithCredit(tmdbId)).thenReturn(Optional.of(mockTmdbResponse));
        when(movieMapper.toDto(mockTmdbResponse)).thenReturn(expectedDto);

        // When
        Optional<MovieResponseDto> result = movieService.getMovieByTmdbId(tmdbId);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Star Wars", result.get().getTitle());
        verify(tmdbClient, times(1)).fetchMoviesWithCredit(tmdbId);
        verify(movieMapper, times(1)).toDto(mockTmdbResponse);
    }

    @Test
    void shouldReturnEmptyOptionalWhenMovieNotFound() {
        // Given
        Long tmdbId = 999999L;
        when(tmdbClient.fetchMoviesWithCredit(tmdbId)).thenReturn(Optional.empty());

        // When
        Optional<MovieResponseDto> result = movieService.getMovieByTmdbId(tmdbId);

        // Then
        assertTrue(result.isEmpty());
        verify(tmdbClient, times(1)).fetchMoviesWithCredit(tmdbId);
        verifyNoInteractions(movieMapper);
    }
}