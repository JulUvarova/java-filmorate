package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTest {
    final JdbcTemplate jdbcTemplate;
    FilmDbStorage filmStorage;
    Film film = new Film();
    Film film2 = new Film();

    @BeforeEach
    void setUp() {
        film.setName("normal name");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);
        film.setDescription("Normal description");
        film.setMpa(new Mpa(1, "G"));
        film.setRate(5);
        film.setGenres(new ArrayList<>());

        film2.setName("normal name");
        film2.setReleaseDate(LocalDate.now());
        film2.setDuration(120);
        film2.setDescription("Normal description");
        film2.setMpa(new Mpa(1, "G"));
        film2.setRate(10);
        film2.setGenres(new ArrayList<>());

        filmStorage = new FilmDbStorage(jdbcTemplate);
    }

    @Test
    void getAll() {
        filmStorage.create(film);
        filmStorage.create(film);

        List<Film> films = filmStorage.getAll();
        assertThat(films.size())
                .isNotNull()
                .isEqualTo(2);
    }

    @Test
    void createAndGetById() {
        filmStorage.create(film);

        Film savedFilm = filmStorage.getById(1L).get();

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    void update() {
        filmStorage.create(film);

        film2.setId(1);
        film2.setName("Update name");
        Film filmToUp = filmStorage.update(film2);

        assertThat(filmToUp)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmToUp);
    }

    @Test
    void delete() {
        filmStorage.create(film);

        filmStorage.delete(film);

        Boolean isDeleted = filmStorage.getById(1).isEmpty();

        assertThat(isDeleted).isTrue();
    }

    @Test
    void getFilmsByRate() {
        filmStorage.create(film);
        filmStorage.create(film2);

        film2.setRate(100);
        filmStorage.create(film2);

        List<Film> filmsByRate = filmStorage.getFilmsByRate(1);
        assertThat(filmsByRate.size())
                .isNotNull()
                .isEqualTo(1);

        assertThat(filmsByRate.get(0).getId())
                .isNotNull()
                .isEqualTo(3);
    }

    @Test
    void addAndDeleteLike() {
        String sql = "insert into users (username, login, email, birthday) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, "name", "login", "12@mail.ru", "2020-12-12");

        filmStorage.create(film);

        filmStorage.addLike(1, 1);
        Film likedFilm = filmStorage.getById(1).get();

        assertThat(likedFilm.getRate())
                .isNotNull()
                .isEqualTo(6);

        filmStorage.deleteLike(1, 1);
        Film dislikedFilm = filmStorage.getById(1).get();

        assertThat(dislikedFilm.getRate())
                .isNotNull()
                .isEqualTo(5);
    }
}