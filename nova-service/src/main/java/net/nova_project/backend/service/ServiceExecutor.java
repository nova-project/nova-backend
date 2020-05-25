package net.nova_project.backend.service;

import lombok.extern.slf4j.Slf4j;
import net.nova_project.backend.service.event.InitServiceEvent;
import net.nova_project.backend.service.event.PostInitServiceEvent;
import net.nova_project.backend.service.event.PreInitServiceEvent;
import net.nova_project.backend.service.event.StartServiceEvent;
import net.nova_project.backend.service.event.StopServiceEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
final class ServiceExecutor {

    private ServiceExecutor() {
        throw new UnsupportedOperationException();
    }

    static void preInitServices(final Map<Class<?>, ServiceData> services,
                                final Set<Class<?>> servicesToInit,
                                final PreInitServiceEvent event) throws ServiceException {
        executeStep(services,
                servicesToInit,
                ServiceData::isPreInit,
                ServiceData::getPreInitMethod,
                serviceData -> serviceData.setPreInit(true),
                event,
                "pre init",
                false);
    }

    static void initServices(final Map<Class<?>, ServiceData> services,
                             final Set<Class<?>> servicesToInit,
                             final InitServiceEvent event) throws ServiceException {
        executeStep(services,
                servicesToInit,
                ServiceData::isInit,
                ServiceData::getInitMethod,
                serviceData -> serviceData.setInit(true),
                event,
                "init",
                false);
    }

    static void postInitServices(final Map<Class<?>, ServiceData> services,
                                 final Set<Class<?>> servicesToInit,
                                 final PostInitServiceEvent event) throws ServiceException {
        executeStep(services,
                servicesToInit,
                ServiceData::isPostInit,
                ServiceData::getPostInitMethod,
                serviceData -> serviceData.setPostInit(true),
                event,
                "post init",
                false);
    }

    static void startServices(final Map<Class<?>, ServiceData> services,
                              final Set<Class<?>> servicesToInit,
                              final StartServiceEvent event) throws ServiceException {
        executeStep(services,
                servicesToInit,
                ServiceData::isStarted,
                ServiceData::getStartMethod,
                serviceData -> serviceData.setStarted(true),
                event,
                "start",
                false);
    }

    static void stopServices(final Map<Class<?>, ServiceData> services,
                             final Set<Class<?>> servicesToInit,
                             final StopServiceEvent event) throws ServiceException {
        executeStep(services,
                servicesToInit,
                serviceData -> !serviceData.isStarted(),
                ServiceData::getStopMethod,
                serviceData -> serviceData.setStarted(false),
                event,
                "stop",
                true);
    }

    private static void executeStep(final Map<Class<?>, ServiceData> services,
                                    final Set<Class<?>> servicesToProcess,
                                    final Function<ServiceData, Boolean> skipService,
                                    final Function<ServiceData, Method> function,
                                    final Consumer<ServiceData> updateState,
                                    final Object eventData, final String action,
                                    final boolean invertDepends) throws ServiceException {
        final Set<Class<?>> depends = getDepends(services, servicesToProcess);
        if (!invertDepends && !depends.isEmpty()) {
            executeStep(services, depends, skipService, function, updateState, eventData, action, false);
        }

        for (final Class<?> service : servicesToProcess) {
            final ServiceData serviceData = services.get(service);

            if (!skipService.apply(serviceData)) {
                final Method method = function.apply(serviceData);
                if (method != null) {
                    method.setAccessible(true);
                    try {
                        method.invoke(serviceData.getInstance(), eventData);
                        updateState.accept(serviceData);
                    } catch (InvocationTargetException e) {
                        throw new ServiceException("Unable to " + action + " service " + service.getName() + ".", e);
                    } catch (IllegalAccessException e) {
                        log.error("Unable to " + action + " service " + service.getName() + ".", e);
                    }
                    method.setAccessible(false);
                }
            }
        }

        if (invertDepends && !depends.isEmpty()) {
            executeStep(services, depends, skipService, function, updateState, eventData, action, true);
        }
    }

    private static Set<Class<?>> getDepends(final Map<Class<?>, ServiceData> services,
                                            final Set<Class<?>> servicesToInit) {
        return servicesToInit.stream()
                .flatMap(service -> Arrays.stream(services.get(service).getDepends()))
                .collect(Collectors.toSet());
    }
}
