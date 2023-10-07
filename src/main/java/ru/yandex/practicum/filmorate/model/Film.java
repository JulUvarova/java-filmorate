package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.ValidatorAnnotation.ValidateDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class Film extends BaseModel {
    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @Size(max=200, message = "Описание не более 200 знаков")
    private String description;

    @ValidateDate
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма не может быть отрицательной")
    private int duration;
}
