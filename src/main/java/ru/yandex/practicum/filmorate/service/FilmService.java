package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class FilmService extends BaseService<Film> {
    private final FilmStorage storage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(FilmStorage storage, UserStorage userStorage, GenreStorage genreStorage) {
        super(storage);
        this.storage = storage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
    }

    public void addLike(long filmId, long userId) {
        getById(filmId);
        userCheck(userId);
        storage.addLike(filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        getById(filmId);
        userCheck(userId);
        storage.deleteLike(filmId, userId);
    }

    public List<Film> getFilmsByRate(int limit) {
        List<Film> films = storage.getFilmsByRate(limit);
        genreStorage.loadGenreToFilm(films);
        return films;
    }

    @Override
    public List<Film> readAll() {
        List<Film> films = super.readAll();
        genreStorage.loadGenreToFilm(films);
        return films;
    }

    @Override
    public Film create(Film model) {
        Film film = super.create(model);
        genreStorage.createGenreToFilm(film);
        genreStorage.loadGenreToFilm(List.of(film));
        return film;
    }

    @Override
    public Film update(Film film) {
        Film updFilm = super.update(film);
        genreStorage.deleteGenreToFilm(film);
        genreStorage.createGenreToFilm(film);
        genreStorage.loadGenreToFilm(List.of(updFilm));
        return updFilm;
    }

    @Override
    public Film getById(long id) {
        Film film = super.getById(id);
        genreStorage.loadGenreToFilm(List.of(film));
        return film;
    }


    private void userCheck(long id) {
        userStorage.getById(id).orElseThrow(
                () -> new ModelNotFoundException(String.format("Пользователь с id=%d не найден", id)));
    }
}
