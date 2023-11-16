package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.ValidateDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class Film extends BaseModel {
    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @NotNull(message = "Описание не может быть пустым")
    @Size(max = 200, message = "Описание не более 200 знаков")// мб null
    private String description;

    @ValidateDate
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    private int duration;

    private List<Long> likes = new ArrayList<>();

    private long rate;

    private Mpa mpa;

    private List<Genre> genres = new ArrayList<>();

    public Film(long id, String name, String description, LocalDate releaseDate, int duration,
                List<Long> likes, Mpa mpa, List<Genre> genres, long rate) {
        super(id);
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.likes = likes;
        this.mpa = mpa;
        this.genres = genres;
        this.rate = rate;
    }

    public void addLike(long user) {
        rate++;
    }

    public void deleteLike(long user) {
        rate--;
    }
}