package net.getnova.framework.api.executor;

import io.netty.handler.codec.http.HttpResponseStatus;
import java.lang.reflect.InvocationTargetException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.data.ApiEndpoint;
import net.getnova.framework.api.data.ApiParameter;
import net.getnova.framework.api.data.ApiRequest;
import net.getnova.framework.api.data.ApiResponse;
import net.getnova.framework.api.exception.ParameterApiException;
import reactor.core.publisher.Mono;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ApiEndpointExecutor {

  static final ApiEndpointExecutor INSTANCE = new ApiEndpointExecutor();

  Mono<ApiResponse> execute(final ApiEndpoint endpoint, final ApiRequest request) {
    try {
      final Object[] args = new Object[endpoint.getParameters().size()];

      int i = 0;
      for (final ApiParameter<?> parameter : endpoint.getParameters()) {
        try {
          args[i] = parameter.parse(request);
        }
        catch (ParameterApiException e) {
          return Mono.just(e.toApiResponse());
        }
      }

      return endpoint.execute(args);
    }
    catch (InvocationTargetException e) {
      log.error("Unable to execute endpoint \"{} {}\".",
        endpoint.getMethod(), endpoint.getPath().getRaw(), e.getTargetException());
    }
    catch (IllegalAccessException | IllegalArgumentException e) {
      log.error("Unable to execute endpoint \"{} {}\".",
        endpoint.getMethod(), endpoint.getPath().getRaw(), e);
    }

    return Mono.just(ApiResponse.of(HttpResponseStatus.INTERNAL_SERVER_ERROR));
  }
}
