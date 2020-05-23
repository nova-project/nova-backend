package net.nova_project.backend.injection;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
class InjectionModule extends AbstractModule {

    private final Set<InjectionBinder> binders;

    @Override
    protected final void configure() {
        final Binder binder = this.binder();
        this.binders.forEach(injectionBinder -> injectionBinder.bindInjections(binder));
    }
}
