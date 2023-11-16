package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
public class MpaDbStorage implements BaseStorage<Mpa> {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAll() {
        String sql = "select * from mpa order by id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs));
    }

    @Override
    public Optional<Mpa> getById(long findId) {
        String sql = "select * from mpa where id = ?";
        List<Mpa> mpa = jdbcTemplate.query(sql, (rs, rowNum) -> makeMpa(rs), findId);
        if (mpa.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(mpa.get(0));
    }

    @Override
    public BaseModel create(Mpa model) {
        throw new UnsupportedOperationException("Функция пока не реализована");
    }

    @Override
    public BaseModel update(Mpa model) {
        throw new UnsupportedOperationException("Функция пока не реализована");
    }

    @Override
    public Mpa delete(Mpa model) {
        throw new UnsupportedOperationException("Функция пока не реализована");
    }

    private Mpa makeMpa(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Mpa(id, name);
    }
}
