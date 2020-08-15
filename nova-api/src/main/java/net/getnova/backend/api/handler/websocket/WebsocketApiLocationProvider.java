package net.getnova.backend.api.handler.websocket;

import lombok.Data;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.codec.http.server.HttpLocationProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Data
public final class WebsocketApiLocationProvider implements HttpLocationProvider<WebsocketApiLocation> {

  private final Map<String, ApiEndpointData> endpoints;

  @NotNull
  @Override
  public WebsocketApiLocation getLocation() {
    return new WebsocketApiLocation(this.endpoints);
  }
}
