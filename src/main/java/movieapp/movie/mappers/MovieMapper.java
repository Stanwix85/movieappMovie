package movieapp.movie.mappers;

import movieapp.movie.entities.Movie;
import movieapp.movie.dto.MovieResponseDto;
import movieapp.movie.dto.TmdbMovieResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieMapper {
    public Movie toEntity(MovieResponseDto dto) {
        if (dto == null) {
            return null;
        }
        Movie movie = new Movie();
        movie.setId(dto.getId());
        movie.setMovieId(dto.getMovieId());
        movie.setTitle(dto.getTitle());
        movie.setDirector(dto.getDirector());
        movie.setAgeRating(dto.getAgeRating());
        movie.setRunTime(dto.getRunTime());
        movie.setRating(dto.getRating());
        movie.setGenre(dto.getGenre());
        movie.setReleaseDate(dto.getReleaseDate());
        movie.setTagline(dto.getTagline());
        movie.setSynopsis(dto.getSynopsis());
        movie.setActors(dto.getActors());
        movie.setPoster(dto.getPoster());
        return movie;
    }
    public MovieResponseDto toDto(Movie entity) {
        if(entity == null) {
            return null;
        }
        MovieResponseDto dto = new MovieResponseDto();
        dto.setId(entity.getId());
        dto.setMovieId(entity.getMovieId());
        dto.setTitle(entity.getTitle());
        dto.setDirector(entity.getDirector());
        dto.setAgeRating(entity.getAgeRating());
        dto.setRunTime(entity.getRunTime());
        dto.setRating(entity.getRating());
        dto.setGenre(entity.getGenre());
        dto.setReleaseDate(entity.getReleaseDate());
        dto.setTagline(entity.getTagline());
        dto.setSynopsis(entity.getSynopsis());
        dto.setActors(entity.getActors());
        dto.setPoster(entity.getPoster());
        return dto;


    }
    public MovieResponseDto toDto(TmdbMovieResponse tmdb) {
        if (tmdb == null) {
            return null;
        }
        MovieResponseDto dto = new MovieResponseDto();
        dto.setMovieId(tmdb.id() != null ? String.valueOf(tmdb.id()) : null);
        dto.setTitle(tmdb.title());
        dto.setDirector(extractDirector(tmdb));
        dto.setRunTime(tmdb.runTime());
        dto.setRating(tmdb.voteAverage());
        dto.setGenre(extractedGenres(tmdb));
        dto.setTagline(tmdb.tagline());
        dto.setSynopsis(tmdb.overview());
        dto.setActors(extractActors(tmdb));
        dto.setPoster(tmdb.posterPath());

        if (tmdb.releaseDate() != null && !tmdb.releaseDate().isBlank()) {
            dto.setReleaseDate(java.time.LocalDate.parse(tmdb.releaseDate()));
        }

        return dto;
    }


    public List<String> extractedGenres(TmdbMovieResponse tmdbMovie) {
        if (tmdbMovie.genres() == null) {
            return List.of();
        }
        return tmdbMovie.genres().stream()
                .map(TmdbMovieResponse.TmdbGenre::name)
                .filter(name -> name != null && !name.isBlank())
                .limit(3)
                .toList();

    }
    public List<String> extractActors(TmdbMovieResponse tmdbMovie) {
    if(tmdbMovie.credits() == null || tmdbMovie.credits().cast() == null){
        return List.of();

    }
    return tmdbMovie.credits().cast().stream()
            .map(TmdbMovieResponse.TmdbCastMember::name)
            .filter(name -> name != null && !name.isBlank())
            .limit(6)
            .toList();
    }
    public String extractDirector(TmdbMovieResponse tmdbMovie) {
        if (tmdbMovie.credits() == null || tmdbMovie.credits().crew() == null) {
            return null;
        }
        return tmdbMovie.credits().crew().stream()
                .filter(crew -> "Director".equalsIgnoreCase(crew.job()))
                .map(TmdbMovieResponse.TmdbCrewMember::name)
                .findFirst()
                .orElse(null);
    }
}
