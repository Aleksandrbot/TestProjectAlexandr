package com.example.crudproject.dao;

import com.example.crudproject.model.Actor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ActorDaoTest {
    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ActorDao actorDao;
    private Actor testActor;

    @BeforeEach
    void setUp() {
        testActor = new Actor();
        testActor.setId(1L);
        testActor.setName("Name");
        testActor.setSurname("Surname");
        testActor.setRole("Role");
        testActor.setMovie_id(101);
    }

    @Test
    void getActorsByMovieIdTest() {
        Long movieId = 101L;

        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Arrays.asList(testActor));

        List<Actor> result = actorDao.getActorsByMovieId(movieId);

        assertEquals(1, result.size());
        assertEquals(testActor.getName(), result.get(0).getName());

        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM actor WHERE movie_id = ?"),
                any(Object[].class),
                any(RowMapper.class));
    }

    @Test
    void addActorTest() {
        actorDao.addActor(testActor);

        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO actor (name, surname, role, movie_id) VALUES (?, ?, ?, ?)"),
                eq(testActor.getName()), eq(testActor.getSurname()), eq(testActor.getRole()), eq(testActor.getMovie_id()));
    }

    @Test
    void deleteActorTest() {
        Long actorId = 1L;

        actorDao.deleteActor(actorId);

        verify(jdbcTemplate, times(1)).update(
                eq("DELETE FROM actor WHERE id = ?"),
                eq(actorId));
    }

    @Test
    void actorRowMapperTest() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("Name");
        when(resultSet.getString("surname")).thenReturn("Surname");
        when(resultSet.getString("role")).thenReturn("Role");
        when(resultSet.getInt("movie_id")).thenReturn(101);

        RowMapper<Actor> rowMapper = actorDao.actorRowMapper();
        Actor mappedActor = rowMapper.mapRow(resultSet, 0);

        assertEquals("Name", mappedActor.getName());
        assertEquals("Surname", mappedActor.getSurname());
        assertEquals("Role", mappedActor.getRole());
        assertEquals(101, mappedActor.getMovie_id());
    }
}