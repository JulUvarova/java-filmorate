package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.service.BaseService;

import java.util.List;

public abstract class BaseController<M extends BaseModel> {
    BaseService<M> service;

    public BaseController(BaseService<M> service) {
        this.service = service;
    }

    public List<M> readAll() {
        return service.readAll();
    }

    public BaseModel create(M model) {
        return service.create(model);
    }

    public BaseModel update(M model) {
        return service.update(model);
    }

    public BaseModel getById(long id) {
        return service.getById(id);
    }
}
