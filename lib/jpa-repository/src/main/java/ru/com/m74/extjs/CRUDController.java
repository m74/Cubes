package ru.com.m74.extjs;

import org.springframework.web.bind.annotation.*;
import ru.com.m74.extjs.dto.Request;
import ru.com.m74.extjs.dto.Response;

import java.util.Map;

public class CRUDController<T> {

    private JpaRepository<T> repository;

    public CRUDController(JpaRepository<T> repository) {
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Response<Iterable<T>> getAll(Request pagination) {
        Response<Iterable<T>> response = new Response<>(repository.getAll(pagination));
        if (pagination.isPaging()) {
            response.setTotalCount(repository.count());
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    public Response<T> get(@PathVariable long id) {
        return new Response<>(repository.get(id));
    }

    @RequestMapping(method = RequestMethod.POST)
    public Response<T> create(@RequestBody T entity) {
        return new Response<>(repository.save(entity));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public Response<T> save(@RequestBody Map<String, Object> changes, @PathVariable long id) {
        return new Response<>(repository.save(id, changes));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void delete(@PathVariable long id) {
        repository.deleteById(id);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteAll(@RequestParam long ids[]) {
        repository.deleteByIds(ids);
    }

}
