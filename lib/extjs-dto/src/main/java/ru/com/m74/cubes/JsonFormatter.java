package ru.com.m74.cubes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.format.Formatter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Locale;

/**
 * @author mixam
 * @since 02.02.18 14:04
 */
public class JsonFormatter<T> implements Formatter<T> {
    private ObjectMapper mapper;

    private Class<T> cls;

    public JsonFormatter(ObjectMapper mapper, Class<T> cls) {
        this.mapper = mapper;
        this.cls = cls;
    }

    @Override
    public T parse(String text, Locale locale) throws ParseException {
        try {
            return mapper.readValue(text, cls);
        } catch (IOException e) {
            throw new ParseException(text, 0);
        }
    }

    @Override
    public String print(T object, Locale locale) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
