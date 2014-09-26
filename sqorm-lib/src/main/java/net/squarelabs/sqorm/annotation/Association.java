package net.squarelabs.sqorm.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Association {

    String name() default "";

    String[] primaryKey();

    String[] foreignKey();

    boolean isForeignKey() default false;
}
