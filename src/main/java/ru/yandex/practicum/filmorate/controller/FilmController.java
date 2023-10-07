package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid; // нельзя проверить через класс/метод https://habr.com/ru/companies/otus/articles/746414/
import java.util.ArrayList;

@RestController
@RequestMapping("/films")
@Slf4j

public class FilmController extends BaseController<Film> {
    @GetMapping
    public ArrayList<Film> readAll() {
        log.info("Отправили {} фильмов из хранилища", storage.size());
        return super.readAll();
    }

    @PostMapping
    public BaseModel create(@Valid @RequestBody Film film) {
        log.info("Создан фильм: {}", film);
        return super.create(film);
    }

    @PutMapping
    public BaseModel update(@Valid @RequestBody Film film) {
        log.info("Фильм с id={} обновлен", film.getId());
        return super.update(film);
    }
}
