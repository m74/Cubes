package ru.com.m74.cubes.security.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.com.m74.cubes.security.dao.UsersRepository;
import ru.com.m74.cubes.security.domain.User;
import ru.com.m74.extjs.CRUDController;

/**
 * @author mixam
 * @since 18.05.16 19:18
 */
@RestController
@RequestMapping(path = "/User")
public class UsersController extends CRUDController<User> {

    @Autowired
    public UsersController(UsersRepository repository) {
        super(repository);
    }

}
