package ru.com.m74.cubes.security.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import ru.com.m74.cubes.security.domain.User;
import ru.com.m74.extjs.JpaRepositoryImpl;

import java.util.Map;

@Repository
public class UsersRepositoryImpl extends JpaRepositoryImpl<User> implements UsersRepository {

    private final BCryptPasswordEncoder encoder;

    @Autowired
    public UsersRepositoryImpl(BCryptPasswordEncoder encoder) {
        super(User.class);
        this.encoder = encoder;
    }

    @Override
    public User save(Object id, Map<String, Object> changes) {
        String password = (String) changes.get("password");
        if (password != null) {
            changes.replace("password", encoder.encode(password));
        }
        return super.save(id, changes);
    }
}
