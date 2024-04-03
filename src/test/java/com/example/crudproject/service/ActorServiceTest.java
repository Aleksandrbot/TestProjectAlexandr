package com.example.crudproject.service;

import com.example.crudproject.dao.ActorDao;
import com.example.crudproject.model.Actor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ActorServiceTest {

    @Mock
    private ActorDao actorDao;

    @InjectMocks
    private ActorService actorService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getActorsByMovieIdTest() {
        Long movieId = 1L;
        Actor actor1 = new Actor(1L, "Name1", "Surname1", "Role1", 1);
        Actor actor2 = new Actor(2L, "Name2", "Surname2", "Role2", 1);
        List<Actor> expectedActors = Arrays.asList(actor1, actor2);

        when(actorDao.getActorsByMovieId(movieId)).thenReturn(expectedActors);

        List<Actor> actualActors = actorService.getActorsByMovieId(movieId);

        verify(actorDao).getActorsByMovieId(movieId);
        assertIterableEquals(expectedActors, actualActors);
    }

    @Test
    void addActorTest() {
        Actor newActor = new Actor(null, "NewName", "NewSurname", "NewRole", 2);

        actorService.addActor(newActor);

        verify(actorDao).addActor(newActor);
    }

    @Test
    void deleteActorTest() {
        Long actorId = 3L;

        actorService.deleteActor(actorId);

        verify(actorDao).deleteActor(actorId);
    }
}