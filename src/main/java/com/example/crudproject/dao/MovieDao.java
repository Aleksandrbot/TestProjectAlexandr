package com.example.crudproject.dao;

import com.example.crudproject.model.Actor;
import com.example.crudproject.model.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class MovieDao {

    private final JdbcTemplate jdbcTemplate;
    private final ActorDao actorDao;

    @Autowired
    public MovieDao(JdbcTemplate jdbcTemplate, ActorDao actorDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.actorDao = actorDao;
    }

    //Фильтр по вхождению слова в название фильма
    public List<Movie> getMoviesByTitleContain(String word) {
        String sql = "SELECT * FROM movie WHERE title LIKE ? ORDER BY id DESC";
        List<Movie> movies = jdbcTemplate.query(sql, new Object[]{"%" + word + "%"}, movieRowMapper());
        for (Movie movie : movies) {
            movie.setActors(actorDao.getActorsByMovieId(movie.getId()));
        }
        return movies;
    }

    //Фильтр по жанру фильма
    public List<Movie> getMoviesByGenre(String genre) {
        String sql = "SELECT * FROM movie WHERE genre = ? ORDER BY id DESC";
        List<Movie> movies = jdbcTemplate.query(sql, new Object[]{genre}, movieRowMapper());
        for (Movie movie : movies) {
            movie.setActors(actorDao.getActorsByMovieId(movie.getId()));
        }
        return movies;
    }

    //Фильтр по фамилии актера, сыгравшего в фильме:
    public List<Movie> getMoviesByActorSurname(String surname) {
        String sql = "SELECT DISTINCT m.* " +
                "FROM movie m " +
                "JOIN actor a ON m.id = a.movie_id " +
                "WHERE a.surname = ? " +
                "ORDER BY id DESC";

        List<Movie> movies = jdbcTemplate.query(sql, new Object[]{surname}, movieRowMapper());

        for (Movie movie : movies) {
            List<Actor> actors = actorDao.getActorsByMovieId(movie.getId());
            movie.setActors(actors);
        }
        return movies;
    }

    //Метод который выводит все фильмы вместе с актёрами
    public List<Movie> getAllMovies() {
        String sql = "SELECT * FROM movie ORDER BY id DESC";
        List<Movie> movies = jdbcTemplate.query(sql, movieRowMapper());

        for (Movie movie : movies) {
            List<Actor> actors = jdbcTemplate.query(
                    "SELECT * FROM actor WHERE movie_id = ?",
                    new Object[]{movie.getId()},
                    actorDao.actorRowMapper());
            movie.setActors(actors);
        }
        return movies;
    }

    // Метод для получения фильма по id со всеми его актерами
    public Movie getMovieWithActors(Long movieId) {
        String sql = "SELECT * FROM movie WHERE id = ?";
        Movie movie = jdbcTemplate.queryForObject(sql, new Object[]{movieId}, movieRowMapper());
        List<Actor> actors = jdbcTemplate.query("SELECT * FROM actor WHERE movie_id = ?", new Object[]{movieId}, actorDao.actorRowMapper());
        movie.setActors(actors);
        return movie;
    }

    //Метод для добавления нового фильма
    public void addMovie(Movie movie) {
        String sql = "INSERT INTO movie (title, description, release, rating, genre) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, movie.getTitle(), movie.getDescription(), movie.getRelease(), movie.getRating(), movie.getGenre());
    }

    //Метод для редактирования фильма и его актёров
    //исправлена ошибка! до этого можно было редактировать фильм только при наличии актёров
    public void updateMovieWithActors(Movie movie) {
        String sqlMovieUpdate = "UPDATE movie SET title=?, description=?, release=?, rating=?, genre=? WHERE id=?";
        jdbcTemplate.update(sqlMovieUpdate, movie.getTitle(), movie.getDescription(),
                movie.getRelease(), movie.getRating(), movie.getGenre(), movie.getId());

        String sqlDeleteActors = "DELETE FROM actor WHERE movie_id = ?";
        if (movie.getActors() != null && !movie.getActors().isEmpty()) {
            jdbcTemplate.update(sqlDeleteActors, movie.getId());

            String sqlInsertActors = "INSERT INTO actor (name, surname, role, movie_id) VALUES (?, ?, ?, ?)";
            for (Actor actor : movie.getActors()) {
                jdbcTemplate.update(sqlInsertActors, actor.getName(), actor.getSurname(),
                        actor.getRole(), movie.getId());
            }
        }
    }

    //Метод для удаления фильма по id. Фильм удаляется вместе с актёрами
    public void deleteMovie(Long id) {
        // Удаление актёров, связанных с фильмом
        String sqlDeleteActors = "DELETE FROM actor WHERE movie_id = ?";
        jdbcTemplate.update(sqlDeleteActors, id);

        String sqlDeleteMovie = "DELETE FROM movie WHERE id = ?";
        jdbcTemplate.update(sqlDeleteMovie, id);
    }

    public RowMapper<Movie> movieRowMapper() {
        return (ResultSet rs, int rowNum) -> {
            Movie movie = new Movie();
            Actor actor = new Actor();
            movie.setId(rs.getLong("id"));
            movie.setTitle(rs.getString("title"));
            movie.setDescription(rs.getString("description"));
            movie.setRelease(rs.getInt("release"));
            movie.setRating(rs.getDouble("rating"));
            movie.setGenre(rs.getString("genre"));
            return movie;
        };
    }
}
