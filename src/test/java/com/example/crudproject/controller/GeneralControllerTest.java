package com.example.crudproject.controller;

import com.example.crudproject.model.Actor;
import com.example.crudproject.model.Movie;
import com.example.crudproject.service.ActorService;
import com.example.crudproject.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class GeneralControllerTest {

    @Mock
    private MovieService movieService;

    @Mock
    private ActorService actorService;

    @InjectMocks
    private GeneralController generalController;

    @Mock
    private Model model;

    @Test
    public void testGetMoviesByTitleContain() {
        String word = "example";
        Model model = mock(Model.class);
        List<Movie> movies = new ArrayList<>();
        when(movieService.getMoviesByTitleContain(word)).thenReturn(movies);

        String result = generalController.getMoviesByTitleContain(word, model);

        verify(model).addAttribute("movies", movies);
        assertEquals("movies", result);
    }

    @Test
    public void testGetMoviesByGenre() {
        String genre = "genre";
        List<Movie> expectedMovies = Arrays.asList(
                new Movie(1L, "Title", "Description", 1984, 8.5, genre, null)
        );
        when(movieService.getMoviesByGenre(genre)).thenReturn(expectedMovies);

        String viewName = generalController.getMoviesByGenre(genre, model);

        verify(movieService).getMoviesByGenre(genre);
        verify(model).addAttribute("movies", expectedMovies);
        assertEquals("movies", viewName);
    }

    @Test
    void testGetMoviesByActorSurname() {
        // Arrange
        String surname = "surname";
        List<Movie> mockMovies = Arrays.asList(new Movie(), new Movie());
        when(movieService.getMoviesByActorSurname(surname)).thenReturn(mockMovies);

        String viewName = generalController.getMoviesByActorSurname(surname, model);

        verify(movieService).getMoviesByActorSurname(surname);
        verify(model).addAttribute("movies", mockMovies);
        assertEquals("movies", viewName);
    }

    @Test
    void testGetAllMovies() {
        List<Movie> mockMovies = Arrays.asList(new Movie(), new Movie());
        when(movieService.getAllMovies()).thenReturn(mockMovies);

        String viewName = generalController.getAllMovies(model);

        verify(movieService).getAllMovies();
        verify(model).addAttribute("movies", mockMovies);
        assertEquals("movies", viewName);
    }

    @Test
    void testAddActorForm() {
        List<Movie> movies = Arrays.asList(new Movie(), new Movie());
        when(movieService.getAllMovies()).thenReturn(movies);

        String viewName = generalController.addActor(model);

        assertEquals("addActor", viewName);

        verify(model).addAttribute(eq("actor"), any(Actor.class));
        verify(model).addAttribute(eq("movie"), any(Movie.class));
        verify(model).addAttribute("movies", movies);
    }

    @Test
    void testAddActor() {
        Actor actor = new Actor();
        actor.setId((long) 1);
        actor.setName("Name");
        actor.setSurname("Surname");
        actor.setRole("Role");
        actor.setMovie_id(2);

        String view = generalController.addActor(actor);

        verify(actorService).addActor(actor);

        assertEquals("redirect:/movies/show", view);
    }

    @Test
    public void testShowAddMovieForm() {
        String viewName = generalController.showAddMovieForm(model);

        assertEquals("addMovie", viewName);
        verify(model).addAttribute(eq("movie"), any(Movie.class));
    }

    @Test
    public void testAddMovie() {
        Movie movie = new Movie();
        movie.setId(1L); // Предположим, что ID фильма 1
        movie.setTitle("Test Movie");
        movie.setDescription("Description");
        movie.setRelease(1999);
        movie.setRating(10.3);
        movie.setGenre("Genre");

        String viewName = generalController.addMovie(movie);

        verify(movieService).addMovie(any(Movie.class));
        assertEquals("redirect:/movies/show", viewName);
    }

    @Test
    public void testShowUpdateMovieForm() {
        Long movieId = 1L;
        Movie mockMovie = new Movie();
        mockMovie.setId(1L);
        mockMovie.setTitle("Title");
        mockMovie.setDescription("Description");
        mockMovie.setRelease(1999);
        mockMovie.setRating(10.3);
        mockMovie.setGenre("Genre");

        when(movieService.getMovieWithActors(movieId)).thenReturn(mockMovie);

        Model model = mock(Model.class);

        String viewName = generalController.showUpdateMovieForm(movieId, model);

        assertEquals("updateMovie", viewName);
        verify(model).addAttribute("movie", mockMovie);
        verify(movieService).getMovieWithActors(movieId);
    }

    @Test
    public void testUpdateMovie() {
        Long movieId = 1L;
        Movie mockMovie = new Movie();
        mockMovie.setId(1L);
        mockMovie.setTitle("Test Movie");
        mockMovie.setDescription("Description");
        mockMovie.setRelease(1999);
        mockMovie.setRating(10.3);
        mockMovie.setGenre("Genre");

        String redirectViewPath = generalController.updateMovie(movieId, mockMovie);

        verify(movieService).updateMovieWithActors(mockMovie);
        assertEquals("redirect:/movies/show", redirectViewPath);

        assertEquals(movieId, mockMovie.getId());
    }

    @Test
    void deleteMovieTest() {
        Long movieId = 1L;
        doNothing().when(movieService).deleteMovie(movieId);

        String viewName = generalController.deleteMovie(movieId);

        assertEquals("redirect:/movies/show", viewName);

        verify(movieService, times(1)).deleteMovie(movieId);
    }

    @Test
    void deleteActorTest() {
        Long actorId = 1L;
        doNothing().when(actorService).deleteActor(actorId);

        String viewName = generalController.deleteActor(actorId);

        assertEquals("redirect:/movies/show", viewName);
        verify(actorService, times(1)).deleteActor(actorId);
    }

}