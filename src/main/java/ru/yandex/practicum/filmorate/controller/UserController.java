package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends BaseController<User> {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        super(service);
        this.service = service;
    }

    @GetMapping
    public List<User> readAll() {
        log.info("Отправляем пользователей из хранилища");
        return super.readAll();
    }

    @PostMapping
    public BaseModel create(@Valid @RequestBody User user) {
        validateUserName(user);
        log.info("Создаем пользователя: {}", user);
        return super.create(user);
    }

    @PutMapping
    public BaseModel update(@Valid @RequestBody User user) {
        validateUserName(user);
        log.info("Пользователь с id={} обновляется", user.getId());
        return super.update(user);
    }

    @GetMapping("/{id}")
    public BaseModel getById(@PathVariable String id) {
        log.info("Получаем пользователя с id={}", id);
        return super.getById(Long.parseLong(id));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователи с id {} и {} сохраняются в друзьях", id, friendId);
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        log.info("Пользователи с id {} и {} удаляются из друзей", id, friendId);
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable long id) {
        log.info("Отправляем друзей пользователя с id {}", id);
        return service.getAllFriend(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        log.info("Ищем пересечения в друзьях у пользователей с id {} и {}", id, otherId);
        return service.getCommonFriend(id, otherId);
    }

    private void validateUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
