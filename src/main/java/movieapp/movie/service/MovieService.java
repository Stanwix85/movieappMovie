package movieapp.movie.service;

import movieapp.movie.dto.MovieResponseDto;
import movieapp.movie.dto.TmdbMovieResponse;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    Optional<MovieResponseDto> getMovieByTmdbId(Long tmdbId);

    List<TmdbMovieResponse> searchMovies(String query);
}
