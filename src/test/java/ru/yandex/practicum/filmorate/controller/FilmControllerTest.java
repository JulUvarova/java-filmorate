package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.yandex.practicum.filmorate.exception.ExceptionControllerAdvice;
import ru.yandex.practicum.filmorate.exception.ProjectException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//разобщает тесты
@ActiveProfiles("test")//пометка, запуск тестов под профилем "тест"
class FilmControllerTest extends BaseControllerTest<Film> {
    @Autowired
    private MockMvc mockMvc;

    private Film film = new Film();

    public static final String PATH = "/films";

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new FilmController())
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();

        film.setName("normal name");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);
        film.setDescription("Normal description");
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
        ProjectException exc = getExcFromResult(result);
        assertEquals("Название не может быть пустым", exc.getMessage());
    }

    @Test
    void failNullNameFilmCreate() throws Exception {
        film.setName(null);

        MvcResult result = mockMvc.perform(getPostRequest(film, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ProjectException exc = getExcFromResult(result);
        assertEquals("Название не может быть пустым", exc.getMessage());
    }

    @Test
    void failDescriptionFilmCreate() throws Exception {
        film.setDescription("В этом случае мы сообщаем Spring, что единственный bean-компонент, " +
                "который следует использовать в этом тесте, — это класс BookController. " +
                "Это может не иметь большого значения в нашем небольшом примере приложения, " +
                "но по мере того, как наши приложения растут, и у вас есть сотни или тысячи");

        MvcResult result = mockMvc.perform(getPostRequest(film, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ProjectException exc = getExcFromResult(result);
        assertEquals("Описание не более 200 знаков", exc.getMessage());
    }

    @Test
    void failReleaseDateFilmCreate() throws Exception {
        film.setReleaseDate(LocalDate.of(1100, 1, 1));

        MvcResult result = mockMvc.perform(getPostRequest(film, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ProjectException exc = getExcFromResult(result);
        assertEquals("Фильм не может быть снят ранее 28.12.1895", exc.getMessage());
    }

    @Test
    void failDurationFilmCreate() throws Exception {
        film.setDuration(-1);

        MvcResult result = mockMvc.perform(getPostRequest(film, PATH))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andReturn();
        ProjectException exc = getExcFromResult(result);
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
        ProjectException exc = getExcFromResult(result);
        assertEquals("Данные с id=1000 не найдены", exc.getMessage());
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
}