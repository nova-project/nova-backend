package net.getnova.framework.api.executor;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.data.ApiEndpoint;
import net.getnova.framework.api.data.ApiRequest;
import net.getnova.framework.api.data.ApiResponse;
import net.getnova.framework.api.data.ToApiResponse;
import net.getnova.framework.api.exception.ValidationApiException;
import reactor.core.publisher.Mono;

@Slf4j
public class ApiExecutor {

  private final ApiEndpointExecutor endpointExecutor;
  private final Set<ApiEndpoint> endpoints;

  public ApiExecutor(final Set<ApiEndpoint> endpoints, final Validator validator) {
    this.endpointExecutor = new ApiEndpointExecutor(validator);
    this.endpoints = endpoints;
  }

  public Mono<ApiResponse> execute(final ApiRequest request) {
    final Set<MatchedEndpoint> matchedEndpoints = this.matchPath(request);

    if (matchedEndpoints.isEmpty()) {
      return Mono.just(ApiResponse.of(HttpResponseStatus.NOT_FOUND, "ENDPOINT"));
    }

    return this.matchMethod(matchedEndpoints, request)
      .map(endpoint ->
        endpoint.execute(this.endpointExecutor, request)
          .onErrorResume(cause -> {
            if (cause instanceof ToApiResponse) {
              return Mono.just(((ToApiResponse) cause).toApiResponse());
            }

            if (cause instanceof ValidationApiException) {
              final Set<? extends ConstraintViolation<?>> violations = ((ValidationApiException) cause).getViolations();

            }

            log.error("Unable to execute api request \"{} {}\".", request.getMethod(), request.getPath(), cause);
            return Mono.just(ApiResponse.of(HttpResponseStatus.INTERNAL_SERVER_ERROR));
          })
      )
      .orElseGet(() -> Mono.just(ApiResponse.of(HttpResponseStatus.METHOD_NOT_ALLOWED))); // TODO: add allowed methods
  }

  private Set<MatchedEndpoint> matchPath(final ApiRequest request) {
    return this.endpoints.stream()
      .flatMap(endpoint ->
        endpoint.getPath().match(request.getPath())
          .map(matcher -> new MatchedEndpoint(endpoint, matcher))
          .stream())
      .collect(Collectors.toSet());
  }

  private Optional<MatchedEndpoint> matchMethod(final Set<MatchedEndpoint> endpoints, final ApiRequest request) {
    return endpoints.stream()
      .filter(endpoint -> endpoint.getMethod().equals(request.getMethod()))
      .findFirst();
  }

  @EqualsAndHashCode
  @RequiredArgsConstructor
  private static class MatchedEndpoint {

    private final ApiEndpoint endpoint;
    private final Matcher matcher;

    public Mono<ApiResponse> execute(final ApiEndpointExecutor endpointExecutor, final ApiRequest request) {
      request.setPathVariables(this.endpoint.getPath().getVariables(this.matcher));
      return endpointExecutor.execute(this.endpoint, request);
    }

    public HttpMethod getMethod() {
      return this.endpoint.getMethod();
    }
  }
}
