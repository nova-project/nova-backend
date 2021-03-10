package net.getnova.framework.api.ws;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.json.JsonObjectDecoder;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.data.ApiResponse;
import net.getnova.framework.api.data.ApiResponse.DataResponse;
import net.getnova.framework.api.data.ApiResponse.FluxDataResponse;
import net.getnova.framework.api.data.ApiResponse.MessageResponse;
import net.getnova.framework.api.data.ApiResponse.TargetMessageResponse;
import net.getnova.framework.api.exception.RuntimeApiException;
import net.getnova.framework.api.executor.ApiExecutor;
import net.getnova.framework.web.server.http.route.WebsocketRouteHandler;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;

@Slf4j
@RequiredArgsConstructor
public class WebsocketApiRouteHandler implements WebsocketRouteHandler {

  private static final JsonNodeFactory NODE_FACTORY = new JsonNodeFactory(false);
  private final ApiExecutor executor;
  private final ObjectMapper objectMapper;

  @Override
  public Publisher<Void> handle(final WebsocketInbound inbound, final WebsocketOutbound outbound) {
    return outbound.sendString(
      inbound.withConnection(c -> c.addHandler(new JsonObjectDecoder()))
        .receive()
        .asString(StandardCharsets.UTF_8)
        .flatMap(this::execute)
        .flatMap(this::serialize)
        .map(this::jsonToString),
      StandardCharsets.UTF_8
    );
  }

  private Mono<ApiResponse> execute(final String data) {
    final WebsocketApiRequest request;

    try {
      request = this.objectMapper.readValue(data, WebsocketApiRequest.class);
    }
    catch (JsonParseException e) {
      return Mono.just(ApiResponse.of(HttpResponseStatus.BAD_REQUEST, "JSON", "SYNTAX"));
    }
    catch (JsonMappingException e) {
      return Mono.just(ApiResponse.of(HttpResponseStatus.BAD_REQUEST, "JSON", "UNEXPECTED_CONTENT"));
    }
    catch (JsonProcessingException e) {
      return Mono.just(ApiResponse.of(HttpResponseStatus.INTERNAL_SERVER_ERROR));
    }

    request.setObjectMapper(this.objectMapper);
    return this.executor.execute(request);
  }

  private String jsonToString(final JsonNode json) {
    try {
      return this.objectMapper.writeValueAsString(json);
    }
    catch (JsonProcessingException e) {
//      log.error("Unable to convert apiResponse to json. \"{} {}\"", request.getMethod(), request.getPath(), e);
      throw new RuntimeApiException(e);
    }
  }

  private Mono<ObjectNode> serialize(final ApiResponse response) {
    if (response instanceof FluxDataResponse) {
      return ((FluxDataResponse) response).getData()
        .collectList()
        .map(data -> this.jsonToString(response, data));
    }
    else if (response instanceof DataResponse) {
      return Mono.just(this.jsonToString(response, ((DataResponse) response).getData()));
    }
    else if (response instanceof TargetMessageResponse) {
      return Mono.just(
        this.serializeHeader(response)
          .<ObjectNode>set("message", NODE_FACTORY.textNode(((TargetMessageResponse) response).getMessage()))
          .set("target", NODE_FACTORY.textNode(((TargetMessageResponse) response).getTarget()))
      );
    }
    else if (response instanceof MessageResponse) {
      return Mono.just(this.serializeHeader(response)
        .set("message", NODE_FACTORY.textNode(((MessageResponse) response).getMessage())));
    }
    else {
      return Mono.just(this.serializeHeader(response));
    }
  }

  private ObjectNode jsonToString(final ApiResponse response, final Object data) {
    return this.serializeHeader(response)
      .set("data", this.objectMapper.valueToTree(data));
  }

  private ObjectNode serializeHeader(final ApiResponse response) {
    final ObjectNode node = NODE_FACTORY.objectNode();

    final ObjectNode status = NODE_FACTORY.objectNode();
    status.set("code", NODE_FACTORY.numberNode(response.getStatus().code()));
    status.set("name", NODE_FACTORY.textNode(response.getStatus().reasonPhrase()));
    node.set("status", status);

    return node;
  }
}
