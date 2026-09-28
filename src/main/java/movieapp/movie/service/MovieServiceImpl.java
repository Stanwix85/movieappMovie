package movieapp.movie.service;

import movieapp.movie.client.TmdbClient;
import movieapp.movie.dto.MovieResponseDto;
import movieapp.movie.dto.TmdbMovieResponse;
import movieapp.movie.mappers.MovieMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements  MovieService{

    private final TmdbClient tmdbClient;
    private final MovieMapper movieMapper;

    public MovieServiceImpl(TmdbClient tmdbClient, MovieMapper movieMapper){
        this.tmdbClient = tmdbClient;
        this.movieMapper = movieMapper;
    }
    @Override
    public Optional<MovieResponseDto> getMovieByTmdbId(Long tmdbId) {
        return tmdbClient.fetchMoviesWithCredit(tmdbId)
                .map(movieMapper::toDto);
    }

    @Override
    public List<TmdbMovieResponse> searchMovies(String query){
        return tmdbClient.searchMovies(query)
                .map(response -> response.results().stream()
                .toList())
                .orElse(List.of());
    }

}
