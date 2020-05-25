package net.nova_project.backend.injection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

/**
 * This handle the dependency injection of the nova backend.
 *
 * @see InjectionHandler#addInjectionBinder(InjectionBinder)
 * @see InjectionHandler#createBindings(Stage)
 * @see InjectionHandler#getInjector()
 * @see InjectionBinder
 */
@Slf4j
public class InjectionHandler {

    private final Set<InjectionBinder> binders;
    @Getter
    private Injector injector;

    /**
     * This creates a new instance of the {@link InjectionHandler}, to handle
     * the dependency injection of the nova backend.
     */
    public InjectionHandler() {
        this.binders = new HashSet<>();
    }

    /**
     * This method collects {@link InjectionBinder}'s witch collects
     * configuration information (primarily <i>bindings</i>) to configure
     * the {@link InjectionHandler}, which is then able to provide them with bindings.
     *
     * @param binder the {@link InjectionBinder} witch collects the
     *               configuration (primarily <i>bindings</i>) information
     * @see InjectionBinder
     */
    public void addInjectionBinder(final InjectionBinder binder) {
        this.binders.add(binder);
    }

    /**
     * This method trigger the internal Guice registration for all before in
     * {@link InjectionHandler#addInjectionBinder(InjectionBinder)} configured bindings.
     *
     * @param stage the {@link Stage} witch should be used for the Guice internal injection.
     * @see InjectionHandler#recreateBindings()
     */
    public void createBindings(final Stage stage) {
        if (this.injector == null) this.injector = Guice.createInjector(stage, new InjectionModule(this.binders));
        else {
            log.warn("The bindings are already created. Use {} instead.", InjectionHandler.class.getName() + ".recreateBindings()");
        }
    }

    /**
     * This method trigger the internal Guice reregistration for all before in
     * {@link InjectionHandler#addInjectionBinder(InjectionBinder)} configured bindings.
     */
    public void recreateBindings() {
        this.injector = this.injector.createChildInjector(new InjectionModule(this.binders));
    }
}
