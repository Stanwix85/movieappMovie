package movieapp.movie.client.implement;

import movieapp.movie.client.TmdbClient;
import movieapp.movie.dto.TmdbMovieResponse;
import movieapp.movie.dto.TmdbSearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Optional;

@Component
public class TmdbClientImpl implements TmdbClient {
    private static final Logger log = LoggerFactory.getLogger(TmdbClientImpl.class);
    private final RestClient restClient;

    public TmdbClientImpl(
            @Value("${tmdb.api.url:https://api.themoviedb.org/3}") String baseUrl,
            @Value("${tmdb.api.token:}") String readAccessToken) {

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + readAccessToken)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Optional<TmdbMovieResponse> fetchMovieById(Long tmdbId) {
        try {
            TmdbMovieResponse response = restClient.get()
                    .uri("/movie/{id}", tmdbId)
                    .retrieve()
                    .body(TmdbMovieResponse.class);
            return Optional.ofNullable(response);
        } catch (Exception ex) {
            log.error("Failed to fetch movie with ID {} from TMDB: {}", tmdbId, ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<TmdbMovieResponse> fetchMoviesWithCredit(Long tmdbId) {
        try {
            TmdbMovieResponse response = restClient.get()
                    .uri("/movie/{id}?append_to_response=credits", tmdbId)
                    .retrieve()
                    .body(TmdbMovieResponse.class);

            return Optional.ofNullable(response);
        } catch (Exception ex) {
            log.error("Failed to fetch movie with ID {} from TMDB: {}", tmdbId, ex.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<TmdbSearchResponse> searchMovies(String query) {
        try {
            TmdbSearchResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                                    .path("/search/movie")
                                    .queryParam("query", query)
                                    .build())
                    .retrieve()
                    .body(TmdbSearchResponse.class);
            return Optional.ofNullable(response);
        } catch (Exception ex) {
            log.error("Failed to search movies with query '{}' from TMDB: {}", query, ex.getMessage());
            return Optional.empty();
        }
    }
}
