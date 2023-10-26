package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.validator.ValidateDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @JsonIgnore
    private Set<Long> likes = new HashSet<>();

    public void addLike(long user) {
        likes.add(user);
    }

    public void deleteLike(long user) {
        likes.remove(user);
    }

    public long getRate() {
        return likes.size();
    }
}
