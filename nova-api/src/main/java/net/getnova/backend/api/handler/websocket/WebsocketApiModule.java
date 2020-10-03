package net.getnova.backend.api.handler.websocket;

import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.annotations.ApiEndpointCollection;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.parser.ApiEndpointCollectionParser;
import net.getnova.backend.boot.module.Module;
import net.getnova.backend.network.server.http.HttpServerModule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collection;
import java.util.Map;

@Slf4j
@ComponentScan
@Module(HttpServerModule.class)
public class WebsocketApiModule {

  public WebsocketApiModule(final WebsocketApiConfig config,
                            final HttpServerModule httpServerModule,
                            final ApplicationContext context) {
    final Collection<Object> collections = context.getBeansWithAnnotation(ApiEndpointCollection.class).values();
    final Map<String, ApiEndpointData> endpoints = ApiEndpointCollectionParser.getEndpoints(ApiEndpointCollectionParser.parseCollections(collections));
    httpServerModule.getRoutes().addRoute(config.getPath(), new WebsocketApiRoute(endpoints));
  }
}
