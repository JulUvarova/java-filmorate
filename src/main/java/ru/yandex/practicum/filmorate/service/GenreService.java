package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.database.GenreDbStorage;

@Service
public class GenreService extends BaseService<Genre> {
    @Autowired
    public GenreService(GenreDbStorage storage) {
        super(storage);
    }
}
