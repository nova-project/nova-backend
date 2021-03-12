package net.getnova.framework.api.executor;

import io.netty.handler.codec.http.HttpResponseStatus;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.data.ApiEndpoint;
import net.getnova.framework.api.data.ApiParameter;
import net.getnova.framework.api.data.request.ApiRequest;
import net.getnova.framework.api.data.response.ApiError;
import net.getnova.framework.api.data.response.ApiResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ApiEndpointExecutor {

  private final Validator validator;

  Mono<ApiResponse> execute(final ApiEndpoint endpoint, final ApiRequest request) {
    return Flux.fromIterable(endpoint.getParameters())
      .flatMap(parameter -> this.parseParameter(request, parameter))
      .map(arg -> {
        final Set<? extends ConstraintViolation<?>> violations = this.validator.validate(arg);
        if (violations.isEmpty()) {
          return arg;
        }

        return ApiResponse.of(HttpResponseStatus.BAD_REQUEST, ApiError.of(violations));
      })
      .collectList()
      .flatMap(args -> this.execute(endpoint, args.toArray()));
  }

  private Mono<?> parseParameter(final ApiRequest request, final ApiParameter<?> parameter) {
    final Object value = parameter.parse(request);

    return value instanceof Mono
      ? (Mono<?>) value
      : Mono.just(value);
  }

  private Mono<ApiResponse> execute(final ApiEndpoint endpoint, final Object[] args) {
    try {
      return endpoint.execute(args);
    }
    catch (InvocationTargetException e) {
      return this.handleError(endpoint, e.getTargetException());
    }
    catch (IllegalAccessException | IllegalArgumentException e) {
      return this.handleError(endpoint, e);
    }
  }

  private Mono<ApiResponse> handleError(final ApiEndpoint endpoint, final Throwable exception) {
    log.error("Unable to execute endpoint \"{} {}\".", endpoint.getMethod(), endpoint.getPath().getRaw(), exception);
    return Mono.just(ApiResponse.of(HttpResponseStatus.INTERNAL_SERVER_ERROR));
  }
}
