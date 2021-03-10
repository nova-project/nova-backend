package net.getnova.framework.api.rest;

import io.netty.handler.codec.http.HttpMethod;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.api.ApiUtils;
import net.getnova.framework.api.data.AbstractApiRequest;
import net.getnova.framework.web.server.http.route.HttpRoute;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;

@RequiredArgsConstructor
@EqualsAndHashCode
public class RestApiRequest extends AbstractApiRequest {

  private final HttpRoute route;
  private final HttpServerRequest request;
  private final RestApiBodyReceiver body;

  @Override
  public HttpMethod getMethod() {
    return this.request.method();
  }

  @Override
  public String getPath() {
    return this.route.getPath(this.request);
  }

  @Override
  public <T> Mono<T> getData(final Class<T> clazz) {
    return this.body.receive(clazz).onErrorMap(ApiUtils::mapJsonError);
  }
}
