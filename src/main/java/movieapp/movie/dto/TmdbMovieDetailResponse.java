package movieapp.movie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TmdbMovieDetailResponse(
        List<TmdbMovieResponse> results
) {}
