package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Mpa extends BaseModel {
    private String name;

    public Mpa(long id, String name) {
        super(id);
        this.name = name;
    }
}