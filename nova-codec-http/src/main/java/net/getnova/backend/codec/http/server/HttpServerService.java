package net.getnova.backend.codec.http.server;

import net.getnova.backend.config.ConfigService;
import net.getnova.backend.injection.InjectionHandler;
import net.getnova.backend.netty.codec.CodecHandler;
import net.getnova.backend.netty.server.InetServer;
import net.getnova.backend.netty.server.ServerService;
import net.getnova.backend.service.Service;
import net.getnova.backend.service.event.PostInitService;
import net.getnova.backend.service.event.PostInitServiceEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.InetSocketAddress;

@Service(value = "http-server", depends = {ConfigService.class, ServerService.class})
@Singleton
public class HttpServerService {

    private final HttpServerConfig config;
    private final HttpServerCodec codec;
    @Inject
    private ServerService serverService;

    /**
     * Creates a new Http Service.
     *
     * @param configService    the instance of the {@link ConfigService}
     * @param injectionHandler the instance of the {@link InjectionHandler}
     */
    @Inject
    public HttpServerService(final ConfigService configService, final InjectionHandler injectionHandler) {
        this.config = configService.addConfig("httpServer", new HttpServerConfig());
        this.codec = new HttpServerCodec(injectionHandler);
    }

    @PostInitService
    private void postInit(final PostInitServiceEvent event) {
        this.serverService.addServer(new InetServer("http",
                new InetSocketAddress(this.config.getHost(), this.config.getPort()),
                null,
                new CodecHandler(this.codec)));
    }

    /**
     * Adds a new {@link HttpLocationProvider} to the {@link java.util.Map} of location providers.
     *
     * @param path     the path to the {@link HttpLocation}. Without a {@code /} at the start
     * @param provider {@link HttpLocationProvider} instance of the {@link HttpLocation}
     * @see HttpLocationProvider
     * @see HttpLocation
     */
    public void addLocationProvider(final String path, final HttpLocationProvider<?> provider) {
        this.codec.addLocationProvider(path.toLowerCase(), provider);
    }
}
