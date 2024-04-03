package com.example.crudproject.service;

import com.example.crudproject.dao.ActorDao;
import com.example.crudproject.dao.MovieDao;
import com.example.crudproject.model.Actor;
import com.example.crudproject.model.Movie;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

public class MovieServiceTest {

    @Mock
    private MovieDao movieDao;

    @Mock
    private ActorDao actorDao;

    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetMoviesByTitleContain() {
        String word = "Title";
        List<Movie> expectedMovies = new ArrayList<>();
        when(movieDao.getMoviesByTitleContain(word)).thenReturn(expectedMovies);

        List<Movie> result = movieService.getMoviesByTitleContain(word);

        assertEquals(expectedMovies, result);
        verify(movieDao).getMoviesByTitleContain(word);
    }

    @Test
    public void testGetMoviesByGenre() {
        String genre = "Genre";
        List<Movie> expectedMovies = new ArrayList<>();
        when(movieDao.getMoviesByGenre(genre)).thenReturn(expectedMovies);

        List<Movie> result = movieService.getMoviesByGenre(genre);

        assertEquals(expectedMovies, result);
        verify(movieDao).getMoviesByGenre(genre);
    }

    @Test
    public void testGetMoviesByActorSurname() {
        String surname = "Surname";
        List<Movie> expectedMovies = new ArrayList<>();
        when(movieDao.getMoviesByActorSurname(surname)).thenReturn(expectedMovies);

        List<Movie> result = movieService.getMoviesByActorSurname(surname);

        assertEquals(expectedMovies, result);
        verify(movieDao).getMoviesByActorSurname(surname);
    }

    @Test
    public void testGetAllMovies() {
        List<Movie> expectedMovies = new ArrayList<>();
        Movie movie = new Movie();
        movie.setId(1L);
        expectedMovies.add(movie);
        List<Actor> actors = new ArrayList<>();

        when(movieDao.getAllMovies()).thenReturn(expectedMovies);
        when(actorDao.getActorsByMovieId(any())).thenReturn(actors);

        List<Movie> result = movieService.getAllMovies();

        assertEquals(expectedMovies, result);
        verify(movieDao).getAllMovies();
        verify(actorDao).getActorsByMovieId(any());
    }

    @Test
    public void testGetMovieWithActors() {
        Long movieId = 1L;
        Movie expectedMovie = new Movie();
        expectedMovie.setId(movieId);

        when(movieDao.getMovieWithActors(movieId)).thenReturn(expectedMovie);

        Movie result = movieService.getMovieWithActors(movieId);

        assertEquals(expectedMovie, result);
        verify(movieDao).getMovieWithActors(movieId);
    }

    @Test
    public void testAddMovie() {
        Movie movie = new Movie();

        movieService.addMovie(movie);

        verify(movieDao).addMovie(movie);
    }

    @Test
    public void testUpdateMovieWithActors() {
        Movie movie = new Movie();

        movieService.updateMovieWithActors(movie);

        verify(movieDao).updateMovieWithActors(movie);
    }

    @Test
    public void testDeleteMovie() {
        Long id = 1L;

        movieService.deleteMovie(id);

        verify(movieDao).deleteMovie(id);
    }
}