package net.nova_project.backend.service;

import lombok.extern.slf4j.Slf4j;
import net.nova_project.backend.service.event.InitService;
import net.nova_project.backend.service.event.InitServiceEvent;
import net.nova_project.backend.service.event.PostInitService;
import net.nova_project.backend.service.event.PostInitServiceEvent;
import net.nova_project.backend.service.event.PreInitService;
import net.nova_project.backend.service.event.PreInitServiceEvent;
import net.nova_project.backend.service.event.StartService;
import net.nova_project.backend.service.event.StartServiceEvent;
import net.nova_project.backend.service.event.StopService;
import net.nova_project.backend.service.event.StopServiceEvent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Slf4j
final class ServiceParser {

    private ServiceParser() {
        throw new UnsupportedOperationException();
    }

    static ServiceData parseService(final Class<?> clazz) {
        if (!clazz.isAnnotationPresent(Service.class)) {
            log.error("Service class {} is not annotated with {}.", clazz.getName(), Service.class.getName());
            return null;
        }

        final Service service = clazz.getAnnotation(Service.class);
        try {
            final Constructor<?> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            final Object instance = constructor.newInstance();
            constructor.setAccessible(false);

            return new ServiceData(service.value(), service.depends(),
                    instance,
                    getMethod(clazz, PreInitService.class, PreInitServiceEvent.class),
                    getMethod(clazz, InitService.class, InitServiceEvent.class),
                    getMethod(clazz, PostInitService.class, PostInitServiceEvent.class),
                    getMethod(clazz, StartService.class, StartServiceEvent.class),
                    getMethod(clazz, StopService.class, StopServiceEvent.class)
            );
        } catch (NoSuchMethodException e) {
            log.error("Service class {} does not has empty constructor.", clazz.getName());
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            log.error("Unable to load service class {}.", clazz.getName());
        }
        return null;
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
