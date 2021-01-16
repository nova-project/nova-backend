package net.getnova.framework.api.executor;

import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.Map;
import java.util.Optional;
import net.getnova.framework.api.data.ApiEndpointData;
import net.getnova.framework.api.data.ApiRequest;
import net.getnova.framework.api.data.ApiResponse;
import reactor.core.publisher.Mono;

public final class ApiExecutor {

  private ApiExecutor() {
    throw new UnsupportedOperationException();
  }

  public static Mono<ApiResponse> execute(final Map<String, ApiEndpointData> endpoints, final ApiRequest request) {
    return Optional.ofNullable(endpoints.get(request.getEndpoint()))
      .map(endpoint -> ApiEndpointExecutor.execute(request, endpoint))
      .orElse(Mono.just(new ApiResponse(HttpResponseStatus.NOT_FOUND, "ENDPOINT")));
  }
}
