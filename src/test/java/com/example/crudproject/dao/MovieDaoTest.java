package com.example.crudproject.dao;

import com.example.crudproject.model.Actor;
import com.example.crudproject.model.Movie;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class MovieDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ActorDao actorDao;

    @InjectMocks
    private MovieDao movieDao;

    @Test
    public void testGetMoviesByTitleContain() {
        String word = "Test";
        Movie testMovie = new Movie(1L, "Title", "Description", 2023, 8.0, "Genre", null);
        Actor testActor = new Actor(1L, "Name", "Surname", "Role", 1);

        when(jdbcTemplate.query(
                eq("SELECT * FROM movie WHERE title LIKE ? ORDER BY id DESC"),
                any(Object[].class),
                any(RowMapper.class)
        )).thenReturn(Collections.singletonList(testMovie));

        when(actorDao.getActorsByMovieId(testMovie.getId())).thenReturn(Collections.singletonList(testActor));

        List<Movie> result = movieDao.getMoviesByTitleContain(word);

        assertTrue(!result.isEmpty() );
        assertEquals(1, result.size());
        Movie resultMovie = result.get(0);
        assertEquals("Title", resultMovie.getTitle());
        assertTrue(resultMovie.getActors().contains(testActor));

        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM movie WHERE title LIKE ? ORDER BY id DESC"),
                any(Object[].class),
                any(RowMapper.class)
        );
        verify(actorDao, times(1)).getActorsByMovieId(testMovie.getId());
    }

    @Test
    public void testGetMoviesByGenre() {
        String genre = "Comedy";
        Movie testMovie = new Movie(1L, "Title", "description", 2021, 9.0, genre, null);
        Actor testActor = new Actor(1L, "Name", "Surname", "Role", 1);

        when(jdbcTemplate.query(
                eq("SELECT * FROM movie WHERE genre = ? ORDER BY id DESC"),
                any(Object[].class),
                any(RowMapper.class)
        )).thenReturn(Collections.singletonList(testMovie));

        when(actorDao.getActorsByMovieId(testMovie.getId())).thenReturn(Collections.singletonList(testActor));

        List<Movie> result = movieDao.getMoviesByGenre(genre);

        assertTrue(!result.isEmpty());
        assertEquals(1, result.size());
        Movie resultMovie = result.get(0);
        assertEquals(genre, resultMovie.getGenre());
        assertTrue(resultMovie.getActors().contains(testActor));

        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM movie WHERE genre = ? ORDER BY id DESC"),
                any(Object[].class),
                any(RowMapper.class)
        );
        verify(actorDao, times(1)).getActorsByMovieId(testMovie.getId());
    }

    @Test
    public void testGetMoviesByActorSurname() {
        String surname = "Surname";
        Movie testMovie = new Movie(1L, "Test Movie", "Description here", 2020, 8.5, "Drama", null);
        Actor testActor1 = new Actor(1L, "Name", "Surname", "Role1", 1);
        Actor testActor2 = new Actor(2L, "Name", "Surname", "Role2", 2);

        when(jdbcTemplate.query(
                eq("SELECT DISTINCT m.* FROM movie m JOIN actor a ON m.id = a.movie_id WHERE a.surname = ? ORDER BY id DESC"),
                any(Object[].class),
                any(RowMapper.class)
        )).thenReturn(Arrays.asList(testMovie));

        when(actorDao.getActorsByMovieId(anyLong())).thenReturn(Arrays.asList(testActor1, testActor2));

        List<Movie> movies = movieDao.getMoviesByActorSurname(surname);

        assertFalse(movies.isEmpty());
        assertEquals(1, movies.size());
        assertEquals(testMovie, movies.get(0));
        assertEquals(2, movies.get(0).getActors().size());
        assertTrue(movies.get(0).getActors().contains(testActor1));
        assertTrue(movies.get(0).getActors().contains(testActor2));

        verify(jdbcTemplate, times(1)).query(
                eq("SELECT DISTINCT m.* FROM movie m JOIN actor a ON m.id = a.movie_id WHERE a.surname = ? ORDER BY id DESC"),
                any(Object[].class),
                any(RowMapper.class)
        );
        verify(actorDao, times(1)).getActorsByMovieId(testMovie.getId());
    }

    @Test
    public void testGetAllMovies() {
        // Arrange
        Movie movie1 = new Movie(1L, "Movie 1", "Description 1", 2020, 9.0, "Comedy", Arrays.asList());
        List<Movie> movies = Arrays.asList(movie1);

        Actor actor1 = new Actor(1L, "Actor", "One", "Role", 1);
        List<Actor> actors = Arrays.asList(actor1);

        when(jdbcTemplate.query("SELECT * FROM movie ORDER BY id DESC", movieDao.movieRowMapper())).thenReturn(movies);
        when(jdbcTemplate.query(
                "SELECT * FROM actor WHERE movie_id = ?",
                new Object[]{movie1.getId()},
                actorDao.actorRowMapper())).thenReturn(actors);

        List<Movie> result = movieDao.getAllMovies();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotNull(result.get(0).getActors());
        assertEquals(1, result.get(0).getActors().size());
        assertEquals("Actor One", result.get(0).getActors().get(0).getName() + " " + result.get(0).getActors().get(0).getSurname());
    }

    @Test
    void testGetMovieWithActorsById() {
        Long movieId = 1L;
        Movie movie = new Movie(movieId, "Movie 1", "Description 1", 2020, 9.0, "Comedy", new ArrayList<>());
        Actor actor1 = new Actor(1L, "Actor", "One", "Role", 1);
        List<Actor> actors = Arrays.asList(actor1);

        // Имитация получения фильма по ID
        when(jdbcTemplate.queryForObject(
                "SELECT * FROM movie WHERE id = ?",
                new Object[]{movieId},
                movieDao.movieRowMapper())).thenReturn(movie);

        // Имитация получения актеров данного фильма
        when(jdbcTemplate.query(
                "SELECT * FROM actor WHERE movie_id = ?",
                new Object[]{movieId},
                actorDao.actorRowMapper())).thenReturn(actors);

        Movie resultMovie = movieDao.getMovieWithActors(movieId);
        assertNotNull(resultMovie);
        assertEquals(movieId, resultMovie.getId());
        assertEquals("Movie 1", resultMovie.getTitle());
        assertNotNull(resultMovie.getActors());
        assertEquals(1, resultMovie.getActors().size());
        Actor resultActor = resultMovie.getActors().get(0);
        assertEquals("Actor One", resultActor.getName() + " " + resultActor.getSurname());
    }

    @Test
    public void testUpdateMovieWithActors() {
        Movie movie = new Movie(1L, "Movie Title", "Description", 2022, 8.5, "Action", Arrays.asList(
                new Actor(1L, "Actor1", "Surname1", "Role1", 1),
                new Actor(2L, "Actor2", "Surname2", "Role2", 1)
        ));

        doAnswer(invocation -> null).when(jdbcTemplate).update(anyString(), (Object) any());

        movieDao.updateMovieWithActors(movie);

        verify(jdbcTemplate, times(1)).update(anyString(), eq(movie.getTitle()), eq(movie.getDescription()),
                eq(movie.getRelease()), eq(movie.getRating()), eq(movie.getGenre()), eq(movie.getId()));
        verify(jdbcTemplate, times(1)).update(anyString(), eq(movie.getId()));
        verify(jdbcTemplate, times(movie.getActors().size())).update(anyString(),
                any(), any(), any(), eq(movie.getId()));
    }

    @Test
    public void testAddMovie() {
        Movie movie = new Movie();
        movie.setTitle("Title");
        movie.setDescription("Description");
        movie.setRelease(2010);
        movie.setRating(8.8);
        movie.setGenre("Genre");

        movieDao.addMovie(movie);

        verify(jdbcTemplate).update(
                anyString(),
                eq(movie.getTitle()),
                eq(movie.getDescription()),
                eq(movie.getRelease()),
                eq(movie.getRating()),
                eq(movie.getGenre())
        );
    }

    @Test
    public void testDeleteMovie() {
        Long movieId = 1L;

        movieDao.deleteMovie(movieId);

        verify(jdbcTemplate, times(1)).update(
                "DELETE FROM actor WHERE movie_id = ?",
                movieId
        );

        verify(jdbcTemplate, times(1)).update(
                "DELETE FROM movie WHERE id = ?",
                movieId
        );
    }
}