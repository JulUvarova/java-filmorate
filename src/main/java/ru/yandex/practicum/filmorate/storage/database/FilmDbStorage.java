package ru.yandex.practicum.filmorate.storage.database;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() {
        String sql = "select f.*, m.name as mpa_name from films as f "
                + "join mpa as m on f.mpa_id = m.id";
        List<Film> films = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
        films.forEach(film -> getGenreByFilmId(film.getId()));
        return films;
    }

    @Override
    public Film create(Film film) {
        String sql = "insert into films (name, description, release_date, duration, mpa_id, rate) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            stmt.setLong(6, film.getRate());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().longValue());

        updateGenreByFilm(film);
        return film;
    }

    @Override
    public Film update(Film film) {
        String sql = "update films set name = ?, description = ?," +
                " release_date = ?, duration = ?, mpa_id = ?, rate = ? where id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getRate(), film.getId());
        updateGenreByFilm(film);
        return getById(film.getId()).get();
    }

    @Override
    public Film delete(Film film) {
        String sql = "delete from films where id = ?";
        jdbcTemplate.update(sql, film.getId());
        return film;
    }

    @Override
    public Optional<Film> getById(long findId) {
        String sql = "select f.*, m.name as mpa_name from films as f " +
                "join mpa as m on f.mpa_id = m.id " +
                "where f.id = ?";
        List<Film> film = jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs), findId);
        if (film.size() != 1) {
            return Optional.empty();
        }
        return Optional.of(film.get(0));
    }

    @Override
    public List<Film> getFilmsByRate(int limit) {
        String sql = "select id from films order by rate desc limit ?";
        List<Long> sortedFilms = jdbcTemplate.queryForList(sql, Long.class, limit);
        List<Film> films = sortedFilms.stream()
                .map(x -> getById(x).get())
                .collect(Collectors.toList());
        return films;
    }

    @Override
    public void addLike(long film, long user) {
        String sqlIns = "insert into likes (film_id, user_id) values (?, ?)";
        jdbcTemplate.update(sqlIns, film, user);

        String sqlUpd = "update films set rate = rate + 1 where id = ?";
        jdbcTemplate.update(sqlUpd, film);
    }

    @Override
    public void deleteLike(long film, long user) {
        String sqlToDel = "delete from likes where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlToDel, film, user);

        String sqlUpd = "update films set rate = rate - 1 where id = ?";
        jdbcTemplate.update(sqlUpd, film);
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Mpa mpa = new Mpa(rs.getLong("mpa_id"), rs.getString("mpa_name"));
        long rate = rs.getLong("rate");

        return new Film(id, name, description, releaseDate,
                duration, getLikesByFilmId(id), mpa, getGenreByFilmId(id), rate);
    }

    private List<Genre> getGenreByFilmId(long id) {
        String sql = "select g.* from genres as g " +
                "right join film_genre as fg on fg.genre_id = g.id where fg.film_id = ?";
        List<Genre> genres = jdbcTemplate.query(sql, (rs, rowNum) -> makeGenre(rs), id);
        return genres;
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        return new Genre(id, name);
    }

    private void updateGenreByFilm(Film film) {
        String sqlToDel = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlToDel, film.getId());

        if (film.getGenres().size() != 0) {
            String sqlToUp = "insert into film_genre (film_id, genre_id) values (?, ?)";
            Set<Long> genres = film.getGenres().stream().map(x -> x.getId()).collect(Collectors.toSet());
            genres.forEach(x -> jdbcTemplate.update(sqlToUp, film.getId(), x));
        }
    }

    private List<Long> getLikesByFilmId(long id) {
        String sql = "select user_id from likes where film_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, id);
    }
}
