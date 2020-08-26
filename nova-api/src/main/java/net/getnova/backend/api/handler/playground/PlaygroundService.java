package net.getnova.backend.api.handler.playground;

import net.getnova.backend.api.data.ApiEndpointCollectionData;
import net.getnova.backend.boot.module.Module;
import net.getnova.backend.codec.http.server.HttpServerModule;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.TreeSet;

@ComponentScan
@Module(HttpServerModule.class)
public class PlaygroundService {

  private final PlaygroundConfig config;
  private final HttpServerModule httpServerModule;
  private final TreeSet<ApiEndpointCollectionData> collections;

  public PlaygroundService(final PlaygroundConfig config, final HttpServerModule httpServerModule) {
    this.config = config;
    this.httpServerModule = httpServerModule;
    this.collections = new TreeSet<>();
  }

  @PostConstruct
  private void postConstruct() {
    if (this.config.isEnabled()) {
      this.httpServerModule.addLocationProvider(this.config.getPath(), new PlaygroundLocationProvider(this.collections));
    }
  }

  public void addCollections(final Collection<ApiEndpointCollectionData> collections) {
    this.collections.addAll(collections);
  }
}
