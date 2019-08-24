package ru.com.m74.cubes.jdbc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mixam
 * @since 30.07.16 20:28
 */
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface LinkTo {
    String table();

    String id() default "ID";

    Class idType() default Long.class;

    String title() default "";

    /**
     * Business key field
     *
     * @return
     */
    String bk() default "";

    /**
     * Переопределение условий джоина
     *
     * @return
     */
//    String on() default "";

    /**
     * Запрос для получения значения поля title
     *
     * @return
     */
    String titleQuery() default "";

    /**
     * Alias
     *
     * @return
     */
//    String as() default "";
}
