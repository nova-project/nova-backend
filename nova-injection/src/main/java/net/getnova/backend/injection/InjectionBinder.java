package net.getnova.backend.injection;

import com.google.inject.Binder;

/**
 * This interface provide the method {@link InjectionBinder#bindInjections} to collects
 * configuration information (primarily <i>bindings</i>) which will be used to configure
 * the {@link InjectionHandler}.
 *
 * @see InjectionHandler
 * @see InjectionHandler#addInjectionBinder(InjectionBinder)
 */
public interface InjectionBinder {

  /**
   * Collects configuration information (primarily <i>bindings</i>) which
   * will be used to configure the {@link InjectionHandler}.
   *
   * @param binder the {@link Binder} witch collects the information's about the bindings
   */
  void bindInjections(Binder binder);
}
