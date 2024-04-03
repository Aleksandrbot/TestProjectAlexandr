package com.example.crudproject.dao;

import com.example.crudproject.model.Actor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class ActorDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ActorDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Метод для получения актёров по id фильма(этот метод нужен для метода фильтрации)
    public List<Actor> getActorsByMovieId(Long movieId) {
        String sql = "SELECT * FROM actor WHERE movie_id = ?";
        return jdbcTemplate.query(sql, new Object[]{movieId}, actorRowMapper());
    }

    //Метод для добавления нового актера
    public void addActor(Actor actor) {
        String sql = "INSERT INTO actor (name, surname, role, movie_id) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, actor.getName(), actor.getSurname(), actor.getRole(), actor.getMovie_id());
    }
    //Метод для удаления актёров
    public void deleteActor(Long id) {
            String sql = "DELETE FROM actor WHERE id = ?";
            jdbcTemplate.update(sql, id);
    }

    public RowMapper<Actor> actorRowMapper() {
        return (ResultSet rs, int rowNum) -> {
            Actor actor = new Actor();
            actor.setId(rs.getLong("id"));
            actor.setName(rs.getString("name"));
            actor.setSurname(rs.getString("surname"));
            actor.setRole(rs.getString("role"));
            actor.setMovie_id(rs.getInt("movie_id"));
            return actor;
        };
    }
}