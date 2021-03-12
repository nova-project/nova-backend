package net.getnova.framework.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.AsciiString;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.data.response.ApiResponse;
import net.getnova.framework.api.data.response.DataApiResponse;
import net.getnova.framework.api.data.response.DefaultApiResponse;
import net.getnova.framework.api.data.response.ErrorApiResponse;
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
    return this.executor.execute(
      new RestApiRequest(route, request, new RestApiBodyReceiver(this.objectMapper, request))
    );
  }

  private String handleResponse(
    final ApiResponse apiResponse,
    final HttpServerRequest httpRequest,
    final HttpServerResponse httpResponse
  ) {
    httpResponse.status(apiResponse.getStatus());
    httpResponse.header(HttpHeaderNames.CONTENT_TYPE, CONTENT_TYPE);

    try {
      final Object convert = this.convert(apiResponse);
      if (convert == null) {
        return "{}";
      }

      return this.objectMapper.writeValueAsString(convert);
    }
    catch (JsonProcessingException e) {
      log.error("Unable to convert apiResponse to json. \"{} {}\"",
        httpRequest.method(), httpRequest.fullPath(), e);
      httpResponse.status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
      return "{}";
    }
  }

  private Object convert(final ApiResponse response) {
    if (response instanceof DataApiResponse) {
      return ((DataApiResponse) response).getData();
    }

    if (response instanceof ErrorApiResponse) {
      return new ErrorRestApiResponse(((ErrorApiResponse) response).getErrors());
    }

    if (response instanceof DefaultApiResponse) {
      return null;
    }

    throw new IllegalArgumentException("unknown response type: " + response.getClass());
  }
}
