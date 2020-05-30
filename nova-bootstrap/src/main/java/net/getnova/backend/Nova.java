package net.getnova.backend;

import com.google.inject.Stage;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.config.ConfigService;
import net.getnova.backend.handler.InitHandler;
import net.getnova.backend.handler.PostInitHandler;
import net.getnova.backend.handler.PreInitHandler;
import net.getnova.backend.handler.StartHandler;
import net.getnova.backend.handler.StopHandler;
import net.getnova.backend.injection.InjectionHandler;
import net.getnova.backend.logging.NovaLogConfigurer;
import net.getnova.backend.service.ServiceHandler;

/**
 * This is the main class, of the nova backend.
 */
@Slf4j
public final class Nova {

    private final PreInitHandler preInitHandler;
    private final InitHandler initHandler;
    private final PostInitHandler postInitHandler;
    private final StartHandler startHandler;
    private final StopHandler stopHandler;

    private final InjectionHandler injectionHandler;
    private final ServiceHandler serviceHandler;

    private NovaConfig config;
    private ConfigService configService;

    /**
     * This is the "main" entry point of the nova backend.
     *
     * @param args the program arguments
     * @see Bootstrap#main(String[])
     */
    Nova(final String[] args) {
        log.info(" _   _                   ____             _                  _");
        log.info("| \\ | |                 |  _ \\           | |                | |");
        log.info("|  \\| | _____   ____ _  | |_) | __ _  ___| | _____ _ __   __| |");
        log.info("| . ` |/ _ \\ \\ / / _` | |  _ < / _` |/ __| |/ / _ \\ '_ \\ / _` |");
        log.info("| |\\  | (_) \\ V / (_| | | |_) | (_| | (__|   <  __/ | | | (_| |");
        log.info("|_| \\_|\\___/ \\_/ \\__,_| |____/ \\__,_|\\___|_|\\_\\___|_| |_|\\__,_|");

        NovaLogConfigurer.redirectSysLog();
        this.injectionHandler = new InjectionHandler();
        this.serviceHandler = new ServiceHandler(this.injectionHandler);

        this.injectionHandler.addInjectionBinder(binder -> {
            binder.bind(Nova.class).toInstance(this);
            binder.bind(InjectionHandler.class).toInstance(this.injectionHandler);
            binder.bind(ServiceHandler.class).toInstance(this.serviceHandler);
        });
        this.injectionHandler.createBindings(Stage.DEVELOPMENT);

        this.preInitHandler = this.injectionHandler.getInjector().getInstance(PreInitHandler.class);
        this.initHandler = this.injectionHandler.getInjector().getInstance(InitHandler.class);
        this.postInitHandler = this.injectionHandler.getInjector().getInstance(PostInitHandler.class);
        this.startHandler = this.injectionHandler.getInjector().getInstance(StartHandler.class);
        this.stopHandler = this.injectionHandler.getInjector().getInstance(StopHandler.class);

        this.init();
        this.start();
    }

    private void registerServices() {
        this.configService = this.serviceHandler.addService(ConfigService.class);
    }

    /**
     * This method triggers the initialization progress of the backend.
     */
    private void init() {
        log.info("Initializing backend...");

        /* Adding all services which are required to run the minimal size of the backend. */
        this.registerServices();

        /* Nova Internal Stuff */
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown, "shutdown-thread"));
        this.config = this.configService.addConfig("", new NovaConfig());

        this.preInitHandler.execute();
        if (this.config.isDebug()) log.info("Running in debug mode...");
        this.initHandler.execute();
        this.postInitHandler.execute();
        log.info("Initialized backend...");
    }

    /**
     * This method triggers the starting progress of the backend.
     */
    private void start() {
        log.info("Starting backend...");
        this.startHandler.execute();
        log.info("Started backend...");
    }

    /**
     * This method triggers the shutdown progress of the backend.
     */
    public void shutdown() {
        log.info("Stopping backend...");
        this.stopHandler.execute();
        log.info("Stopped backend...");
    }

    /**
     * Returns the current state of the backend. (debug or no debug)
     *
     * @return the current state of the backend
     */
    public boolean isDebug() {
        return this.config.isDebug();
    }
}
