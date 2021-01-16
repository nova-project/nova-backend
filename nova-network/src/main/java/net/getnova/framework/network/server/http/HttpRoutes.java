package net.getnova.framework.network.server.http;

import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.network.server.http.route.HttpRoute;
import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public final class HttpRoutes {

  private final NavigableMap<String, HttpRoute> routes;

  public HttpRoutes() {
    this.routes = new TreeMap<>();
  }

  public void addRoute(final String path, final HttpRoute route) {
    this.routes.put(path.endsWith("/") ? path : path + '/', route);
  }

  Handler toHandler() {
    return new Handler(this.routes.entrySet());
  }

  @Slf4j
  @RequiredArgsConstructor
  private static final class Handler implements BiFunction<HttpServerRequest, HttpServerResponse, Publisher<Void>> {

    private final Set<Map.Entry<String, HttpRoute>> routes;

    @Override
    public Publisher<Void> apply(final HttpServerRequest request, final HttpServerResponse response) {
      final String path = request.path().toLowerCase() + "/";

      return this.routes.stream()
        .filter(entry -> path.startsWith(entry.getKey()))
        .findFirst()
        .map(entry -> this.executeRoute(
          !entry.getKey().equals("/")
            ? new CustomPathHttpServerRequest(request.fullPath().substring(entry.getKey().length()), request)
            : request, response, entry.getValue()
        ))
        .orElseGet(() -> HttpUtils.sendStatus(response, HttpResponseStatus.NOT_FOUND));
    }

    private Publisher<Void> executeRoute(final HttpServerRequest request, final HttpServerResponse response,
      final HttpRoute route) {
      try {
        return route.execute(request, response);
      }
      catch (Throwable cause) {
        log.error("Error while handling a http request.", cause);
        return HttpUtils.sendStatus(response, HttpResponseStatus.INTERNAL_SERVER_ERROR);
      }
    }
  }
}
