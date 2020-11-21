package net.getnova.framework.api.handler.rest;

import net.getnova.framework.api.ApiModule;
import net.getnova.framework.api.data.ApiEndpointCollectionData;
import net.getnova.framework.api.data.ApiEndpointData;
import net.getnova.framework.api.data.ApiType;
import net.getnova.framework.api.parser.ApiEndpointCollectionParser;
import net.getnova.framework.boot.module.Module;
import net.getnova.framework.network.server.http.HttpServerModule;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;
import java.util.Set;

@ComponentScan
@Module({HttpServerModule.class})
public class RestApiModule {

  public RestApiModule(
    final ApiModule apiModule,
    final RestApiConfig config,
    final HttpServerModule httpServerModule
  ) {
    final Set<ApiEndpointCollectionData> collections = apiModule.getCollections(ApiType.REST);
    final Map<String, ApiEndpointData> endpoints = ApiEndpointCollectionParser.getEndpoints(collections);
    if (!endpoints.isEmpty()) {
      httpServerModule.getRoutes().addRoute(config.getPath(), new RestApiRoute(endpoints));
    }
  }
}
