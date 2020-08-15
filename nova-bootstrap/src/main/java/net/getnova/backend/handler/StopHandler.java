package net.getnova.backend.handler;

import lombok.EqualsAndHashCode;
import net.getnova.backend.injection.InjectionHandler;
import net.getnova.backend.service.ServiceHandler;
import net.getnova.backend.service.event.StopServiceEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@EqualsAndHashCode
public final class StopHandler implements BootstrapHandler {

  @Inject
  private InjectionHandler injectionHandler;

  @Inject
  private ServiceHandler serviceHandler;

  @Override
  public void execute() {
    this.serviceHandler.stop(new StopServiceEvent());
  }
}
