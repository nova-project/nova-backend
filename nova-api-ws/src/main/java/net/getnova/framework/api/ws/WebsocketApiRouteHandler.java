package net.getnova.framework.api.ws;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.json.JsonObjectDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.data.response.ApiDataResponse;
import net.getnova.framework.api.data.response.ApiMessageResponse;
import net.getnova.framework.api.data.response.ApiResponse;
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

  private final ApiExecutor executor;
  private final ObjectMapper objectMapper;

  @Override
  public Publisher<Void> handle(final WebsocketInbound inbound, final WebsocketOutbound outbound) {
    return outbound.sendString(
      inbound.withConnection(c -> c.addHandler(new JsonObjectDecoder()))
        .receive()
        .asInputStream()
        .flatMap(this::execute)
        .map(this::serializeResponse),
      StandardCharsets.UTF_8
    );
  }

  private Mono<ApiResponse> execute(final InputStream inputStream) {
    final WebsocketApiRequest request;

    try (inputStream) {
      request = this.objectMapper.readValue(inputStream, WebsocketApiRequest.class);
      request.setObjectMapper(this.objectMapper);
    }
    catch (JsonParseException e) {
      return Mono.just(ApiResponse.of(HttpResponseStatus.BAD_REQUEST, "SYNTAX", "JSON"));
    }
    catch (IOException e) { // JsonMappingException
      log.error("", e);
      return Mono.just(ApiResponse.of(HttpResponseStatus.INTERNAL_SERVER_ERROR));
    }

    return this.executor.execute(request);
  }

  private String serializeResponse(final ApiResponse response) {
    try {
      return this.objectMapper.writeValueAsString(
        this.convertResponse(response)
      );
    }
    catch (JsonProcessingException e) {
//      log.error("Unable to convert apiResponse to json. \"{} {}\"", request.getMethod(), request.getPath(), e);
      throw new RuntimeApiException(e);
    }
  }

  private Object convertResponse(final ApiResponse response) {
    final Map<String, Object> object = new HashMap<>(2);
    object.put("status", Map.of(
      "code", response.getStatus().code(),
      "name", response.getStatus().reasonPhrase()
    ));

    if (response instanceof ApiDataResponse) {
      object.put("data", ((ApiDataResponse) response).getData());
    }
    else if (response instanceof ApiMessageResponse) {
      object.put("message", ((ApiMessageResponse) response).getMessage());
    }

    return object;
  }
}
