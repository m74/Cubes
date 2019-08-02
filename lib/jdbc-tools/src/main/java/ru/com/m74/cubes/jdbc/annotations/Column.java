package ru.com.m74.cubes.jdbc.annotations;

import java.lang.annotation.*;
import java.sql.ResultSet;

/**
 * Поля класса, аннотированные данной аннотацией будут автоматически заполнены
 * значениями из {@link ResultSet} при выполнении метода
 * выполнении класса.<br>
 * <br>
 * Если value не указано, тогда из {@link ResultSet} будет выбираться значение по имени поля.
 * Если value указано, тогда оно будет использоваться для выборки из {@link ResultSet}
 * <br><br>
 * Значение типа поля будет использоваться в методе
 * для преобразования значения поля из {@link ResultSet} в значение целевого поля.
 *
 * @author Zaur Muhametgaleev
 * @since 1/28/16 3:27 PM
 */
@Inherited
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface Column {
    String value() default "";

    /**
     * @deprecated use value = "alias.field_name"
     * @return
     */
    String alias() default "";
    String as() default "";
    String sql() default "";
    String[] orderBy() default {};
}
