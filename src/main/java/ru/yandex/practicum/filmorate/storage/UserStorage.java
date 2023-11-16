package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage extends BaseStorage<User> {
    public List<Long> getAllFriend(long userId);

    public void addFriend(long user, long friend);

    public void deleteFriend(long user, long friend);
}
