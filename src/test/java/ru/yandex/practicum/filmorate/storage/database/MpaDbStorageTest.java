package ru.yandex.practicum.filmorate.storage.database;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MpaDbStorageTest {
    final JdbcTemplate jdbcTemplate;
    MpaDbStorage mpaStorage;

    @BeforeEach
    void setUp() {
        mpaStorage = new MpaDbStorage(jdbcTemplate);
    }

    @Test
    void getAll() {
        List<Mpa> mpas = mpaStorage.getAll();
        assertThat(mpas.size())
                .isNotNull()
                .isEqualTo(5);
    }

    @Test
    void getById() {
        Mpa mpa = mpaStorage.getById(1L).get();

        assertThat(mpa)
                .isNotNull()
                .isEqualTo(new Mpa(1, "G"));
    }

    @Test
    void create() {
        Exception exc = assertThrows(
                UnsupportedOperationException.class,
                () -> mpaStorage.create(new Mpa()));
        assertEquals("Функция пока не реализована", exc.getMessage());
    }

    @Test
    void update() {
        Exception exc = assertThrows(
                UnsupportedOperationException.class,
                () -> mpaStorage.update(new Mpa()));
        assertEquals("Функция пока не реализована", exc.getMessage());
    }

    @Test
    void delete() {
        Exception exc = assertThrows(
                UnsupportedOperationException.class,
                () -> mpaStorage.update(new Mpa()));
        assertEquals("Функция пока не реализована", exc.getMessage());
    }
}