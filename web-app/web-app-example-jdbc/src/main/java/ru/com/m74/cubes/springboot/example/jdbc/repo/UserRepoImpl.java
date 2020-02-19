package ru.com.m74.cubes.springboot.example.jdbc.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;
import ru.com.m74.cubes.jdbc.EntityManager;
import ru.com.m74.cubes.jdbc.repository.AbstractRepoImpl;
import ru.com.m74.cubes.springboot.example.jdbc.domain.User;

import javax.annotation.Resource;
import java.util.Map;

@Repository
public class UserRepoImpl extends AbstractRepoImpl<User, Long> implements UserRepo {

//    @Resource
//    private PlatformTransactionManager transactionManager;

    @Autowired
    public UserRepoImpl(EntityManager em) {
        super(em, User.class);
    }

    @Override
    @Transactional
    public User save(Long id, Map<String, Object> changes) {
//        TransactionTemplate template = new TransactionTemplate(transactionManager);
//        return template.execute(transactionStatus -> {
        User user = super.save(id, changes);
        if (changes.containsKey("comments")) throw new RuntimeException("Test");
        return user;
//        });
    }
}
