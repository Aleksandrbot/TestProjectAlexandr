package com.example.crudproject.service;

import com.example.crudproject.dao.ActorDao;
import com.example.crudproject.dao.MovieDao;
import com.example.crudproject.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieDao movieDao;
    private final ActorDao actorDao;

    @Autowired
    public MovieService(MovieDao movieDao, ActorDao actorDao) {
        this.movieDao = movieDao;
        this.actorDao = actorDao;
    }

    public List<Movie> getMoviesByTitleContain(String word) {
        return movieDao.getMoviesByTitleContain(word);
    }

    public List<Movie> getMoviesByGenre(String genre) {
        return movieDao.getMoviesByGenre(genre);
    }

    public List<Movie> getMoviesByActorSurname(String surname) {
        return movieDao.getMoviesByActorSurname(surname);
    }

    public List<Movie> getAllMovies() {
        List<Movie> movies = movieDao.getAllMovies();
        movies.forEach(movie -> movie.setActors(actorDao.getActorsByMovieId(movie.getId())));
        return movies;
    }

    public Movie getMovieWithActors(Long movieId) {
        return movieDao.getMovieWithActors(movieId);
    }

    public void addMovie(Movie movie) {
        movieDao.addMovie(movie);
    }

    public void updateMovieWithActors(Movie movie) {
        movieDao.updateMovieWithActors(movie);
    }

    public void deleteMovie(Long id) {
        movieDao.deleteMovie(id);
    }
}
