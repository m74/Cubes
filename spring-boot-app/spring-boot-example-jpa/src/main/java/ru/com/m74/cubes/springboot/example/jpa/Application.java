package ru.com.m74.cubes.springboot.example.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "ru.com.m74.cubes.security")
@EntityScan(basePackages = "ru.com.m74.cubes")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
