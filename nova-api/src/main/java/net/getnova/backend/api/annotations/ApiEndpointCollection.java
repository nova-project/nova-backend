package net.getnova.backend.api.annotations;

import net.getnova.backend.api.data.ApiType;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiEndpointCollection {

  @NotNull
  String id();

  @NotNull
  String[] description();

  @NotNull
  ApiType type();

  boolean disabled() default false;
}
