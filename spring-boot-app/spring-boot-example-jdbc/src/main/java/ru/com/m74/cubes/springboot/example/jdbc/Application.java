package ru.com.m74.cubes.springboot.example.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.com.m74.cubes.jdbc.EntityManager;
import ru.com.m74.cubes.sql.MySqlDialect;

@SpringBootApplication(scanBasePackages = {
        "ru.com.m74.cubes", "ru.com.m74.cubes.springboot.example.jdbc"})
public class Application {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public Application(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public EntityManager em() {
        return new EntityManager(jdbcTemplate, null, new MySqlDialect());
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
