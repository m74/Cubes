package ru.com.m74.cubes.jdbc.repository;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.com.m74.extjs.dto.Response;

import java.util.Map;

public class CRUDController<T, I> extends RController<T, I> {

    private final AbstractRepo<T, I> repository;

    public CRUDController(AbstractRepo<T, I> repository) {
        super(repository);
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Response<T> create(@RequestBody T entity) {
        return new Response<>(repository.persist(entity));
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/{id}")
    public Response<T> save(@RequestBody Map<String, Object> changes, @PathVariable I id) {
        repository.convert(changes);
        return new Response<>(repository.save(id, changes));
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    public void delete(@PathVariable I id) {
        repository.deleteById(id);
    }

//    @RequestMapping(method = RequestMethod.DELETE)
//    public void deleteAll(@RequestParam long ids[]) {
//        repository.deleteByIds(ids);
//    }
}
