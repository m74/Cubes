package ru.com.m74.cubes.springboot.example.jdbc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.com.m74.cubes.jdbc.repository.CRUDController;
import ru.com.m74.cubes.springboot.example.jdbc.repo.UserRepo;
import ru.com.m74.cubes.springboot.example.jdbc.domain.User;

@RestController
@RequestMapping("Test.model.User")
public class UserController extends CRUDController<User, Long> {

    @Autowired
    public UserController(UserRepo repo) {
        super(repo);
    }
}
