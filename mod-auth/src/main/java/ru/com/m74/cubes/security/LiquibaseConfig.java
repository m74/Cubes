package ru.com.m74.cubes.security;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    private final DataSource dataSource;

    @Autowired
    public LiquibaseConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    @DependsOn("entityManagerFactory")
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
//        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.xml");
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.yaml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
