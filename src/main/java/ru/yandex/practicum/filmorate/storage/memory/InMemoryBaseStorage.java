package ru.yandex.practicum.filmorate.storage.memory;

import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.storage.BaseStorage;

import java.util.*;

public abstract class InMemoryBaseStorage<M extends BaseModel> implements BaseStorage<M> {
    protected final Map<Long, M> storage = new HashMap<>();
    protected long count;

    @Override
    public List<M> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public BaseModel create(M model) {
        model.setId(++count);
        storage.put(model.getId(), model);
        return model;
    }

    @Override
    public BaseModel update(M model) { // проверка на null в сервисе
        storage.put(model.getId(), model);
        return model;
    }

    @Override
    public M delete(M model) {
        return storage.remove(model.getId());
    }

    @Override
    public Optional<M> getById(long id) {
        return Optional.ofNullable(storage.get(id));
    }
}
