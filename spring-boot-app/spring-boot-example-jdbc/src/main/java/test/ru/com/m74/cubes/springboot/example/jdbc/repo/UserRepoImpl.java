package test.ru.com.m74.cubes.springboot.example.jdbc.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.com.m74.cubes.jdbc.repository.AbstractRepoImpl;
import ru.com.m74.cubes.jdbc.EntityManager;
import test.ru.com.m74.cubes.springboot.example.jdbc.domain.User;

@Repository
public class UserRepoImpl extends AbstractRepoImpl<User> implements UserRepo {

    @Autowired
    public UserRepoImpl(EntityManager em) {
        super(em, User.class);
    }
}
