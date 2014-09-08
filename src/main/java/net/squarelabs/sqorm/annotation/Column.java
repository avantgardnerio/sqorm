package net.squarelabs.sqorm.annotation;

public @interface Column {

    String name() default "";

    int pkOrdinal() default 0;

    boolean nullable() default true;

}
