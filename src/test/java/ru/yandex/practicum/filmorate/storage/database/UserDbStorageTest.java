package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTest {
    final JdbcTemplate jdbcTemplate;
    UserDbStorage userStorage;
    User user = new User();

    @BeforeEach
    public void setUp() {
        user.setName("normal name");
        user.setBirthday(LocalDate.now());
        user.setLogin("normal_login");
        user.setEmail("Normal@email.ru");

        userStorage = new UserDbStorage(jdbcTemplate);
    }

    @Test
    void getAll() {
        userStorage.create(user);
        user.setEmail("second@email.ru");
        user.setLogin("second");
        userStorage.create(user);

        List<User> users = userStorage.getAll();
        assertThat(users.size())
                .isNotNull()
                .isEqualTo(2);
    }

    @Test
    void update() {
        userStorage.create(user);

        User userToUp = new User();
        userToUp.setName("update");
        userToUp.setBirthday(LocalDate.now());
        userToUp.setLogin("update_login");
        userToUp.setEmail("update@email.ru");
        User updatedUser = userStorage.update(userToUp);

        assertThat(updatedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(userToUp);
    }

    @Test
    void delete() {
        userStorage.create(user);

        userStorage.delete(user);

        Boolean isDeleted = userStorage.getById(1).isEmpty();

        assertThat(isDeleted).isTrue();
    }

    @Test
    void createAndGetById() {
        userStorage.create(user);

        User savedUser = userStorage.getById(1L).get();

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    void addAndDeleteFriendAndGetAllFriends() {
        userStorage.create(user);
        user.setEmail("second@email.ru");
        user.setLogin("second");
        userStorage.create(user);

        userStorage.addFriend(1L, 2L);

        List<User> allFriend = userStorage.getAllFriend(1L);
        assertThat(allFriend.size())
                .isNotNull()
                .isEqualTo(1);

        assertThat(allFriend.get(0).getId())
                .isNotNull()
                .isEqualTo(2);

        userStorage.deleteFriend(1, 2);
        List<User> friendAfterDelete = userStorage.getAllFriend(1L);
        assertThat(friendAfterDelete.size())
                .isEqualTo(0);
    }
}