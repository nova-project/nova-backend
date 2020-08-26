package net.getnova.backend.api.handler.websocket;

import lombok.Getter;
import net.getnova.backend.api.annotations.ApiEndpointCollection;
import net.getnova.backend.api.data.ApiEndpointCollectionData;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.parser.ApiEndpointCollectionParser;
import net.getnova.backend.boot.module.Module;
import net.getnova.backend.codec.http.server.HttpServerModule;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;

@ComponentScan
@Module(HttpServerModule.class)
public class WebsocketApiService {

  private final WebsocketApiConfig config;
  private final HttpServerModule httpServerModule;
  private final ApplicationContext context;
  @Getter
  private Set<ApiEndpointCollectionData> collectionsData;

  public WebsocketApiService(final WebsocketApiConfig config,
                             final HttpServerModule httpServerModule,
                             final ApplicationContext context) {
    this.config = config;
    this.httpServerModule = httpServerModule;
    this.context = context;
  }

  @PostConstruct
  private void postInit() {
    this.collectionsData = ApiEndpointCollectionParser.parseCollections(this.context.getBeansWithAnnotation(ApiEndpointCollection.class).values());
    final Map<String, ApiEndpointData> endpoints = ApiEndpointCollectionParser.getEndpoints(this.collectionsData);
    this.httpServerModule.addLocationProvider(this.config.getPath(), new WebsocketApiLocationProvider(endpoints));
  }
}
