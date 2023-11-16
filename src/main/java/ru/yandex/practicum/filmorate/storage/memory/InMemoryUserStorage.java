package ru.yandex.practicum.filmorate.storage.memory;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Component
public class InMemoryUserStorage extends InMemoryBaseStorage<User> implements UserStorage {
    @Override
    public List<Long> getAllFriend(long userId) {
        throw new UnsupportedOperationException("Функция пока не реализована");
    }

    @Override
    public void addFriend(long user, long friend) {
        throw new UnsupportedOperationException("Функция пока не реализована");
    }

    @Override
    public void deleteFriend(long user, long friend) {
        throw new UnsupportedOperationException("Функция пока не реализована");
    }
}
