package net.nova_project.backend.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
@EqualsAndHashCode
class ServiceData {

    private final String id;
    private final Class<?>[] depends;

    private final Object instance;

    private final Method preInitMethod;
    private final Method initMethod;
    private final Method postInitMethod;
    private final Method startMethod;
    private final Method stopMethod;
    private boolean preInit;
    private boolean init;
    private boolean postInit;
    private boolean started;

    ServiceData(final String id,
                final Class<?>[] depends,
                final Object instance,
                final Method preInitMethod,
                final Method initMethod,
                final Method postInitMethod,
                final Method startMethod,
                final Method stopMethod) {
        this.id = id;
        this.depends = depends;

        this.instance = instance;

        this.preInitMethod = preInitMethod;
        this.preInit = false;

        this.initMethod = initMethod;
        this.init = false;

        this.postInitMethod = postInitMethod;
        this.postInit = false;

        this.startMethod = startMethod;
        this.stopMethod = stopMethod;
        this.started = false;
    }
}
