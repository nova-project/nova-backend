package net.getnova.backend.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiMutation {

    String name();

    String[] description();

    boolean nullable() default false;

    String[] deprecationReason() default {};
}
