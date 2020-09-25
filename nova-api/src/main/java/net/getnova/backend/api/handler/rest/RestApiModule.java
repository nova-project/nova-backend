package net.getnova.backend.api.handler.rest;

import net.getnova.backend.api.annotations.ApiEndpointCollection;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.parser.ApiEndpointCollectionParser;
import net.getnova.backend.boot.module.Module;
import net.getnova.backend.network.server.http.HttpServerModule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collection;
import java.util.Map;

@ComponentScan
@Module(HttpServerModule.class)
public class RestApiModule {

  public RestApiModule(final RestApiConfig config,
                       final HttpServerModule httpServerModule,
                       final ApplicationContext context) {
    final Collection<Object> collections = context.getBeansWithAnnotation(ApiEndpointCollection.class).values();
    final Map<String, ApiEndpointData> endpoints = ApiEndpointCollectionParser.getEndpoints(ApiEndpointCollectionParser.parseCollections(collections));
    final RestApiLocation handler = new RestApiLocation(endpoints);
    httpServerModule.getRoutes().addRoute(config.getPath(), handler);
  }
}
