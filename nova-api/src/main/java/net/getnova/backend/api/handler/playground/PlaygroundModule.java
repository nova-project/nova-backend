package net.getnova.backend.api.handler.playground;

import net.getnova.backend.api.data.ApiEndpointCollectionData;
import net.getnova.backend.boot.module.Module;
import net.getnova.backend.network.server.http.HttpServerModule;
import net.getnova.backend.network.server.http.route.FileHttpRoute;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.util.Collection;
import java.util.TreeSet;

@ComponentScan
@Module(HttpServerModule.class)
public class PlaygroundModule {

  private final TreeSet<ApiEndpointCollectionData> collections;

  public PlaygroundModule(final PlaygroundConfig config, final HttpServerModule httpServerModule) {
    this.collections = new TreeSet<>();

    httpServerModule.getRoutes().addRoute("playground", new FileHttpRoute(new File("www")));

//    httpServerModule.getServer().getRoutes()
//      .get(config.getPath(), (request, response) -> response.sendFile(Path.of("www/playground/index.html")))
//      .directory( config.getPath(), Path.of("www/playground"));
  }

  public void addCollections(final Collection<ApiEndpointCollectionData> collections) {
    this.collections.addAll(collections);
  }
}
