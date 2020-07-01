package net.getnova.backend.api.handler.rest;

import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.parser.ApiEndpointCollectionParser;
import net.getnova.backend.codec.http.server.HttpServerService;
import net.getnova.backend.injection.InjectionHandler;
import net.getnova.backend.service.Service;
import net.getnova.backend.service.event.PostInitService;
import net.getnova.backend.service.event.PostInitServiceEvent;
import net.getnova.backend.service.event.PreInitService;
import net.getnova.backend.service.event.PreInitServiceEvent;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Service(id = "rest-api", depends = HttpServerService.class)
@Singleton
public final class RestApiService {

    private final Set<Object> collections;

    @Inject
    private HttpServerService httpServerService;

    @Inject
    private InjectionHandler injectionHandler;

    public RestApiService() {
        this.collections = new LinkedHashSet<>();
    }

    @PreInitService
    private void preInit(final PreInitServiceEvent event) {
        this.addEndpointCollection(TestEndpointCollection.class);
    }

    @PostInitService
    private void postInit(@NotNull final PostInitServiceEvent event) {
        final Map<String, ApiEndpointData> endpoints = ApiEndpointCollectionParser.getEndpoints(ApiEndpointCollectionParser.parseCollections(this.collections));
        this.httpServerService.addLocationProvider("api", new RestApiLocationProvider(endpoints));
    }

    @NotNull
    public <T> T addEndpointCollection(@NotNull final Class<T> collection) {
        final T instance = this.injectionHandler.getInjector().getInstance(collection);
        this.collections.add(instance);
        return instance;
    }
}
