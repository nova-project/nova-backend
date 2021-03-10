package net.getnova.framework.api.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.AsciiString;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.data.ApiResponse;
import net.getnova.framework.api.data.ApiResponse.DataResponse;
import net.getnova.framework.api.data.ApiResponse.FluxDataResponse;
import net.getnova.framework.api.data.ApiResponse.MessageResponse;
import net.getnova.framework.api.data.ApiResponse.TargetMessageResponse;
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

  private static final JsonNodeFactory NODE_FACTORY = new JsonNodeFactory(false);
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
        .flatMap(apiResponse -> handleResponse(apiResponse, httpRequest, httpResponse))
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

  private Mono<String> handleResponse(
    final ApiResponse apiResponse,
    final HttpServerRequest httpRequest,
    final HttpServerResponse httpResponse
  ) {
    httpResponse.status(apiResponse.getStatus());
    httpResponse.header(HttpHeaderNames.CONTENT_TYPE, CONTENT_TYPE);

    return this.serialize(apiResponse)
      .map(data -> {
        try {
          return this.objectMapper.writeValueAsString(data);
        }
        catch (JsonProcessingException e) {
          log
            .error("Unable to convert apiResponse to json. \"{} {}\"", httpRequest.method(), httpRequest.fullPath(), e);
          httpResponse.status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
          return "{}";
        }
      });
  }

  private Mono<JsonNode> serialize(final ApiResponse response) {
    if (response instanceof FluxDataResponse) {
      return ((FluxDataResponse) response).getData()
        .collectList()
        .map(this::dataToJson);
    }
    else if (response instanceof DataResponse) {
      return Mono.just(this.dataToJson(((DataResponse) response).getData()));
    }
    else if (response instanceof TargetMessageResponse) {
      return Mono.just(this.messageToJson(((TargetMessageResponse) response).getMessage())
        .set("target", NODE_FACTORY.textNode(((TargetMessageResponse) response).getTarget())));
    }
    else if (response instanceof MessageResponse) {
      return Mono.just(this.messageToJson(((MessageResponse) response).getMessage()));
    }
    return Mono.just(NODE_FACTORY.objectNode());
  }

  private JsonNode dataToJson(final Object data) {
    return this.objectMapper.valueToTree(data);
  }

  private ObjectNode messageToJson(final String message) {
    return NODE_FACTORY.objectNode()
      .set("message", NODE_FACTORY.textNode(message));
  }
}
