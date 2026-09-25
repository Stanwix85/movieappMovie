package movieapp.movie.client;

import movieapp.movie.dto.MovieResponseDto;
import movieapp.movie.dto.TmdbMovieDetailResponse;
import movieapp.movie.dto.TmdbMovieResponse;
import movieapp.movie.dto.TmdbSearchResponse;

import java.util.Optional;

public interface TmdbClient {
    Optional<TmdbSearchResponse> searchMovies(String query);

    Optional<TmdbMovieResponse> fetchMovieById(Long tmdbId);

    Optional<TmdbMovieResponse> fetchMoviesWithCredit(Long tmdbId);
}
