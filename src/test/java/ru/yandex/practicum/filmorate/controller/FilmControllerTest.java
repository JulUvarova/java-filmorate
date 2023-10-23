package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import ru.yandex.practicum.filmorate.exception.ErrorResponse;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//разобщает тесты
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)//https://www.baeldung.com/spring-dirtiescontext
@ActiveProfiles("test")//пометка, запуск тестов под профилем "тест"
class FilmControllerTest extends BaseControllerTest<Film> {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmController controller;

    @Autowired
    private UserStorage userStorage;

    private User user = new User();

    private Film film = new Film();

    public static final String PATH = "/films";

    @BeforeEach
    public void setup() {
        film.setName("normal name");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);
        film.setDescription("Normal description");

        user.setName("normal name");
        user.setBirthday(LocalDate.now());
        user.setLogin("normal_login");
        user.setEmail("Normal@email.ru");
    }

    @Test
    public void normalFilmCreate() throws Exception {
        MvcResult result = mockMvc.perform(getPostRequest(film, PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Film actualFilm = getModelFromResult(result, Film.class);
        film.setId(1);

        assertEquals(film, actualFilm);
    }

    @Test
    void failBlankNameFilmCreate() throws Exception {
        film.setName(" ");

        MvcResult result = mockMvc.perform(getPostRequest(film, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Название не может быть пустым", exc.getMessage());
    }

    @Test
    void failNullNameFilmCreate() throws Exception {
        film.setName(null);

        MvcResult result = mockMvc.perform(getPostRequest(film, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Название не может быть пустым", exc.getMessage());
    }

    @Test
    void failLargeDescriptionFilmCreate() throws Exception {
        film.setDescription("В этом случае мы сообщаем Spring, что единственный bean-компонент, " +
                "который следует использовать в этом тесте, — это класс BookController. " +
                "Это может не иметь большого значения в нашем небольшом примере приложения, " +
                "но по мере того, как наши приложения растут, и у вас есть сотни или тысячи");

        MvcResult result = mockMvc.perform(getPostRequest(film, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Описание не более 200 знаков", exc.getMessage());
    }

    @Test
    void failNullDesrcriptionFilmCreate() throws Exception {
        film.setDescription(null);

        MvcResult result = mockMvc.perform(getPostRequest(film, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Описание не может быть пустым", exc.getMessage());
    }

    @Test
    void failReleaseDateFilmCreate() throws Exception {
        film.setReleaseDate(LocalDate.of(1100, 1, 1));

        MvcResult result = mockMvc.perform(getPostRequest(film, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Фильм не может быть снят ранее 28.12.1895", exc.getMessage());
    }

    @Test
    void failNullDateFilmCreate() throws Exception {
        film.setReleaseDate(null);

        MvcResult result = mockMvc.perform(getPostRequest(film, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Фильм не может быть снят ранее 28.12.1895", exc.getMessage());
    }

    @Test
    void failDurationFilmCreate() throws Exception {
        film.setDuration(-1);

        MvcResult result = mockMvc.perform(getPostRequest(film, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Продолжительность фильма не может быть отрицательной", exc.getMessage());
    }

    @Test
    void normalFilmUpdate() throws Exception {
        mockMvc.perform(getPostRequest(film, PATH));

        Film updateFilm = new Film();
        updateFilm.setId(1);
        updateFilm.setName("up");
        updateFilm.setReleaseDate(LocalDate.now());
        updateFilm.setDescription("up");
        updateFilm.setDuration(10);

        MvcResult result2 = mockMvc.perform(getPutRequest(updateFilm, PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Film actualFilm = getModelFromResult(result2, Film.class);
        film.setId(1);

        assertEquals(updateFilm, actualFilm);
    }

    @Test
    void unknownFilmUpdate() throws Exception {
        film.setId(1000);

        MvcResult result = mockMvc.perform(getPutRequest(film, PATH))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();
        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Объект с id=1000 не найден", exc.getMessage());
    }

    @Test
    void getAllFilms() throws Exception {
        mockMvc.perform(getPostRequest(film, PATH));

        MvcResult result = mockMvc.perform(getGetRequest(PATH))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Film> films = getListFromResult(result, Film.class);
        assertEquals(1, films.size());
    }

    @Test
    void getFilmById() throws Exception {
        mockMvc.perform(getPostRequest(film, PATH));

        MvcResult result = mockMvc.perform(getGetRequest(PATH + "/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Film actualFilm = getModelFromResult(result, Film.class);
        film.setId(1);

        assertEquals(film, actualFilm);
    }

    @Test
    void getUserByFailId() throws Exception {
        mockMvc.perform(getPostRequest(film, PATH));

        MvcResult result = mockMvc.perform(getGetRequest(PATH + "/1000"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Объект с id=1000 не найден", exc.getMessage());
    }

    @Test
    void addLikeToFilm() throws Exception {
        userStorage.create(user);
        mockMvc.perform(getPostRequest(film, PATH));

        mockMvc.perform(getPutRequest(null, PATH + "/1/like/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Film actualFilm = (Film) controller.getById(1);
        assertEquals(1, actualFilm.getRate());
    }

    @Test
    void addLikeToWrongFilm() throws Exception {
        MvcResult result = mockMvc.perform(getPutRequest(null, PATH + "/100/like/100"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Объект с id=100 не найден", exc.getMessage());
    }

    @Test
    void deleteLikeToFilm() throws Exception {
        userStorage.create(user);
        mockMvc.perform(getPostRequest(film, PATH));

        mockMvc.perform(getPutRequest(null, PATH + "/1/like/1"));

        mockMvc.perform(getDeleteRequest(PATH + "/1/like/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Film actualFilm = (Film) controller.getById(1);
        assertEquals(0, actualFilm.getRate());
    }

    @Test
    void deleteLikeToWrongFilm() throws Exception {
        MvcResult result = mockMvc.perform(getDeleteRequest(PATH + "/100/like/100"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn();

        ErrorResponse exc = getExcFromResult(result);
        assertEquals("Объект с id=100 не найден", exc.getMessage());
    }

    @Test
    void getMostPopularFilms() throws Exception {
        userStorage.create(user);
        mockMvc.perform(getPostRequest(film, PATH));
        mockMvc.perform(getPostRequest(film, PATH));

        mockMvc.perform(getPutRequest(null, PATH + "/1/like/1"));

        MvcResult result = mockMvc.perform(getGetRequest(PATH + "/popular?count=1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<Film> films = getListFromResult(result, Film.class);
        assertEquals(1, films.get(0).getId());
    }
}