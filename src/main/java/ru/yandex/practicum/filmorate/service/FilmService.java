package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService extends BaseService<Film> {
    private final FilmStorage storage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage storage, UserService userService) {
        super(storage);
        this.storage = storage;
        this.userService = userService;
    }

    public void addLike(long filmId, long userId) {
        getById(filmId);
        userService.getById(userId);
        storage.addLike(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        getById(filmId);
        userService.getById(userId);
        storage.deleteLike(filmId, userId);
    }

    public List<Film> getFilmsByRate(int limit) {
        return storage.getFilmsByRate(limit);
    }
}
