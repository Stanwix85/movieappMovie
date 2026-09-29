package movieapp.movie.service;

import movieapp.movie.dto.MovieResponseDto;
import movieapp.movie.dto.TmdbMovieResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieService {
    Optional<MovieResponseDto> getMovieByTmdbId(Long tmdbId);

    List<TmdbMovieResponse> searchMovies(String query);

    Optional<MovieResponseDto> getMovieById( UUID id);

    List<MovieResponseDto> getAllMovies();

    boolean deleteMovie(UUID id);
}
