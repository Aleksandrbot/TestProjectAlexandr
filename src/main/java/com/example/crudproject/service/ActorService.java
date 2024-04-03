package com.example.crudproject.service;

import com.example.crudproject.dao.ActorDao;
import com.example.crudproject.model.Actor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActorService {

    private final ActorDao actorDao;

    @Autowired
    public ActorService(ActorDao actorDao) {
        this.actorDao = actorDao;
    }

    // Метод для получения списка актеров по ID фильма
    public List<Actor> getActorsByMovieId(Long movieId) {
        return actorDao.getActorsByMovieId(movieId);
    }

    // Метод для добавления нового актера
    public void addActor(Actor actor) {
        actorDao.addActor(actor);
    }

    // Метод для удаления актера по ID
    public void deleteActor(Long id) {
        actorDao.deleteActor(id);
    }
}