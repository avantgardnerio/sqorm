package net.squarelabs.sqorm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    String name() default "";

    int pkOrdinal() default -1;

    boolean nullable() default true;

    boolean isVersion() default false;

}
