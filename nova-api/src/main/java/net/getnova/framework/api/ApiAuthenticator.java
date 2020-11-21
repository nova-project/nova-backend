package net.getnova.framework.api;

import net.getnova.framework.api.data.ApiEndpointData;
import net.getnova.framework.api.data.ApiRequest;

public interface ApiAuthenticator {

  boolean isPermitted(ApiRequest request, int authentication, ApiEndpointData endpoint);
}
