package net.nova_project.backend.service;

import lombok.extern.slf4j.Slf4j;
import net.nova_project.backend.service.event.InitServiceEvent;
import net.nova_project.backend.service.event.PostInitServiceEvent;
import net.nova_project.backend.service.event.PreInitServiceEvent;
import net.nova_project.backend.service.event.StartServiceEvent;
import net.nova_project.backend.service.event.StopServiceEvent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is an handler witch handles all services.
 */
@Slf4j
public final class ServiceHandler {

    private final Map<Class<?>, ServiceData> services;
    private final Set<Class<?>> enabledServices;

    /**
     * This is an handler witch handles all services.
     */
    public ServiceHandler() {
        this.services = new HashMap<>();
        this.enabledServices = new HashSet<>();
    }

    /**
     * This method adds a service.
     *
     * @param service the class of the new service.
     * @see ServiceHandler#getService(Class)
     */
    public void addService(final Class<?> service) {
        final ServiceData data = ServiceParser.parseService(service);
        if (data == null) {
            log.error("Can't register service class {}.", service.getName());
            return;
        }
        this.services.put(service, data);
    }

    /**
     * This method gets a Service by it's class type.
     *
     * @param clazz the class type of the service
     * @param <T>   the service
     * @return the service
     */
    public <T> T getService(final Class<? extends T> clazz) {
        return clazz.cast(this.services.get(clazz));
    }

    /**
     * This method enables a service. And if the service isn't
     * registered it will be registers automatically.
     *
     * @param service the service witch should be enabled.
     */
    public void enableService(final Class<?> service) {
        if (!this.services.containsKey(service)) this.addService(service);
        this.enabledServices.add(service);
    }

    /**
     * This method pre inits all services.
     *
     * @param event the event data for the pre init
     */
    public void preInit(final PreInitServiceEvent event) {
        try {
            ServiceExecutor.preInitServices(this.services, this.enabledServices, event);
        } catch (ServiceException e) {
            log.error("Unable to pre init services.", e);
        }
    }

    /**
     * This method inits all services.
     *
     * @param event the event data for the init
     */
    public void init(final InitServiceEvent event) {
        try {
            ServiceExecutor.initServices(this.services, this.enabledServices, event);
        } catch (ServiceException e) {
            log.error("Unable to init services.", e);
        }
    }

    /**
     * This method post inits all services.
     *
     * @param event the event data for the post init
     */
    public void postInit(final PostInitServiceEvent event) {
        try {
            ServiceExecutor.postInitServices(this.services, this.enabledServices, event);
        } catch (ServiceException e) {
            log.error("Unable to post init services.", e);
        }
    }

    /**
     * This method starts all services.
     *
     * @param event the event data for the start
     */
    public void start(final StartServiceEvent event) {
        try {
            ServiceExecutor.startServices(this.services, this.enabledServices, event);
        } catch (ServiceException e) {
            log.error("Unable to start services.", e);
        }
    }

    /**
     * This method stops all services.
     *
     * @param event the event data for the stop
     */
    public void stop(final StopServiceEvent event) {
        try {
            ServiceExecutor.stopServices(this.services, this.enabledServices, event);
        } catch (ServiceException e) {
            log.error("Unable to stop services.", e);
        }
    }
}
