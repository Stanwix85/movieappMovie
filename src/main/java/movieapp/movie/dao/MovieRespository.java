package movieapp.movie.dao;

import movieapp.movie.entities.Movie;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MovieRespository {

    Movie save(Movie movie);

    Optional<Movie> findByID(UUID id);
    Optional<Movie> findByMovieId(String movieId);

    List<Movie> findAll();

    void deleteById(UUID id);

    boolean existByMovieId(String movieId);
}
