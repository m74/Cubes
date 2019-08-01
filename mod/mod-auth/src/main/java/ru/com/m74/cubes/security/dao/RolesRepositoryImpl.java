package ru.com.m74.cubes.security.dao;

import org.springframework.stereotype.Repository;
import ru.com.m74.cubes.security.domain.Role;
import ru.com.m74.extjs.JpaRepositoryImpl;

@Repository
public class RolesRepositoryImpl extends JpaRepositoryImpl<Role> implements RolesRepository {

    public RolesRepositoryImpl() {
        super(Role.class);
    }
}
