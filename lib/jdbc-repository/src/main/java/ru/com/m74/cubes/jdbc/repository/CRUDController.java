package ru.com.m74.cubes.jdbc.repository;

import org.springframework.web.bind.annotation.*;
import ru.com.m74.extjs.dto.Request;
import ru.com.m74.extjs.dto.Response;

import java.io.IOException;
import java.util.Map;

public class CRUDController<T> {

    private final AbstractRepo<T> repository;

    public CRUDController(AbstractRepo<T> repository) {
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
    public Response<T> get(@PathVariable Object id) {
        return new Response<>(repository.get(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Response<T> create(@RequestBody T entity) {
        return new Response<>(repository.persist(entity));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public Response<T> save(@RequestBody Map<String, Object> changes, @PathVariable Object id) {
        return new Response<>(repository.save(id, changes));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void delete(@PathVariable Object id) {
        repository.deleteById(id);
    }

//    @RequestMapping(method = RequestMethod.DELETE)
//    public void deleteAll(@RequestParam long ids[]) {
//        repository.deleteByIds(ids);
//    }
}
