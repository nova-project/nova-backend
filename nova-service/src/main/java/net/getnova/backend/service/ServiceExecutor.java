package net.getnova.backend.service;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.service.event.InitServiceEvent;
import net.getnova.backend.service.event.PostInitServiceEvent;
import net.getnova.backend.service.event.PreInitServiceEvent;
import net.getnova.backend.service.event.StartServiceEvent;
import net.getnova.backend.service.event.StopServiceEvent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@Slf4j
final class ServiceExecutor {

    private ServiceExecutor() {
        throw new UnsupportedOperationException();
    }

    static void preInitServices(final ServiceHandler serviceHandler,
                                final Collection<ServiceData> services,
                                final PreInitServiceEvent event) throws ServiceException {
        executeStep(serviceHandler,
                new LinkedList<>(services),
                ServiceData::isPreInit,
                ServiceData::getPreInitMethod,
                serviceData -> serviceData.setPreInit(true),
                event,
                "pre init",
                false);
    }

    static void initServices(final ServiceHandler serviceHandler,
                             final Collection<ServiceData> services,
                             final InitServiceEvent event) throws ServiceException {
        executeStep(serviceHandler,
                new LinkedList<>(services),
                ServiceData::isInit,
                ServiceData::getInitMethod,
                serviceData -> serviceData.setInit(true),
                event,
                "init",
                false);
    }

    static void postInitServices(final ServiceHandler serviceHandler,
                                 final Collection<ServiceData> services,
                                 final PostInitServiceEvent event) throws ServiceException {
        executeStep(serviceHandler,
                new LinkedList<>(services),
                ServiceData::isPostInit,
                ServiceData::getPostInitMethod,
                serviceData -> serviceData.setPostInit(true),
                event,
                "post init",
                false);
    }

    static void startServices(final ServiceHandler serviceHandler,
                              final Collection<ServiceData> services,
                              final StartServiceEvent event) throws ServiceException {
        executeStep(serviceHandler,
                new LinkedList<>(services),
                ServiceData::isStarted,
                ServiceData::getStartMethod,
                serviceData -> serviceData.setStarted(true),
                event,
                "start",
                false);
    }

    static void stopServices(final ServiceHandler serviceHandler,
                             final Collection<ServiceData> services,
                             final StopServiceEvent event) throws ServiceException {
        executeStep(serviceHandler,
                new LinkedList<>(services),
                serviceData -> !serviceData.isStarted(),
                ServiceData::getStopMethod,
                serviceData -> serviceData.setStarted(false),
                event,
                "stop",
                true);
    }

    @SuppressWarnings("checkstyle:WhitespaceAfter")
    private static void executeStep(final ServiceHandler serviceHandler,
                                    final Collection<ServiceData> services,
                                    final Function<ServiceData, Boolean> skipService,
                                    final Function<ServiceData, Method> function,
                                    final Consumer<ServiceData> updateState,
                                    final Object eventData, final String action,
                                    final boolean invertDepends) throws ServiceException {

        final Collection<ServiceData> depends = getDepends(serviceHandler, services);
        if (!invertDepends && !depends.isEmpty()) {
            executeStep(serviceHandler, depends, skipService, function, updateState, eventData, action, false);
        }

        for (final ServiceData service : services) {
            if (!skipService.apply(service)) {
                updateState.accept(service);
                final Method method = function.apply(service);
                if (method != null) {
                    method.setAccessible(true);
                    try {
                        method.invoke(service.getInstance(), eventData);
                    } catch (InvocationTargetException e) {
                        throw new ServiceException("Unable to " + action + " service "
                                + service.getClazz().getName() + ".", e);
                    } catch (IllegalAccessException e) {
                        log.error("Unable to " + action + " service " + service.getClazz().getName() + ".", e);
                    }
                    method.setAccessible(false);
                }
            }
        }

        if (invertDepends && !depends.isEmpty()) {
            executeStep(serviceHandler, depends, skipService, function, updateState, eventData, action, true);
        }
    }

    private static Collection<ServiceData> getDepends(final ServiceHandler serviceHandler,
                                                      final Collection<ServiceData> services) {
        final List<ServiceData> dependServices = new LinkedList<>();

        services.iterator().forEachRemaining(service -> {
            try {
                for (final Class<?> depend : service.getDepends()) {
                    if (!serviceHandler.hasService(depend)) serviceHandler.addService(depend);
                    dependServices.add(serviceHandler.getServiceData(depend));
                }
            } catch (NullPointerException e) {
                log.error("It is not possible to initialize the service, maybe a non-existent service is requested.", e);
            }
        });

        return dependServices;
    }
}
