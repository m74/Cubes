package ru.com.m74.cubes.security.service;

//import org.springframework.security.core.userdetails.User;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.com.m74.cubes.security.domain.Role;
import ru.com.m74.cubes.security.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * @author mixam
 * @since 05.08.16 1:35
 */
@Service("security")
public class UserDetailsServiceImpl implements UserDetailsService {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        TypedQuery<User> q = em.createQuery("from User where login=:username", User.class);
        q.setParameter("username", username);
        User user = q.getSingleResult();

        return new org.springframework.security.core.userdetails.User(
                user.getLogin(),
                user.getPassword(),
                user.isActive(),
                true,
                true,
                true,
                AuthorityUtils.createAuthorityList(user.getRoles().stream().map(Role::getId).toArray(String[]::new))
        );
    }

    @Override
    public boolean hasRoles(String... roles) {
//        if (!SecurityUtils.hasRoles(roles)) {
//            throw new UserException("У Вас отсутствуют права на доступ к этому ресурсу. Необходимы следующие разрешения: " +
//                    String.join(",", roles));
//        }

        return true;
    }
}
