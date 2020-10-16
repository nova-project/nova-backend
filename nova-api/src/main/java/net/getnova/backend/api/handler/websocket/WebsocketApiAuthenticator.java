package net.getnova.backend.api.handler.websocket;

import net.getnova.backend.api.ApiAuthenticator;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;

public interface WebsocketApiAuthenticator extends ApiAuthenticator {

  @Override
  default boolean isPermitted(ApiRequest request, int authentication, ApiEndpointData endpoint) {
    if (request instanceof WebsocketApiRequest) {
      return this.isPermitted((WebsocketApiRequest) request, authentication, endpoint);
    }

    throw new UnsupportedOperationException("not a websocket");
  }

  boolean isPermitted(WebsocketApiRequest request, int authentication, ApiEndpointData endpoint);
}
