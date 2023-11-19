package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage extends BaseStorage<Genre> {
    public void loadGenreToFilm(List<Film> films);

    public void createGenreToFilm(Film film);

    public void deleteGenreToFilm(Film film);
}


