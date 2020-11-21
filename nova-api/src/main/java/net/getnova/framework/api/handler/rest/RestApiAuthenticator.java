package net.getnova.framework.api.handler.rest;

import net.getnova.framework.api.ApiAuthenticator;
import net.getnova.framework.api.data.ApiEndpointData;
import net.getnova.framework.api.data.ApiRequest;

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
