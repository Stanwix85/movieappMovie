package movieapp.movie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbMovieResponse(
        Long id,
         @JsonProperty("imbd_id") String imbdId,
         String title,
         String overview,
         String tagline,
         @JsonProperty("poster_path")String posterPath,
         @JsonProperty("release_date") String releaseDate,
         Integer runTime,
        @JsonProperty("vote_average") Double voteAverage,
        List<TmdbGenre> genres,
        TmdbCredits credits
        ) {


    public record TmdbGenre(Integer id, String name) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TmdbCredits(
            List<TmdbCastMember> cast,
            List<TmdbCrewMember> crew
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TmdbCastMember(String name, String character) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record TmdbCrewMember(String name, String job) {}

}
