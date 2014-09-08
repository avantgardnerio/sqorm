package net.squarelabs.sqorm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    String name() default "";

    int pkOrdinal() default 0;

    boolean nullable() default true;

}
