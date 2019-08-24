package ru.com.m74.cubes.mvc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ru.com.m74.cubes.mvc.ExceptionController.class)
public class ExceptionHandlerConfiguration {
}
