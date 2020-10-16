package net.getnova.backend.api.executor;

import io.netty.handler.codec.http.HttpResponseStatus;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

public final class ApiExecutor {

  private ApiExecutor() {
    throw new UnsupportedOperationException();
  }

  public static Mono<ApiResponse> execute(final Map<String, ApiEndpointData> endpoints, final ApiRequest request) {
    final ApiEndpointData endpoint = endpoints.get(request.getEndpoint());
    return endpoint == null
      ? Mono.just(new ApiResponse(HttpResponseStatus.NOT_FOUND, "ENDPOINT"))
      : ApiEndpointExecutor.execute(request, endpoint);
  }
}
