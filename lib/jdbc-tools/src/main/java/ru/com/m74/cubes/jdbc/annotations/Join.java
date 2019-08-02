package ru.com.m74.cubes.jdbc.annotations;

import java.lang.annotation.*;

/**
 * @author mixam
 * @since 04.08.16 17:35
 */
@Inherited
@Target(value = ElementType.TYPE)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Join {
    String[] value();
}
