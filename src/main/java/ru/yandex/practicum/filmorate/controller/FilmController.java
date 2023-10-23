package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid; // нельзя проверить через класс/метод https://habr.com/ru/companies/otus/articles/746414/
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends BaseController<Film> {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        super(service);
        this.service = service;
    }

    @GetMapping
    public List<Film> readAll() {
        log.info("Отправляем фильмы из хранилища");
        return super.readAll();
    }

    @PostMapping
    public BaseModel create(@Valid @RequestBody Film film) {
        log.info("Создается фильм: {}", film);
        return super.create(film);
    }

    @PutMapping
    public BaseModel update(@Valid @RequestBody Film film) {
        log.info("Фильм с id={} обновляется", film.getId());
        return super.update(film);
    }

    @GetMapping("/{id}")
    public BaseModel getById(@PathVariable long id) {
        log.info("Получаем фильм с id={}", id);
        return super.getById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь id={} ставит лайк фильму id={}", id, userId);
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь id={} удаляет лайк фильму id={}", id, userId);
        service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return service.getAllFilms().stream()
                .sorted(Comparator.comparing(Film::getRate).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
