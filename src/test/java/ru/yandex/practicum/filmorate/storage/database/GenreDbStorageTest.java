package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GenreDbStorageTest {
    final JdbcTemplate jdbcTemplate;
    GenreDbStorage genreStorage;

    @BeforeEach
    void setUp() {
        genreStorage = new GenreDbStorage(jdbcTemplate);
    }

    @Test
    void getAll() {
        List<Genre> genres = genreStorage.getAll();
        assertThat(genres.size())
                .isNotNull()
                .isEqualTo(6);
    }

    @Test
    void getById() {
        Genre genre = genreStorage.getById(1L).get();

        assertThat(genre)
                .isNotNull()
                .isEqualTo(new Genre(1, "Комедия"));
    }

    @Test
    void create() {
        Exception exc = assertThrows(
                UnsupportedOperationException.class,
                () -> genreStorage.create(new Genre()));
        assertEquals("Функция пока не реализована", exc.getMessage());
    }

    @Test
    void update() {
        Exception exc = assertThrows(
                UnsupportedOperationException.class,
                () -> genreStorage.update(new Genre()));
        assertEquals("Функция пока не реализована", exc.getMessage());
    }

    @Test
    void delete() {
        Exception exc = assertThrows(
                UnsupportedOperationException.class,
                () -> genreStorage.update(new Genre()));
        assertEquals("Функция пока не реализована", exc.getMessage());
    }
}