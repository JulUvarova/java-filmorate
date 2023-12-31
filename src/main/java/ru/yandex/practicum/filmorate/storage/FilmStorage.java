package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends BaseStorage<Film> {
    public List<Film> getFilmsByRate(int limit);

    public void addLike(long film, long user);

    public void deleteLike(long film, long user);
}


