package net.getnova.framework.web.server.http.route;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.core.PathUtils;
import net.getnova.framework.web.server.http.HttpUtils;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

@Data
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.NONE)
public class HttpRoute {

  // 'null' means that this route accepts all methods
  private final HttpMethod method;
  private final String rootPrefix;
  private final String prefix;

  private final HttpRouteHandler handler;

  private HttpRoute(final HttpMethod method, final String pathPrefix, final HttpRouteHandler handler) {
    this.method = method;
    this.rootPrefix = PathUtils.normalizePath(pathPrefix);
    this.prefix = this.rootPrefix + '/';
    this.handler = handler;
  }

  public static HttpRoute of(final String pathPrefix, final HttpRouteHandler handler) {
    return of(null, pathPrefix, handler);
  }

  public static HttpRoute of(final HttpMethod method, final String pathPrefix, final HttpRouteHandler handler) {
    return new HttpRoute(method, pathPrefix, handler);
  }

  Publisher<Void> handle(final HttpServerRequest request, final HttpServerResponse response) {
    try {
      return Flux.from(this.handler.handle(this, request, response))
        .onErrorResume(cause -> this.handleError(cause, request, response));
    }
    catch (Exception e) {
      return this.handleError(e, request, response);
    }
  }

  public String getPath(final HttpServerRequest request) {
    return PathUtils.stripSegments(this.rootPrefix, request.fullPath());
  }

  private Publisher<Void> handleError(
    final Throwable cause,
    final HttpServerRequest request,
    final HttpServerResponse response
  ) {
    // Request ID??
    log.error("Unable to handle http route \"{} {}\".", request.method(), request.fullPath(), cause);

    if (!response.hasSentHeaders()) {
      return HttpUtils.status(response, HttpResponseStatus.INTERNAL_SERVER_ERROR);
    }

    return response.sendString(Mono.just(HttpResponseStatus.INTERNAL_SERVER_ERROR.toString()));
  }
}
