package net.getnova.backend.service;

import com.google.inject.Binder;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.injection.InjectionHandler;
import net.getnova.backend.service.event.InitServiceEvent;
import net.getnova.backend.service.event.PostInitServiceEvent;
import net.getnova.backend.service.event.PreInitServiceEvent;
import net.getnova.backend.service.event.StartServiceEvent;
import net.getnova.backend.service.event.StopServiceEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * This is an handler witch handles all services.
 */
@Slf4j
public final class ServiceHandler {

  private final InjectionHandler injectionHandler;
  private final Map<Class<?>, ServiceData> services;

  /**
   * This is an handler witch handles all services.
   *
   * @param injectionHandler the global instance of the {@link InjectionHandler}
   */
  public ServiceHandler(final InjectionHandler injectionHandler) {
    this.injectionHandler = injectionHandler;
    this.services = new HashMap<>();
  }

  /**
   * This method adds a service.
   *
   * @param service the class of the new service.
   * @param <T>     the type of the service
   * @return an instance of the service
   * @see ServiceHandler#getService(Class)
   */
  public <T> T addService(final Class<? extends T> service) {
    final ServiceData data = ServiceParser.parseService(this.injectionHandler, service);
    if (data == null) {
      log.error("Can't register service class {}.", service.getName());
      return null;
    }
    this.services.put(service, data);
    return service.cast(data.getInstance());
  }

  /**
   * This method gets a Service by it's class type.
   *
   * @param clazz the class type of the service
   * @param <T>   the service
   * @return the service
   */
  public <T> T getService(final Class<? extends T> clazz) {
    return clazz.cast(this.getServiceData(clazz).getInstance());
  }

  /**
   * This gets the service data of the specified class type.
   *
   * @param clazz the class type of the service
   * @return the service data of the service
   */
  public ServiceData getServiceData(final Class<?> clazz) {
    return this.services.get(clazz);
  }

  /**
   * Checks if the specified service exist.
   *
   * @param clazz the class type of the service
   * @return if the service exist
   */
  public boolean hasService(final Class<?> clazz) {
    return this.services.containsKey(clazz);
  }

  /**
   * This method pre inits all services.
   *
   * @param event the event data for the pre init
   */
  public void preInit(final PreInitServiceEvent event) {
    try {
      ServiceExecutor.preInitServices(this, this.services.values(), event);
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
      ServiceExecutor.initServices(this, this.services.values(), event);
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
      ServiceExecutor.postInitServices(this, this.services.values(), event);
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
      ServiceExecutor.startServices(this, this.services.values(), event);
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
      ServiceExecutor.stopServices(this, this.services.values(), event);
    } catch (ServiceException e) {
      log.error("Unable to stop services.", e);
    }
  }

  /**
   * This method create all bindings for all services.
   *
   * @param binder the binder for the binding process
   */
  public void createBindings(final Binder binder) {
    this.services.forEach((clazz, data) -> this.bindService(binder, clazz, data));
  }

  private <T> void bindService(final Binder binder, final Class<T> type, final ServiceData data) {
    binder.bind(type).toInstance(type.cast(data.getInstance()));
  }
}
