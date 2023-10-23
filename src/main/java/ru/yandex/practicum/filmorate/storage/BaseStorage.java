package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.BaseModel;

import java.util.List;

public interface BaseStorage<M extends BaseModel> {
    public List<M> getAll();

    public BaseModel create(M model);

    public BaseModel update(M model);

    public M delete(M model);

    public M getById(long id);
}
