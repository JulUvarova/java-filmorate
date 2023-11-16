package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public abstract class BaseModel {
    private long id;

    public BaseModel(long id) {
        this.id = id;
    }
}
