package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilmService extends BaseService<Film> {
    @Autowired
    public FilmService(FilmStorage storage) {
        super(storage);
    }

    public void addLike(long filmId, long userId) {
        Film film = findModel(filmId);
        if (findModel(userId) != null && film != null) {
            film.addLike(userId);
        }
    }

    public void deleteLike(long filmId, long userId) {
        Film film = findModel(filmId);
        if (findModel(userId) != null && film != null) {
            film.deleteLike(userId);
        }
    }

    public List<Film> getAllFilms() {
        return new ArrayList<>(storage.getAll());
    }
}
