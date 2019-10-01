package ru.com.m74.cubes;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.com.m74.extjs.dto.Filter;
import ru.com.m74.extjs.dto.Sorter;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    @Autowired
    public MvcConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatterForFieldType(Filter[].class, new JsonFormatter<>(objectMapper, Filter[].class));
        registry.addFormatterForFieldType(Sorter[].class, new JsonFormatter<>(objectMapper, Sorter[].class));
    }
}
