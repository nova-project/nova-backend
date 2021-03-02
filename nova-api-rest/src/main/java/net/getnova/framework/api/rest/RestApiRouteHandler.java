package net.getnova.framework.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.AsciiString;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.data.response.ApiDataResponse;
import net.getnova.framework.api.data.response.ApiMessageResponse;
import net.getnova.framework.api.data.response.ApiResponse;
import net.getnova.framework.api.executor.ApiExecutor;
import net.getnova.framework.web.server.http.route.HttpRoute;
import net.getnova.framework.web.server.http.route.HttpRouteHandler;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

@Slf4j
@RequiredArgsConstructor
public class RestApiRouteHandler implements HttpRouteHandler {

  private static final AsciiString CONTENT_TYPE = AsciiString.of(HttpHeaderValues.APPLICATION_JSON
    + "; " + HttpHeaderValues.CHARSET + "=" + StandardCharsets.UTF_8.name());
  private static final byte[] EMPTY_BODY = new byte[0];

  private final ApiExecutor executor;
  private final ObjectMapper objectMapper;

  @Override
  public Publisher<Void> handle(
    final HttpRoute route,
    final HttpServerRequest httpRequest,
    final HttpServerResponse httpResponse
  ) throws Exception {
    return httpResponse.sendString(
      this.execute(route, httpRequest)
        .map(apiResponse -> handleResponse(apiResponse, httpRequest, httpResponse))
    );
  }

  private Mono<ApiResponse> execute(final HttpRoute route, final HttpServerRequest request) {
    // TODO: use netty string decoding -> see RestApiRequest#getData(Class<?>)
    // JsonUtils
    return request.receive()
      .aggregate()
      .asByteArray()
      .defaultIfEmpty(EMPTY_BODY)
      .flatMap(content -> this.executor.execute(new RestApiRequest(this.objectMapper, route, request, content)));
  }

  private String handleResponse(
    final ApiResponse apiResponse,
    final HttpServerRequest httpRequest,
    final HttpServerResponse httpResponse
  ) {
    httpResponse.status(apiResponse.getStatus());
    httpResponse.header(HttpHeaderNames.CONTENT_TYPE, CONTENT_TYPE);

    try {
      return this.objectMapper.writeValueAsString(
        this.convertResponse(apiResponse)
      );
    }
    catch (JsonProcessingException e) {
      log.error("Unable to convert apiResponse to json. \"{} {}\"", httpRequest.method(), httpRequest.fullPath(), e);
      httpResponse.status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
      return "{}";
    }
  }

  private Object convertResponse(final ApiResponse response) {
    if (response instanceof ApiDataResponse) {
      return ((ApiDataResponse) response).getData();
    }
    else if (response instanceof ApiMessageResponse) {
      return Map.of("message", ((ApiMessageResponse) response).getMessage());
    }
    else {
      return Collections.emptyMap();
    }
  }
}
