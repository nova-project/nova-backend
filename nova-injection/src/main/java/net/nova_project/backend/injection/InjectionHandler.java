package net.nova_project.backend.injection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import lombok.Getter;

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
     */
    public void createBindings(final Stage stage) {
        this.injector = Guice.createInjector(stage, new InjectionModule(this.binders));
    }
}
