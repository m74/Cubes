package ru.com.m74.cubes.jdbc.annotations;

import java.lang.annotation.*;

/**
 * @author Zaur Muhametgaleev
 * @since 2/15/16 3:58 AM
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
    String value() default "";
    String alias() default "";
}
