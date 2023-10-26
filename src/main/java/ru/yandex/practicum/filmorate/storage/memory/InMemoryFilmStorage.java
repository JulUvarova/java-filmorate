package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage extends InMemoryBaseStorage<Film> implements FilmStorage {
    @Override
    public List<Film> getFilmsByRate() {
        List<Film> films = new ArrayList<>(storage.values());
        return films.stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .collect(Collectors.toList());
    }
}