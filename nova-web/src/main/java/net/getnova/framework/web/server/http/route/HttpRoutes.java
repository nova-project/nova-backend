package net.getnova.framework.web.server.http.route;

import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.web.server.http.HttpUtils;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpRoutes {

  private final Set<HttpRoute> routes;

  public static HttpRoutes of(final Set<HttpRoute> routes) {
    return new HttpRoutes(routes);
  }

  public Publisher<Void> handle(final HttpServerRequest request, final HttpServerResponse response) {
    final Set<HttpRoute> pathRoutes = this.matchPath(request);

    if (pathRoutes.isEmpty()) {
      return HttpUtils.status(response, HttpResponseStatus.NOT_FOUND);
    }

    return this.matchMethod(pathRoutes, request)
      .map(route -> route.handle(request, response))
      .orElseGet(() -> HttpUtils.methodNotAllowed(
        response,
        pathRoutes.stream()
          .map(HttpRoute::getMethod)
          .collect(Collectors.toSet())
      ));
  }

  private Set<HttpRoute> matchPath(final HttpServerRequest request) {
    return this.routes.stream()
      .filter(route -> this.matchPath(
        request.fullPath().toLowerCase(Locale.ENGLISH),
        route.getRootPrefix(),
        route.getPrefix()
      ))
      .collect(Collectors.toSet());
  }

  private boolean matchPath(final String path, final String rootPrefix, final String prefix) {
    return path.startsWith(prefix) || path.equals(rootPrefix);
  }

  private Optional<HttpRoute> matchMethod(final Set<HttpRoute> routes, final HttpServerRequest request) {
    return routes.stream()    // 'null' means that this route accepts all methods
      .filter(route -> route.getMethod() == null || route.getMethod().equals(request.method()))
      .findFirst();
  }
}
