package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void loadGenreToFilm(List<Film> films) {
        final Map<Long, Film> filmById = films.stream().collect(Collectors.toMap(Film::getId, identity()));

        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final String sqlQuery = "select g.*, fg.film_id from genres as g " +
                "right join film_genre as fg where fg.genre_id = g.id AND fg.film_id in (" + inSql + ")";

        jdbcTemplate.query(sqlQuery, (rs, rowNum) -> {
            final Film film = filmById.get(rs.getLong("film_id"));
            film.addGenre(makeGenre(rs));
            return null;
        }, films.stream().map(Film::getId).toArray());
    }

    @Override
    public void createGenreToFilm(Film film) {
        if (film.getGenres().isEmpty()) {
            return;
        }
        String sql = "insert into film_genre (film_id, genre_id) values (?, ?)";
        final ArrayList<Genre> genres = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, film.getId());
                        ps.setLong(2, genres.get(i).getId());
                    }

                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }

    @Override
    public void deleteGenreToFilm(Film film) {
        String sqlToDel = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlToDel, film.getId());
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
    public Genre create(Genre model) {
        throw new UnsupportedOperationException("Функция пока не реализована");
    }

    @Override
    public Genre update(Genre model) {
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
