package net.getnova.framework.api.handler.websocket;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.ApiModule;
import net.getnova.framework.api.data.ApiEndpointCollectionData;
import net.getnova.framework.api.data.ApiEndpointData;
import net.getnova.framework.api.data.ApiType;
import net.getnova.framework.api.parser.ApiEndpointCollectionParser;
import net.getnova.framework.boot.module.Module;
import net.getnova.framework.network.server.http.HttpServerModule;
import org.springframework.context.annotation.ComponentScan;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Getter
@ComponentScan
@Module({HttpServerModule.class})
public class WebsocketApiModule {

  private final Set<WebsocketApiContext> contexts;

  public WebsocketApiModule(
    final ApiModule apiModule,
    final WebsocketApiConfig config,
    final HttpServerModule httpServerModule
  ) {
    final Set<ApiEndpointCollectionData> collections = apiModule.getCollections(ApiType.WEBSOCKET);
    final Map<String, ApiEndpointData> endpoints = ApiEndpointCollectionParser.getEndpoints(collections);
    this.contexts = Collections.newSetFromMap(new ConcurrentHashMap<>());
    if (!endpoints.isEmpty()) {
      httpServerModule.getRoutes().addRoute(config.getPath(), new WebsocketApiRoute(endpoints, this.contexts));
    }
  }
}
