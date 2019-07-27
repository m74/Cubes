package ru.com.m74.cubes.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.com.m74.cubes.security.dao.RolesRepository;
import ru.com.m74.cubes.security.domain.Role;
import ru.com.m74.extjs.CrudController;

/**
 * @author mixam
 * @since 18.05.16 19:18
 */
@RestController
@RequestMapping(path = "/Role")
public class RolesController extends CrudController<Role> {

    @Autowired
    public RolesController(RolesRepository repository) {
        super(repository);
    }
}
