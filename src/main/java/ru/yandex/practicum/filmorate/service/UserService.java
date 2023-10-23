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
        User checkUser = findModel(user);
        User checkFriend = findModel(friend);
        checkUser.addFriend(friend);
        checkFriend.addFriend(user);
    }

    public void deleteFriend(long user, long friend) {
        User checkUser = findModel(user);
        User checkFriend = findModel(friend);
        checkUser.deleteFriend(friend);
        checkFriend.deleteFriend(user);
    }

    public List<User> getAllFriend(long id) {
        List<Long> friendship =  new ArrayList<>(findModel(id).getFriends());
        return friendship.stream().map(storage::getById).collect(Collectors.toList());
    }

    public List<User> getCommonFriend(long user1, long user2) {
        List<Long> user1Friends = new ArrayList<>(findModel(user1).getFriends());
        List<Long> user2Friends = new ArrayList<>(findModel(user2).getFriends());
        user1Friends.retainAll(user2Friends);
        return user1Friends.stream().map(storage::getById).collect(Collectors.toList());
    }
}
