package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class GenreDbStorage implements BaseStorage<Genre> {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAll() {
        String sql = "select * from genres order by id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs));
    }

    @Override
    public Optional<Genre> getById(long findId) {
        String sql = "select * from genres where id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), findId);
        if (genres.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(genres.get(0));
    }

    @Override
    public BaseModel create(Genre model) {
        throw new UnsupportedOperationException("Функция пока не реализована");
    }

    @Override
    public BaseModel update(Genre model) {
        throw new UnsupportedOperationException("Функция пока не реализована");
    }

    @Override
    public Genre delete(Genre model) {
        throw new UnsupportedOperationException("Функция пока не реализована");
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }
}
