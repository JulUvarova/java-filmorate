package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.exception.ProjectException;
import ru.yandex.practicum.filmorate.model.BaseModel;
import ru.yandex.practicum.filmorate.model.Film;

import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class BaseControllerTest<M extends BaseModel> {
    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper(); //преобразовывает объект в JSON-строку

    protected M getModelFromResult(MvcResult result, Class<M> type) throws Exception {
        M model = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8), type);
        return model;
    }

    protected List<M> getListFromResult(MvcResult result, Class<M> type) throws Exception {
        String list = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        List<M> filmList = objectMapper.readValue(
                list, objectMapper.getTypeFactory().constructCollectionType(List.class, type));
        return filmList;
    }

    protected ProjectException getExcFromResult(MvcResult result) throws Exception {
        ProjectException exc = objectMapper.readValue(result.getResponse().getContentAsString(StandardCharsets.UTF_8),
                ProjectException.class);
        return exc;
    }

    protected RequestBuilder getPutRequest(M model, String path) throws Exception {
        return MockMvcRequestBuilders
                .put(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(model))
                .accept(MediaType.APPLICATION_JSON);
    }

    protected RequestBuilder getPostRequest(M model, String path) throws Exception {
        return MockMvcRequestBuilders
                .post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(model))
                .accept(MediaType.APPLICATION_JSON);
    }

    protected RequestBuilder getGetRequest(String path) {
        return MockMvcRequestBuilders
                .get(path)
                .accept(MediaType.APPLICATION_JSON);
    }
}
