package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService extends BaseService<User> {
    UserStorage storage;

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
        return storage.getAllFriend(id).stream()
                .map(x -> getById(x))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriend(long user1, long user2) {
        getById(user1);
        getById(user2);
        List<Long> user1Friends = storage.getAllFriend(user1);
        List<Long> user2Friends = storage.getAllFriend(user2);
        user1Friends.retainAll(user2Friends);
        return user1Friends.stream().map(x -> getById(x)).collect(Collectors.toList());
    }
}
