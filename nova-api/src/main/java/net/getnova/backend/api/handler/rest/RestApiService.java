package net.getnova.backend.api.handler.rest;

import net.getnova.backend.api.data.ApiEndpointCollectionData;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.parser.ApiEndpointCollectionParser;
import net.getnova.backend.boot.module.Module;
import net.getnova.backend.codec.http.server.HttpServerModule;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@ComponentScan
@Module(HttpServerModule.class)
public class RestApiService {

  private final RestApiConfig config;
  private final HttpServerModule httpServerModule;
  private final Set<Object> collections;
  private Set<ApiEndpointCollectionData> collectionsData;

  public RestApiService(final RestApiConfig config, final HttpServerModule httpServerModule) {
    this.config = config;
    this.httpServerModule = httpServerModule;
    this.collections = new LinkedHashSet<>();
  }

  @PostConstruct
  private void postConstruct() {
    this.collectionsData = ApiEndpointCollectionParser.parseCollections(this.collections);
    final Map<String, ApiEndpointData> endpoints = ApiEndpointCollectionParser.getEndpoints(this.collectionsData);
    this.httpServerModule.addLocationProvider(this.config.getPath(), new RestApiLocationProvider(endpoints));
  }

//  @NotNull
//  public <T> T addEndpointCollection(@NotNull final Class<T> collection) {
//    final T instance = this.injectionHandler.getInjector().getInstance(collection);
//    this.collections.add(collection.getDeclaredConstructor().newInstance());
//    return instance;
//  }
}
