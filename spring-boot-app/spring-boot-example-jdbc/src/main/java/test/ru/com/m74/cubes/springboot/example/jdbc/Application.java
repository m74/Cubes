package test.ru.com.m74.cubes.springboot.example.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.com.m74.cubes.jdbc.EntityManager;

@SpringBootApplication(scanBasePackages = {
        "ru.com.m74.cubes", "test.ru.com.m74.cubes.springboot.example.jdbc"})
public class Application {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public Application(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public EntityManager em() {
        return new EntityManager(jdbcTemplate);
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
