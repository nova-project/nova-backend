package net.getnova.backend.api.annotations;

import net.getnova.backend.api.data.ApiParameterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParameter {

  String id();

  boolean required() default true;

  ApiParameterType type() default ApiParameterType.NORMAL;

  String[] description();
}
