package net.getnova.backend.api.handler.rest;

import net.getnova.backend.api.ApiAuthenticator;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;

public interface RestApiAuthenticator extends ApiAuthenticator {

  @Override
  default boolean isPermitted(ApiRequest request, int authentication, ApiEndpointData endpoint) {
    if (request instanceof RestApiRequest) {
      return this.isPermitted((RestApiRequest) request, authentication, endpoint);
    }

    throw new UnsupportedOperationException("not a rest api");
  }

  boolean isPermitted(RestApiRequest request, int authentication, ApiEndpointData endpoint);
}
