package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

@Service
public class MpaService extends BaseService<Mpa> {
    @Autowired
    public MpaService(MpaStorage storage) {
        super(storage);
    }
}
