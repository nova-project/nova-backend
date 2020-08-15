package net.getnova.backend.api.handler.playground;

import net.getnova.backend.api.data.ApiEndpointCollectionData;
import net.getnova.backend.codec.http.server.HttpServerService;
import net.getnova.backend.config.ConfigService;
import net.getnova.backend.service.Service;
import net.getnova.backend.service.event.StartService;
import net.getnova.backend.service.event.StartServiceEvent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.TreeSet;

@Service(id = "playground", depends = {ConfigService.class, HttpServerService.class})
@Singleton
public final class PlaygroundService {

  private final PlaygroundConfig config;
  private final TreeSet<ApiEndpointCollectionData> collections;

  @Inject
  private HttpServerService httpServerService;

  @Inject
  public PlaygroundService(final ConfigService configService) {
    this.config = configService.addConfig("playground", new PlaygroundConfig());
    this.collections = new TreeSet<>();
  }

  @StartService
  private void start(final StartServiceEvent event) {
    if (this.config.isEnabled()) {
      this.httpServerService.addLocationProvider(this.config.getPath(), new PlaygroundLocationProvider(this.collections));
    }
  }

  public void addCollections(final Collection<ApiEndpointCollectionData> collections) {
    this.collections.addAll(collections);
  }
}
