package net.getnova.backend.api;

import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.api.annotations.ApiEndpointCollection;
import net.getnova.backend.api.data.ApiEndpointCollectionData;
import net.getnova.backend.api.data.ApiType;
import net.getnova.backend.api.handler.rest.RestApiModule;
import net.getnova.backend.api.handler.websocket.WebsocketApiModule;
import net.getnova.backend.api.parser.ApiEndpointCollectionParser;
import net.getnova.backend.boot.context.ContextHandler;
import net.getnova.backend.boot.module.Module;
import org.springframework.context.annotation.ComponentScan;

import java.util.Set;
import java.util.stream.Collectors;

@ComponentScan
@RequiredArgsConstructor
@Module({RestApiModule.class, WebsocketApiModule.class})
public class ApiModule {

  private final ContextHandler context;
  private Set<ApiEndpointCollectionData> collections;

  @PostConstruct
  private void postConstruct() {
    this.collections = ApiEndpointCollectionParser.parseCollections(
      this.context.getBeansWithAnnotation(ApiEndpointCollection.class), this.context::getBean);
  }

  public Set<ApiEndpointCollectionData> getCollections(final ApiType type) {
    return this.collections.parallelStream()
      .filter(collection -> collection.getType().equals(type) || collection.getType().equals(ApiType.ALL))
      .collect(Collectors.toUnmodifiableSet());
  }
}
