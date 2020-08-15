package net.getnova.backend.handler;

import lombok.EqualsAndHashCode;
import net.getnova.backend.injection.InjectionHandler;
import net.getnova.backend.service.ServiceHandler;
import net.getnova.backend.service.event.PreInitServiceEvent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@EqualsAndHashCode
public final class PreInitHandler implements BootstrapHandler {

  @Inject
  private InjectionHandler injectionHandler;

  @Inject
  private ServiceHandler serviceHandler;

  @Override
  public void execute() {
    this.injectionHandler.addInjectionBinder(
      binder -> this.serviceHandler.preInit(new PreInitServiceEvent(binder))
    );
    this.injectionHandler.recreateBindings();
  }
}
