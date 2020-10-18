package net.getnova.backend.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiEndpoint {

  String id();

  String[] description();

  int authentication() default 0;

  boolean disabled() default false;
}
