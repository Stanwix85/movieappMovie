package movieapp.movie.entities;

import java.time.LocalDate;
import java.util.Collection;
import java.util.UUID;

public class Movie {

        private UUID id;
        private String movieId;
        private String title;
        private String director;
        private String ageRating;
        private Integer runTime;
        private Double rating;
        private Collection<String> genre;
        private LocalDate releaseDate;
        private String tagline;
        private String synopsis;
        private Collection<String> actors;
        private String poster;

        public Movie(){

        }

        public Movie(UUID id, String movieId, String title, String director, String ageRating, Integer runTime, Double rating, Collection<String> genre, LocalDate releaseDate, String tagline, String synopsis, Collection<String> actors, String poster) {
            this.id = id;
            this.movieId = movieId;
            this.title = title;
            this.director = director;
            this.ageRating = ageRating;
            this.runTime = runTime;
            this.rating = rating;
            this.genre = genre;
            this.releaseDate = releaseDate;
            this.tagline = tagline;
            this.synopsis = synopsis;
            this.actors = actors;
            this.poster = poster;
        }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }

    public Integer getRunTime() {
        return runTime;
    }

    public void setRunTime(Integer runTime) {
        this.runTime = runTime;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Collection<String> getGenre() {
        return genre;
    }

    public void setGenre(Collection<String> genre) {
        this.genre = genre;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Collection<String> getActors() {
        return actors;
    }

    public void setActors(Collection<String> actors) {
        this.actors = actors;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
