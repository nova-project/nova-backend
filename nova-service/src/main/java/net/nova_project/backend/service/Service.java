package net.nova_project.backend.service;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {

    /**
     * This is the <i>unique</i> id of the service.
     * Its isn't a problem if the id isn't <i>unique</i>, but it should.
     *
     * @return the <i>unique</i> id of the service
     */
    String value();

    /**
     * This is a array with all depends services.
     *
     * @return a array with all depends services
     */
    Class<?>[] depends() default {};
}
