package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectRequestParam;
import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public List<Film> readAll() {
        log.info("Отправляем фильмы из хранилища");
        return service.readAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Создается фильм: {}", film);
        return service.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Фильм с id={} обновляется", film.getId());
        return service.update(film);
    }

    @GetMapping("/{id}")
    public Film getById(@PathVariable long id) {
        log.info("Получаем фильм с id={}", id);
        return service.getById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        if (userId < 1) {
            throw new ModelNotFoundException(String.format("Пользователя с id=%d не существует", userId));
        }
        log.info("Пользователь id={} ставит лайк фильму id={}", id, userId);
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        if (userId < 1) {
            throw new ModelNotFoundException(String.format("Пользователя с id=%d не существует", userId));
        }
        log.info("Пользователь id={} удаляет лайк фильму id={}", id, userId);
        service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        if (count <= 0) {
            throw new IncorrectRequestParam(String.format("Список не может содержать %d объектов", count));
        }
        log.info("Создается список из {} лучших фильмов", count);
        return service.getFilmsByRate(count);
    }
}
