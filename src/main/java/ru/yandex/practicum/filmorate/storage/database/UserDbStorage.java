package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Primary
@Component
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    @Override
    public User create(User user) {
        String sql = "insert into users (username, login, email, birthday) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sql = "update users set username = ?, login = ?, email = ?, birthday = ? where id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public User delete(User user) {
        String sql = "delete from users where id = ?";
        jdbcTemplate.update(sql, user.getId());
        return user;
    }

    @Override
    public Optional<User> getById(long findId) {
        String sql = "select * from users where id = ?";
        List<User> user = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), findId);
        if (user.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(user.get(0));
    }

    @Override
    public List<User> getAllFriend(long id) {
        String sql = "select * from users where id in (" +
                "select friend_id from friendship where user_id = ?)";
        List<User> friends = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), id);
        return friends;
    }

    @Override
    public void addFriend(long user, long friend) {
        String sql = "insert into friendship (user_id, friend_id) values (?, ?)";
        jdbcTemplate.update(sql, user, friend);
    }

    @Override
    public void deleteFriend(long user, long friend) {
        String sql = "delete from friendship where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, user, friend);
    }

    @Override
    public List<User> getCommonFriend(long user1, long user2) {
        String sql = "select * from users where id in (" +
                "select friend_id from friendship where user_id = ? and friend_id in (" +
                "select friend_id from friendship where user_id = ?))";
        List<User> friends = jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs), user1, user2);
        return friends;
    }

    private User makeUser(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("username");
        String login = rs.getString("login");
        String email = rs.getString("email");
        LocalDate birthday = rs.getDate("birthday").toLocalDate();

        return new User(id, name, login, email, birthday);
    }
}
