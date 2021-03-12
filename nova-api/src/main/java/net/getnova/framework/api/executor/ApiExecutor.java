package net.getnova.framework.api.executor;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import javax.validation.Validator;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.ApiException;
import net.getnova.framework.api.data.ApiEndpoint;
import net.getnova.framework.api.data.request.ApiRequest;
import net.getnova.framework.api.data.response.ApiResponse;
import reactor.core.publisher.Mono;

@Slf4j
@EqualsAndHashCode
@SuppressWarnings("PMD.BeanMembersShouldSerialize")
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
      return Mono.just(ApiResponse.of(HttpResponseStatus.NOT_FOUND, "ENDPOINT", "NOT_FOUND"));
    }

    return this.matchMethod(matchedEndpoints, request)
      .map(endpoint ->
        endpoint.execute(this.endpointExecutor, request)
          .onErrorResume(cause -> this.handleError(request, cause))
      )
      .orElseGet(() -> Mono.just(ApiResponse.of(HttpResponseStatus.METHOD_NOT_ALLOWED))); // todo: add allowed methods
  }

  private Set<MatchedEndpoint> matchPath(final ApiRequest request) {
    return this.endpoints.stream()
      .flatMap(endpoint ->
        endpoint.getPath().match(request.getPath().toLowerCase(Locale.ENGLISH))
          .map(matcher -> new MatchedEndpoint(endpoint, matcher))
          .stream())
      .collect(Collectors.toSet());
  }

  private Optional<MatchedEndpoint> matchMethod(final Set<MatchedEndpoint> endpoints, final ApiRequest request) {
    return endpoints.stream()
      .filter(endpoint -> endpoint.getMethod().equals(request.getMethod()))
      .findFirst();
  }

  private Mono<ApiResponse> handleError(final ApiRequest request, final Throwable cause) {
    final boolean apiException = cause instanceof ApiException;

    if (!apiException || cause.getCause() == null) {
      log.error("Unable to execute api request \"{} {}\".", request.getMethod(), request.getPath(), cause);
    }

    if (apiException) {
      return Mono.just(((ApiException) cause).getResponse());
    }

    return Mono.just(ApiResponse.of(HttpResponseStatus.INTERNAL_SERVER_ERROR));
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
