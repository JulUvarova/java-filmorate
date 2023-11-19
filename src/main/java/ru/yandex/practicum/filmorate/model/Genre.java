package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@EqualsAndHashCode(callSuper = true, of = {"id"})
@NoArgsConstructor
public class Genre extends BaseModel {
    @NotBlank(message = "Название не может быть пустым")
    private String name;

    public Genre(long id, String name) {
        super(id);
        this.name = name;
    }
}