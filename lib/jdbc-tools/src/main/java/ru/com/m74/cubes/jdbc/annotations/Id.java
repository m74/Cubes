package ru.com.m74.cubes.jdbc.annotations;

import java.lang.annotation.*;

/**
 * @author Zaur Muhametgaleev
 * @since 2/15/16 12:14 PM
 *
 * Если Value задана, то значение используем в качестве sequence
 */
@Inherited
@Target(value= ElementType.FIELD)
@Retention(value= RetentionPolicy.RUNTIME)
public @interface Id {
    String sequenceGenerator() default "";
    String name() default "";
}
