package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class UserControllerTest extends BaseControllerTest<User> {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController controller;

    private User user = new User();

    public static final String PATH = "/users";

    @BeforeEach
    public void setup() {
        user.setName("normal name");
        user.setBirthday(LocalDate.now());
        user.setLogin("normal_login");
        user.setEmail("Normal@email.ru");
    }

    @Test
    public void normalUserCreate() throws Exception {
        MvcResult result = mockMvc.perform(getPostRequest(user, PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        User actualUser = getModelFromResult(result, User.class);
        user.setId(1);

        assertEquals(user, actualUser);
    }

    @Test
    void failWhitespaceLoginUserCreate() throws Exception {
        user.setLogin("a a");

        MvcResult result = mockMvc.perform(getPostRequest(user, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Логин не может быть пустым и содержать пробелы", exc.getMessage());
    }

    @Test
    void failBlankLoginUserCreate() throws Exception {
        user.setLogin("  ");

        MvcResult result = mockMvc.perform(getPostRequest(user, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Логин не может быть пустым и содержать пробелы", exc.getMessage());
    }

    @Test
    void failNullLoginUserCreate() throws Exception {
        user.setLogin(null);

        MvcResult result = mockMvc.perform(getPostRequest(user, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Логин не может быть пустым и содержать пробелы", exc.getMessage());
    }

    @Test
    void nullNameUserCreate() throws Exception {
        user.setName(null);

        MvcResult result = mockMvc.perform(getPostRequest(user, PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        User actualUser = getModelFromResult(result, User.class);
        user.setId(1);

        assertEquals(user.getLogin(), actualUser.getName());
    }

    @Test
    void failBirthdayInFutureUserCreate() throws Exception {
        user.setBirthday(LocalDate.of(3000, 1, 1));

        MvcResult result = mockMvc.perform(getPostRequest(user, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Дата рождения не может быть в будущем", exc.getMessage());
    }

    @Test
    void failNullBirthdayUserCreate() throws Exception {
        user.setBirthday(null);

        MvcResult result = mockMvc.perform(getPostRequest(user, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Дата рождения не может быть пустой", exc.getMessage());
    }

    @Test
    void failEmailUserCreate() throws Exception {
        user.setEmail("это-неправильный?эмейл@");

        MvcResult result = mockMvc.perform(getPostRequest(user, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Почта должна быть оформлена по правилам", exc.getMessage());
    }

    @Test
    void failBlankEmailUserCreate() throws Exception {
        user.setEmail("");

        MvcResult result = mockMvc.perform(getPostRequest(user, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Почта не может быть пустой", exc.getMessage());
    }

    @Test
    void failNullEmailUserCreate() throws Exception {
        user.setEmail(null);

        MvcResult result = mockMvc.perform(getPostRequest(user, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Почта не может быть пустой", exc.getMessage());
    }

    @Test
    void normalUserUpdate() throws Exception {
        mockMvc.perform(getPostRequest(user, PATH));

        User updateUser = new User();
        updateUser.setId(1);
        updateUser.setName("up");
        updateUser.setBirthday(LocalDate.now());
        updateUser.setLogin("up");
        updateUser.setEmail("jjjj@mail.ru");

        MvcResult result2 = mockMvc.perform(getPutRequest(updateUser, PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        User actualUser = getModelFromResult(result2, User.class);
        user.setId(1);

        assertEquals(updateUser, actualUser);
    }

    @Test
    void unknownUserUpdate() throws Exception {
        user.setId(1000);

        MvcResult result = mockMvc.perform(getPutRequest(user, PATH))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Объект с id=1000 не найден", exc.getMessage());
    }

    @Test
    void getAllUsers() throws Exception {
        mockMvc.perform(getPostRequest(user, PATH));

        MvcResult result = mockMvc.perform(getGetRequest(PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<User> users = getListFromResult(result, User.class);
        assertEquals(1, users.size());
    }

    @Test
    void getUserById() throws Exception {
        mockMvc.perform(getPostRequest(user, PATH));

        MvcResult result = mockMvc.perform(getGetRequest(PATH + "/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        User actualUser = getModelFromResult(result, User.class);
        user.setId(1);

        assertEquals(user, actualUser);
    }

    @Test
    void getUserByFailId() throws Exception {
        mockMvc.perform(getPostRequest(user, PATH));

        MvcResult result = mockMvc.perform(getGetRequest(PATH + "/1000"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Объект с id=1000 не найден", exc.getMessage());
    }

    @Test
    void addAndGetFriends() throws Exception {
        mockMvc.perform(getPostRequest(user, PATH));
        user.setEmail("second@email.ru");
        user.setLogin("second");
        mockMvc.perform(getPostRequest(user, PATH));

        mockMvc.perform(getPutRequest(null, PATH + "/1/friends/2"));

        MvcResult result = mockMvc.perform(getGetRequest(PATH + "/1/friends"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<User> friends = getListFromResult(result, User.class);
        assertEquals(1, friends.size());
        assertEquals(2, friends.get(0).getId());
    }

    @Test
    void deleteAndGetEmptyFriends() throws Exception {
        mockMvc.perform(getPostRequest(user, PATH));
        user.setEmail("second@email.ru");
        user.setLogin("second");
        mockMvc.perform(getPostRequest(user, PATH));

        mockMvc.perform(getPutRequest(null, PATH + "/1/friends/2"));

        mockMvc.perform(getDeleteRequest(PATH + "/1/friends/2"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        MvcResult result = mockMvc.perform(getGetRequest(PATH + "/1/friends"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        List<User> friends = getListFromResult(result, User.class);

        assertEquals(0, friends.size());
    }

    @Test
    void getCommonFriends() throws Exception {
        mockMvc.perform(getPostRequest(user, PATH));
        user.setEmail("second@email.ru");
        user.setLogin("second");
        mockMvc.perform(getPostRequest(user, PATH));
        user.setEmail("third@email.ru");
        user.setLogin("third");
        mockMvc.perform(getPostRequest(user, PATH));

        mockMvc.perform(getPutRequest(null, PATH + "/1/friends/2"));
        mockMvc.perform(getPutRequest(null, PATH + "/3/friends/2"));

        MvcResult result = mockMvc.perform(getGetRequest(PATH + "/1/friends/common/3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<User> friends = getListFromResult(result, User.class);

        assertEquals(1, friends.size());
        assertEquals(2, friends.get(0).getId());
    }

    @Test
    void getEmptyCommonFriends() throws Exception {
        mockMvc.perform(getPostRequest(user, PATH));
        user.setEmail("second@email.ru");
        user.setLogin("second");
        mockMvc.perform(getPostRequest(user, PATH));
        user.setEmail("third@email.ru");
        user.setLogin("third");
        mockMvc.perform(getPostRequest(user, PATH));

        mockMvc.perform(getPutRequest(null, PATH + "/1/friends/3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        MvcResult result = mockMvc.perform(getGetRequest(PATH + "/1/friends/common/3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<User> friends = getListFromResult(result, User.class);

        assertEquals(0, friends.size());
    }
}