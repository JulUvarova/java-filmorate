package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService extends BaseService<User> {
    @Autowired
    public UserService(UserStorage storage) {
        super(storage);
    }

    public void addFriend(long user, long friend) {
        User checkUser = getById(user);
        User checkFriend = getById(friend);
        checkUser.addFriend(friend);
        checkFriend.addFriend(user);
    }

    public void deleteFriend(long user, long friend) {
        User checkUser = getById(user);
        User checkFriend = getById(friend);
        checkUser.deleteFriend(friend);
        checkFriend.deleteFriend(user);
    }

    public List<User> getAllFriend(long id) {
        List<Long> friendship =  new ArrayList<>(getById(id).getFriends());
        return friendship.stream().map(x -> storage.getById(x).get()).collect(Collectors.toList());
    }

    public List<User> getCommonFriend(long user1, long user2) {
        List<Long> user1Friends = new ArrayList<>(getById(user1).getFriends());
        List<Long> user2Friends = new ArrayList<>(getById(user2).getFriends());
        user1Friends.retainAll(user2Friends);
        return user1Friends.stream().map(x -> storage.getById(x).get()).collect(Collectors.toList());
    }
}
