package net.nova_project.backend;

import com.google.inject.Stage;
import lombok.extern.slf4j.Slf4j;
import net.nova_project.backend.injection.InjectionHandler;
import net.nova_project.backend.logging.NovaLogConfigurer;
import net.nova_project.backend.service.ServiceHandler;
import net.nova_project.backend.service.event.InitServiceEvent;
import net.nova_project.backend.service.event.PostInitServiceEvent;
import net.nova_project.backend.service.event.PreInitServiceEvent;
import net.nova_project.backend.service.event.StartServiceEvent;
import net.nova_project.backend.service.event.StopServiceEvent;

/**
 * This is the main class, of the nova backend.
 */
@Slf4j
public final class Nova {

    private final InjectionHandler injectionHandler;
    private final ServiceHandler serviceHandler;

    /**
     * This is the "main" entry point of the nova backend.
     *
     * @param args the program arguments
     * @see Bootstrap#main(String[])
     */
    Nova(final String[] args) {
        NovaLogConfigurer.redirectSysLog();
        this.injectionHandler = new InjectionHandler();
        this.serviceHandler = new ServiceHandler(this.injectionHandler);

        this.init();
        this.start();
    }

    private void enableServices() {
        this.serviceHandler.enableService(TestService.class);
    }

    private void init() {
        log.info("Initializing backend...");
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "shutdown-thread"));

        this.injectionHandler.addInjectionBinder(binder -> {
            binder.bind(Nova.class).toInstance(this);
            binder.bind(InjectionHandler.class).toInstance(this.injectionHandler);
            binder.bind(ServiceHandler.class).toInstance(this.serviceHandler);
        });
        this.injectionHandler.createBindings(Stage.DEVELOPMENT);
        this.enableServices();

        // PRE INIT
        // TODO Use config to change between development and production
        this.injectionHandler.addInjectionBinder(
                binder -> this.serviceHandler.preInit(new PreInitServiceEvent(binder))
        );
        this.injectionHandler.recreateBindings();

        // INIT
        this.serviceHandler.init(new InitServiceEvent());

        // POST INIT
        this.serviceHandler.postInit(new PostInitServiceEvent());

        log.info("Initialized backend...");
    }

    private void start() {
        log.info("Starting backend...");
        this.serviceHandler.start(new StartServiceEvent());
        log.info("Started backend...");
    }

    private void stop() {
        log.info("Stopping backend...");
        this.serviceHandler.stop(new StopServiceEvent());
        log.info("Stopped backend...");
    }

    /**
     * This method triggers the shutdown progress of the backend.
     */
    public void shutdown() {
        this.stop();
    }
}
