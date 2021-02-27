package net.getnova.framework.api.handler.rest;

import java.util.Map;
import java.util.Set;
import net.getnova.framework.api.ApiModule;
import net.getnova.framework.api.data.ApiEndpointCollectionData;
import net.getnova.framework.api.data.ApiEndpointData;
import net.getnova.framework.api.data.ApiType;
import net.getnova.framework.api.parser.ApiEndpointCollectionParser;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
//@Module({HttpServerUtils.class})
public class RestApiModule {

  public RestApiModule(
    final ApiModule apiModule,
    final RestApiConfig config
//    final HttpServerUtils httpServerModule
  ) {
    final Set<ApiEndpointCollectionData> collections = apiModule.getCollections(ApiType.REST);
    final Map<String, ApiEndpointData> endpoints = ApiEndpointCollectionParser.getEndpoints(collections);
//    if (!endpoints.isEmpty()) {
//      httpServerModule.getRoutes().addRoute(config.getPath(), new RestApiRoute(endpoints));
//    }
  }
}
