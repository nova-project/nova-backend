package net.getnova.backend.api.handler.rest;

import lombok.Data;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.codec.http.server.HttpLocationProvider;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@Data
public final class RestApiLocationProvider implements HttpLocationProvider<RestApiLocation> {

  private final Map<String, ApiEndpointData> endpoints;

  @NotNull
  @Override
  public RestApiLocation getLocation() {
    return new RestApiLocation(this.endpoints);
  }
}
