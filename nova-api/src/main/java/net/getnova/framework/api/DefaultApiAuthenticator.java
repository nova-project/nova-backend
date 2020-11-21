package net.getnova.framework.api;

import net.getnova.framework.api.data.ApiEndpointData;
import net.getnova.framework.api.data.ApiRequest;
import org.springframework.stereotype.Component;

@Component
public class DefaultApiAuthenticator implements ApiAuthenticator {

  @Override
  public boolean isPermitted(final ApiRequest request, final int authentication, final ApiEndpointData endpoint) {
    return true;
  }
}
