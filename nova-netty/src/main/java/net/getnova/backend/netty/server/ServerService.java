package net.getnova.backend.netty.server;

import net.getnova.backend.injection.InjectionHandler;
import net.getnova.backend.netty.NettyService;
import net.getnova.backend.service.Service;
import net.getnova.backend.service.event.StartService;
import net.getnova.backend.service.event.StartServiceEvent;
import net.getnova.backend.service.event.StopService;
import net.getnova.backend.service.event.StopServiceEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

@Service(value = "server", depends = {NettyService.class})
@Singleton
public class ServerService {

    private final List<Server> servers;
    @Inject
    private InjectionHandler injectionHandler;

    /**
     * Creates a new {@link ServerService}.
     */
    public ServerService() {
        this.servers = new LinkedList<>();
    }

    @StartService
    private void start(final StartServiceEvent event) {
        this.servers.forEach(Server::start);
    }

    @StopService
    private void stop(final StopServiceEvent event) {
        this.servers.forEach(Server::close);
    }

    /**
     * Adds a server to the managed server lifecycle.
     *
     * @param server the server witch should be added to the lifecycle.
     */
    public void addServer(final Server server) {
        this.injectionHandler.getInjector().injectMembers(server);
        this.servers.add(server);
    }
}
