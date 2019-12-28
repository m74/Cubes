package ru.com.m74.cubes.jdbc.repository;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.com.m74.extjs.dto.Request;
import ru.com.m74.extjs.dto.Response;

import java.util.Map;

public class RController<T, I> {

    private final ReadOnlyRepo<T, I> repository;

    public RController(ReadOnlyRepo<T, I> repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Response<Iterable<T>> getAll(@RequestParam Map<String, Object> params, Request request) {
        Response<Iterable<T>> response = new Response<>(repository.getAll(request, params));
        if (request.isPaging()) {
            response.setTotalCount(repository.count(request, params));
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public Response<T> get(@PathVariable I id) {
        return new Response<>(repository.get(id));
    }

}
