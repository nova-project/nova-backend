package net.getnova.framework.api.handler.websocket;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.ApiModule;
import net.getnova.framework.api.data.ApiEndpointCollectionData;
import net.getnova.framework.api.data.ApiEndpointData;
import net.getnova.framework.api.data.ApiType;
import net.getnova.framework.api.parser.ApiEndpointCollectionParser;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@Getter
@ComponentScan
public class WebsocketApiModule {

  private final Set<WebsocketApiContext> contexts;

  public WebsocketApiModule(
    final ApiModule apiModule,
    final WebsocketApiConfig config
  ) {
    final Set<ApiEndpointCollectionData> collections = apiModule.getCollections(ApiType.WEBSOCKET);
    final Map<String, ApiEndpointData> endpoints = ApiEndpointCollectionParser.getEndpoints(collections);
    this.contexts = Collections.newSetFromMap(new ConcurrentHashMap<>());
//    if (!endpoints.isEmpty()) {
//      httpServerModule.getRoutes().addRoute(config.getPath(), new WebsocketApiRoute(endpoints, this.contexts));
//    }
  }
}
