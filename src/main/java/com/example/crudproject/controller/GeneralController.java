package com.example.crudproject.controller;

import com.example.crudproject.model.Actor;
import com.example.crudproject.model.Movie;
import com.example.crudproject.service.ActorService;
import com.example.crudproject.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movies")
public class GeneralController {
    private final MovieService movieService;
    private final ActorService actorService;

    @Autowired
    public GeneralController(MovieService movieService, ActorService actorService) {
        this.movieService = movieService;
        this.actorService = actorService;

    }

    //Метод фильтрации по вхождению слова в название фильма
    @GetMapping("/searchByTitle")
    public String getMoviesByTitleContain(@RequestParam String word, Model model) {
        model.addAttribute("movies", movieService.getMoviesByTitleContain(word));
        return "movies";
    }

    //Метод фильтрации по жанру
    @GetMapping("/searchByGenre")
    public String getMoviesByGenre(@RequestParam String genre, Model model) {
        model.addAttribute("movies", movieService.getMoviesByGenre(genre));
        return "movies";
    }

    //Метод фильтрации по фамилии актёра
    @GetMapping("/searchByActorSurname")
    public String getMoviesByActorSurname(@RequestParam String surname, Model model) {
        model.addAttribute("movies", movieService.getMoviesByActorSurname(surname));
        return "movies";
    }

    //Метод выводит все фильмы вместе с актёрами
    @GetMapping("/show")
    public String getAllMovies(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies";
    }

    //Метод-форма для добавления нового актёра в БД
    @GetMapping("/saveActor")
    public String addActor(Model model) {
        model.addAttribute("actor", new Actor());
        model.addAttribute("movie", new Movie());
        model.addAttribute("movies", movieService.getAllMovies());
        return "addActor";
    }

    //Метод для добавления нового актёра в БД
    @PostMapping("/saveActor")
    public String addActor(@ModelAttribute Actor actor) {
        actorService.addActor(actor);
        return "redirect:/movies/show";
    }

    //Метод-форма для добавления нового фильма в БД
    @GetMapping("/saveMovie")
    public String showAddMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "addMovie";
    }

    //Метод для добавления нового фильма в БД
    @PostMapping("/saveMovie")
    public String addMovie(@ModelAttribute Movie movie) {
        movieService.addMovie(movie);
        return "redirect:/movies/show";
    }

    //Метод-форма для редактирования фильмов с актёрами
    @GetMapping("/updateMovieWithActors/{id}")
    public String showUpdateMovieForm(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieWithActors(id); // или другой подходящий метод для получения фильма
        model.addAttribute("movie", movie);
        return "updateMovie";
    }

    //Метод для редактирования фильмов с актёрами
    @PostMapping("/updateMovieWithActors/{id}")
    public String updateMovie(@PathVariable Long id, @ModelAttribute Movie movie) {
        movie.setId(id);
        movieService.updateMovieWithActors(movie);

        return "redirect:/movies/show";
    }

    //Метод для удаления фильма по id
    @GetMapping("/deleteMovie/{id}")
    public String deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return "redirect:/movies/show";
    }

    //Метод для удаления актёра по id
    @GetMapping("/deleteActor/{id}")
    public String deleteActor(@PathVariable Long id) {
        actorService.deleteActor(id);
        return "redirect:/movies/show";
    }
}
