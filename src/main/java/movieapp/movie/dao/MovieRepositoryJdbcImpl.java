package movieapp.movie.dao;

import movieapp.movie.entities.Movie;

import movieapp.movie.mappers.MovieMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MovieRepositoryJdbcImpl implements MovieRepository{
    private final JdbcClient jdbcClient;
    private final RowMapper<Movie> movieRowMapper;

    public MovieRepositoryJdbcImpl(JdbcClient jdbcClient) {

        this.jdbcClient = jdbcClient;
        this.movieRowMapper = (rs, rowNum) -> {
            Movie movie = new Movie();
            movie.setId(UUID.fromString(rs.getString("id")));
            movie.setMovieId(rs.getString("movie_id"));
            movie.setTitle(rs.getString("title"));
            movie.setDirector(rs.getString("director"));
            movie.setAgeRating(rs.getString("age_rating"));
            movie.setRunTime(rs.getObject("run_time", Integer.class));
            movie.setRating(rs.getObject("rating", Double.class));

            var releaseDate = rs.getDate("release_date");
            if (releaseDate != null) {
                movie.setReleaseDate(releaseDate.toLocalDate());
            }
            movie.setTagline(rs.getString("tagline"));
            movie.setSynopsis(rs.getString("synopsis"));
            movie.setPoster(rs.getString("poster"));

            return movie;

        };
    }

    @Override
    public boolean existByMovieId(String movieId) {
        Integer count = jdbcClient.sql("SELECT COUNT(1) FROM movies WHERE movie_id = ?")
                .param(movieId)
                .query(Integer.class)
                .single();
        return count != null && count > 0;
    }
    @Override
    public Optional<Movie> findById(UUID id){
        if(id == null){
            return Optional.empty();
        }
        Optional<Movie> movieOpt = jdbcClient.sql("SELECT * FROM movies WHERE id = ?")
                .param( id.toString())
                .query(movieRowMapper)
                .optional();
        movieOpt.ifPresent(this::populateGenresAndActors);
        return movieOpt;
    }
    @Override
    public Optional<Movie> findByMovieId(String movieId){
        if (movieId == null || movieId.isBlank()){
            return Optional.empty();
        }
        Optional<Movie> movieOpt =  jdbcClient.sql("SELECT * FROM movies WHERE movie_id = ?")
                .param(movieId)
                .query(movieRowMapper)
                .optional();
        movieOpt.ifPresent(this::populateGenresAndActors);
        return movieOpt;

    }
    @Override
    public List<Movie> findAll(){
        List<Movie> movieList = jdbcClient.sql("SELECT * FROM movies")
                .query(movieRowMapper)
                .list();
        movieList.forEach(this::populateGenresAndActors);
        return movieList;

    }
    @Override
    @Transactional
    public void deleteById(UUID id){
        if(id == null) {
            return;
        }
        jdbcClient.sql("DELETE FROM movies WHERE id = ?")
                .param(id.toString())
                .update();
    }

    @Override
    @Transactional
    public Movie save(Movie movie) {
        Optional<Movie> existing = findByMovieId(movie.getMovieId());
        if(existing.isPresent()){
            movie.setId(existing.get().getId());
        } else if(movie.getId() == null) {
            movie.setId(UUID.randomUUID());
        }
        String movieSql = """
        INSERT INTO movies (id, movie_id, title, director, age_rating, run_time, rating, release_date, tagline, synopsis, poster)
        VALUES (:id, :movieId, :title, :director, :ageRating, :runTime, :rating, :releaseDate, :tagline, :synopsis, :poster)
        ON DUPLICATE KEY UPDATE
            title = VALUES(title),
            director = VALUES(director),
            age_rating = VALUES(age_rating),
            run_time = VALUES(run_time),
            rating = VALUES(rating),
            release_date = VALUES(release_date),
            tagline = VALUES(tagline),
            synopsis = VALUES(synopsis),
            poster = VALUES(poster)
    """;
        jdbcClient.sql(movieSql)
                .param("id", movie.getId().toString())
                .param("movieId", movie.getMovieId())
                .param("title", movie.getTitle())
                .param("director", movie.getDirector())
                .param("ageRating", movie.getAgeRating())
                .param("runTime", movie.getRunTime())
                .param("rating", movie.getRating())
                .param("releaseDate", movie.getReleaseDate())
                .param("tagline", movie.getTagline())
                .param("synopsis", movie.getSynopsis())
                .param("poster", movie.getPoster())
                .update();

        saveGenres(movie);
        saveActors(movie);


        return movie;
    }
    private void populateGenresAndActors(Movie movie){
        List<String> genres = jdbcClient.sql("""
            SELECT g.name FROM genres g 
            JOIN movie_genres mg ON g.id = mg.genre_id
            WHERE mg.movie_id = ? """)
                .param(movie.getId().toString())
                .query(String.class)
                .list();
        movie.setGenre(genres);

        List<String> actors = jdbcClient.sql("""
            SELECT a.name FROM actors a 
            JOIN movie_actors ma ON a.id = ma.actor_id
            WHERE ma.movie_id = ? """)
                .param(movie.getId().toString())
                .query(String.class)
                .list();
        movie.setActors(actors);

    }
    private void saveGenres(Movie movie){
        if (movie.getGenre() == null || movie.getGenre().isEmpty()) {
            return;
        }
        for (String genreName : movie.getGenre()) {
            jdbcClient.sql("INSERT IGNORE INTO genres (name) VALUES (?)")
                    .param(genreName)
                    .update();

        Integer genreId = jdbcClient.sql("SELECT id FROM genres WHERE name = ?")
                .param(genreName)
                .query(Integer.class)
                .single();
        jdbcClient.sql("INSERT IGNORE INTO movie_genres (movie_id, genre_id) VALUES (?,?)")
                .param(movie.getId().toString())
                .param(genreId)
                .update();
    }}

    private void saveActors(Movie movie){
        if (movie.getActors() == null || movie.getActors().isEmpty()){
            return;
        }
        for (String actorName : movie.getActors()) {
            jdbcClient.sql("INSERT IGNORE INTO actors (name) VALUES (?)")
                    .param(actorName)
                    .update();
            Integer actorId = jdbcClient.sql("SELECT id FROM actors WHERE name = ?")
                    .param(actorName)
                    .query(Integer.class)
                    .single();
            jdbcClient.sql("INSERT IGNORE INTO movie_actors (movie_id, actor_id) VALUES (?,?)")
                    .param(movie.getId().toString())
                    .param(actorId)
                    .update();
        }
    }
}
