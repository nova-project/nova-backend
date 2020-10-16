package net.getnova.backend.api;

import javax.annotation.PostConstruct;
import net.getnova.backend.api.annotations.ApiEndpointCollection;
import net.getnova.backend.api.data.ApiEndpointCollectionData;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiType;
import net.getnova.backend.api.handler.rest.RestApiModule;
import net.getnova.backend.api.handler.websocket.WebsocketApiModule;
import net.getnova.backend.api.parser.ApiEndpointCollectionParser;
import net.getnova.backend.boot.context.ContextHandler;
import net.getnova.backend.boot.module.Module;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Set;
import java.util.stream.Collectors;

@ComponentScan
@Module({RestApiModule.class, WebsocketApiModule.class})
public class ApiModule {

  private final ContextHandler context;
  private Set<ApiEndpointCollectionData> collections;

  public ApiModule(final ContextHandler context) {
    this.context = context;
  }

  @PostConstruct
  private void postConstruct() {
    this.collections = ApiEndpointCollectionParser.parseCollections(this.context.getBeansWithAnnotation(ApiEndpointCollection.class), this.context::getBean);
  }

  @Bean
  DefaultApiAuthenticator defaultAuthenticator() {
    return new DefaultApiAuthenticator();
  }

  public Set<ApiEndpointCollectionData> getCollections(final ApiType type) {
    return this.collections.parallelStream()
      .filter(collection -> collection.getType().equals(type) || collection.getType().equals(ApiType.ALL))
      .collect(Collectors.toUnmodifiableSet());
  }

  public static final class DefaultApiAuthenticator implements ApiAuthenticator {
    @Override
    public boolean isPermitted(final ApiRequest request, final int authentication, final ApiEndpointData endpoint) {
      return true;
    }
  }
}
