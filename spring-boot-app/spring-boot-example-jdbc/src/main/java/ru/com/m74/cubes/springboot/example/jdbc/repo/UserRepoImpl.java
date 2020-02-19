package ru.com.m74.cubes.springboot.example.jdbc.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.com.m74.cubes.jdbc.EntityManager;
import ru.com.m74.cubes.jdbc.repository.AbstractRepoImpl;
import ru.com.m74.cubes.springboot.example.jdbc.domain.User;

import java.util.Map;

@Repository
public class UserRepoImpl extends AbstractRepoImpl<User, Long> implements UserRepo {

    @Autowired
    public UserRepoImpl(EntityManager em) {
        super(em, User.class);
    }

    @Override
    public User save(Long id, Map<String, Object> changes) {
        User user = super.save(id, changes);
//        if (true) throw new RuntimeException("Test");
        return user;
    }
}
