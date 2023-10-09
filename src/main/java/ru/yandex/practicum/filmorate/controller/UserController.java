package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends BaseController<User> {
    @GetMapping
    public List<User> readAll() {
        log.info("Отправили {} пользователей из хранилища", storage.size());
        return super.readAll();
    }

    @PostMapping
    public BaseModel create(@Valid @RequestBody User user) {
        validateUserName(user);
        log.info("Создан пользователь: {}", user);
        return super.create(user);
    }

    @PutMapping
    public BaseModel update(@Valid @RequestBody User user) {
        validateUserName(user);
        log.info("Пользователь с id={} обновлен", user.getId());
        return super.update(user);
    }

    private void validateUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
