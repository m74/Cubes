package ru.com.m74.cubes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = "ru.com.m74.cubes")
@EntityScan(basePackages = "ru.com.m74.cubes")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
