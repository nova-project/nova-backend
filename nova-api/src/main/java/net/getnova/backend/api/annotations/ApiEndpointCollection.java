package net.getnova.backend.api.annotations;

import net.getnova.backend.api.ApiAuthenticator;
import net.getnova.backend.api.ApiModule;
import net.getnova.backend.api.data.ApiType;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiEndpointCollection {

  String id();

  String[] description();

  ApiType type();

  boolean disabled() default false;

  Class<? extends ApiAuthenticator> authenticator() default ApiModule.DefaultApiAuthenticator.class;
}
