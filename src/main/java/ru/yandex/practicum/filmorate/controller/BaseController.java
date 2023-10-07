package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ModelNotFoundException;
import ru.yandex.practicum.filmorate.model.BaseModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseController<M extends BaseModel> {
    protected final Map<Integer, M> storage = new HashMap<>();
    private int idCount = 0;

    public ArrayList<M> readAll() {
        return new ArrayList<>(storage.values());
    }

    public BaseModel create(M model) {
        model.setId(++idCount);
        storage.put(idCount, model);
        return model;
    }

    public BaseModel update(M model) {
        if (!storage.containsKey(model.getId())) {
            throw new ModelNotFoundException(String.format("Данные с id=%d не найдены", model.getId()));
        }
        storage.put(model.getId(), model);
        return model;
    }
}
