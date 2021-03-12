package net.getnova.framework.api.rest;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.util.List;
import lombok.EqualsAndHashCode;
import net.getnova.framework.api.ApiUtils;
import net.getnova.framework.api.data.request.AbstractApiRequest;
import net.getnova.framework.web.server.http.route.HttpRoute;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;

@EqualsAndHashCode
public class RestApiRequest extends AbstractApiRequest {

  private final HttpRoute route;
  private final HttpServerRequest request;
  private final MultiValueMap<String, String> query;
  private final RestApiBodyReceiver body;

  public RestApiRequest(
    final HttpRoute route,
    final HttpServerRequest request,
    final RestApiBodyReceiver body
  ) {
    this.route = route;
    this.request = request;
    this.query = new LinkedMultiValueMap<>(new QueryStringDecoder(request.uri()).parameters());
    this.body = body;
  }

  @Override
  public HttpMethod getMethod() {
    return this.request.method();
  }

  @Override
  public String getPath() {
    return this.route.getPath(this.request);
  }

  @Override
  public MultiValueMap<String, String> getQueryVariables() {
    return this.query;
  }

  @Override
  public List<String> getQueryVariable(final String name) {
    return this.query.get(name);
  }

  @Override
  public <T> Mono<T> getData(final Class<T> clazz) {
    return this.body.receive(clazz).onErrorMap(ApiUtils::mapJsonError);
  }
}
