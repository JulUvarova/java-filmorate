package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
public class UserService extends BaseService<User> {
    private UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        super(storage);
        this.storage = storage;
    }

    public void addFriend(long user, long friend) {
        getById(user);
        getById(friend);
        storage.addFriend(user, friend);
    }

    public void deleteFriend(long user, long friend) {
        getById(user);
        getById(friend);
        storage.deleteFriend(user, friend);
    }

    public List<User> getAllFriend(long id) {
        getById(id);
        return storage.getAllFriend(id);
    }

    public List<User> getCommonFriend(long user1, long user2) {
        return storage.getCommonFriend(user1, user2);
    }
}
