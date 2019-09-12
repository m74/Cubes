package test.ru.com.m74.cubes.springboot.example.jdbc.repo;

import ru.com.m74.cubes.jdbc.repository.AbstractRepo;
import test.ru.com.m74.cubes.springboot.example.jdbc.domain.User;

public interface UserRepo extends AbstractRepo<User, Long> {
}
