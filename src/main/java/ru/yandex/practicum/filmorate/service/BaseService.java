package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.List;

public abstract class BaseService<M extends BaseModel> {
    BaseStorage<M> storage;

    public BaseService(BaseStorage<M> storage) {
        this.storage = storage;
    }

    public List<M> readAll() {
        return storage.getAll();
    }

    public BaseModel create(M model) {
        return storage.create(model);
    }

    public BaseModel update(M model) {
        getById(model.getId());
        return storage.update(model);
    }

    public M getById(long id) {
        M model = storage.getById(id).orElseThrow(
                () -> new ModelNotFoundException(String.format("Объект с id=%d не найден", id)));
        return model;
    }
}
