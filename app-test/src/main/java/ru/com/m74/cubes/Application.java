package ru.com.m74.cubes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan({"ru.com.m74.cubes"})
@PropertySource("classpath:application.properties")
public class Application implements WebMvcConfigurer {

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {


//        registry.addConverter(new Converter<String, Date>() {
//            @Override
//            public Date convert(String s) {
//                try {
//                    return timestampFormat().parse(s);
//                } catch (ParseException e) {
//                    return null;
//                }
//            }
//        });
        registry.addFormatter(new Formatter<Date>() {
            @Override
            public Date parse(String s, Locale locale) throws ParseException {
                return timestampFormat().parse(s);
            }

            @Override
            public String print(Date date, Locale locale) {
                return timestampFormat().format(date);
            }
        });
//        super.addFormatters(registry);
    }

    @Bean
    public SimpleDateFormat timestampFormat() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setDateFormat(timestampFormat());
        return mapper;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        resolver.setMaxUploadSize((long) 10 * 1024 * 1024); // max 10 Mb
        return resolver;
    }


    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> converter : converters) {
            if (converter.getClass().isAssignableFrom(MappingJackson2HttpMessageConverter.class)) {
                MappingJackson2HttpMessageConverter jackson2HttpMessageConverter =
                        (MappingJackson2HttpMessageConverter) converter;
                jackson2HttpMessageConverter.setObjectMapper(objectMapper());
            }
        }
//        super.extendMessageConverters(converters);
    }

}
