package net.getnova.framework.api.handler.playground;

import java.util.Collection;
import java.util.TreeSet;
import net.getnova.framework.api.data.ApiEndpointCollectionData;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class PlaygroundModule {

  private final TreeSet<ApiEndpointCollectionData> collections;

  public PlaygroundModule(final PlaygroundConfig config) {
    this.collections = new TreeSet<>();

//    httpServerModule.getRoutes().addRoute("playground", new FileHttpRoute(new File("www")));

//    httpServerModule.getServer().getRoutes()
//      .get(config.getPath(), (request, response) -> response.sendFile(Path.of("www/playground/index.html")))
//      .directory( config.getPath(), Path.of("www/playground"));
  }

  public void addCollections(final Collection<ApiEndpointCollectionData> collections) {
    this.collections.addAll(collections);
  }
}
