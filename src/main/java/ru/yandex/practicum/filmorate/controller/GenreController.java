package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@Slf4j
public class GenreController {
    private final GenreService service;

    @Autowired
    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping
    public List<Genre> readAll() {
        List<Genre> genres = service.readAll();
        log.info("Отправляем список жанров из хранилища: {} наименований", genres.size());
        return genres;
    }

    @GetMapping("/{id}")
    public Genre getById(@PathVariable long id) {
        Genre genre = service.getById(id);
        log.info("Получаем жанр с id={}: ", id, genre.getName());
        return genre;
    }
}
