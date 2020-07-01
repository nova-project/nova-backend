package net.getnova.backend.api.annotations;

import net.getnova.backend.api.data.ApiParameterType;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ApiParameter {

    @NotNull
    String id();

    boolean required() default true;

    ApiParameterType type() default ApiParameterType.NORMAL;

    @NotNull
    String[] description();
}
