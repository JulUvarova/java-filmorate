package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@Service
public class FilmService extends BaseService<Film> {
    private FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage storage) {
        super(storage);
        this.storage = storage;
    }

    public void addLike(long filmId, long userId) {
        Film film = getById(filmId);
        film.addLike(userId);
    }

    public void deleteLike(long filmId, long userId) {
        Film film = getById(filmId);
        film.deleteLike(userId);
    }

    public List<Film> getFilmsByRate() {
        return storage.getFilmsByRate();
    }
}
