package net.getnova.backend.service;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.injection.InjectionHandler;
import net.getnova.backend.service.event.InitService;
import net.getnova.backend.service.event.InitServiceEvent;
import net.getnova.backend.service.event.PostInitService;
import net.getnova.backend.service.event.PostInitServiceEvent;
import net.getnova.backend.service.event.PreInitService;
import net.getnova.backend.service.event.PreInitServiceEvent;
import net.getnova.backend.service.event.StartService;
import net.getnova.backend.service.event.StartServiceEvent;
import net.getnova.backend.service.event.StopService;
import net.getnova.backend.service.event.StopServiceEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Slf4j
final class ServiceParser {

    private ServiceParser() {
        throw new UnsupportedOperationException();
    }

    static ServiceData parseService(final InjectionHandler injectionHandler, final Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Service.class)) {
            log.error("Service class {} is not annotated with {}.", clazz.getName(), Service.class.getName());
            return null;
        }

        final Service service = clazz.getAnnotation(Service.class);

        return new ServiceData(service.id(), service.depends(),
                injectionHandler.getInjector().getInstance(clazz), // Dont create every time a new instance
                getMethod(clazz, PreInitService.class, PreInitServiceEvent.class),
                getMethod(clazz, InitService.class, InitServiceEvent.class),
                getMethod(clazz, PostInitService.class, PostInitServiceEvent.class),
                getMethod(clazz, StartService.class, StartServiceEvent.class),
                getMethod(clazz, StopService.class, StopServiceEvent.class)
        );
    }

    private static Method getMethod(final Class<?> clazz,
                                    final Class<? extends Annotation> annotation,
                                    final Class<?> eventClass) {
        for (final Method method : clazz.getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.isAnnotationPresent(annotation)) {
                if (checkMethod(method, eventClass)) return method;
                else return null;
            }
        }
        return null;
    }

    private static boolean checkMethod(final Method method, final Class<?> eventClass) {
        final Parameter[] parameters = method.getParameters();
        if (parameters.length != 1 || !parameters[0].getType().equals(eventClass)) {
            log.error(
                    "Service event method {} does not have the right parameter count or type. Required is {}.",
                    method.getDeclaringClass().getName() + "." + method.getName(),
                    eventClass.getName()
            );
            return false;
        }

        if (!Void.TYPE.isAssignableFrom(method.getReturnType())) {
            log.error(
                    "Service event method {} does not have void as return type.",
                    method.getDeclaringClass().getName() + "." + method.getName()
            );
            return false;
        }
        return true;
    }
}
