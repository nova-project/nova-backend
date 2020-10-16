package net.getnova.backend.api;

import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;

public interface ApiAuthenticator {

  boolean isPermitted(ApiRequest request, int authentication, ApiEndpointData endpoint);
}
