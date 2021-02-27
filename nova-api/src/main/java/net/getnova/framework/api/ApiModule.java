package net.getnova.framework.api;

import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.api.data.ApiEndpointCollectionData;
import net.getnova.framework.api.data.ApiType;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@RequiredArgsConstructor
//@Module({RestApiModule.class, WebsocketApiModule.class})
public class ApiModule {

  //  private final ContextHandler context;
  private Set<ApiEndpointCollectionData> collections;

  @PostConstruct
  private void postConstruct() {
//    this.collections = ApiEndpointCollectionParser.parseCollections(
//      this.context.getBeansWithAnnotation(ApiEndpointCollection.class), this.context::getBean);
  }

  public Set<ApiEndpointCollectionData> getCollections(final ApiType type) {
    return this.collections.parallelStream()
      .filter(collection -> collection.getType().equals(type) || collection.getType().equals(ApiType.ALL))
      .collect(Collectors.toUnmodifiableSet());
  }
}
