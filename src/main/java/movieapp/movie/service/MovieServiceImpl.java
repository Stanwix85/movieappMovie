package movieapp.movie.service;

import movieapp.movie.client.TmdbClient;
import movieapp.movie.dao.MovieRepository;
import movieapp.movie.dao.MovieRepositoryJdbcImpl;
import movieapp.movie.dto.MovieResponseDto;
import movieapp.movie.dto.TmdbMovieResponse;
import movieapp.movie.entities.Movie;
import movieapp.movie.mappers.MovieMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.UUID;

@Service
public class MovieServiceImpl implements  MovieService{

    private final TmdbClient tmdbClient;
    private final MovieMapper movieMapper;
    private final MovieRepository movieRepository;


    public MovieServiceImpl(TmdbClient tmdbClient, MovieMapper movieMapper, MovieRepository movieRepository){
        this.tmdbClient = tmdbClient;
        this.movieMapper = movieMapper;
        this.movieRepository = movieRepository;

    }
    @Override
    public Optional<MovieResponseDto> getMovieByTmdbId(Long tmdbId) {
        if(tmdbId == null){
            return Optional.empty();
        }
        String movieStr = String.valueOf(tmdbId);

        Optional<Movie> existingMovie = movieRepository.findByMovieId(movieStr);
        if (existingMovie.isPresent()) {
            return existingMovie.map(movieMapper::toDto);
        }
        return tmdbClient.fetchMoviesWithCredit(tmdbId)
                .map(tmdbResponse -> {
                        Movie movieToSave = movieMapper.toEntity(movieMapper.toDto(tmdbResponse));
                        Movie savedMovie = movieRepository.save(movieToSave);
                        return movieMapper.toDto(savedMovie);
                });
    }

    @Override
    public List<TmdbMovieResponse> searchMovies(String query){
        if(query == null || query.isBlank()) {
            return List.of();
        }
        return tmdbClient.searchMovies(query)
                .map(response -> response.results() != null ? response.results() : List.<TmdbMovieResponse>of())
                .orElse(List.of());
    }

    @Override
    public Optional<MovieResponseDto> getMovieById( UUID id){
        if (id == null) {
            return Optional.empty();
        }
        return movieRepository.findById(id)
                .map(movieMapper:: toDto);
    }
    @Override
    public List<MovieResponseDto> getAllMovies(){
        return movieRepository.findAll()
                .stream()
                .map(movieMapper::toDto)
                .toList();
    }
    @Override
    public boolean deleteMovie(UUID id) {
        if(id == null){
            return false;
        }
        if(movieRepository.findById(id).isEmpty()) {
            return false;
        }
        movieRepository.deleteById(id);
        return true;

    }

}
