package net.getnova.framework.api.ws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.json.JsonObjectDecoder;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.ApiException;
import net.getnova.framework.api.ApiUtils;
import net.getnova.framework.api.data.response.ApiResponse;
import net.getnova.framework.api.data.response.DefaultApiResponse;
import net.getnova.framework.api.executor.ApiExecutor;
import net.getnova.framework.api.ws.WebsocketApiResponse.DefaultResponse;
import net.getnova.framework.api.ws.WebsocketApiResponse.ErrorResponse;
import net.getnova.framework.web.server.http.route.WebsocketRouteHandler;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;

@Slf4j
@RequiredArgsConstructor
public class WebsocketApiRouteHandler implements WebsocketRouteHandler {

  private static final Charset CHARSET = StandardCharsets.UTF_8;
  private static final JsonNodeFactory NODE_FACTORY = new JsonNodeFactory(false);
  private final ApiExecutor executor;
  private final ObjectMapper objectMapper;

  @Override
  public Publisher<Void> handle(final WebsocketInbound inbound, final WebsocketOutbound outbound) {
    return outbound.sendString(
      inbound.withConnection(c -> c.addHandler(new JsonObjectDecoder()))
        .receive()
        .asString(CHARSET)
        .flatMap(this::execute)
        .map(this::toString),
      CHARSET
    );
  }

  private Mono<WebsocketApiResponse> execute(final String data) {
    final WebsocketApiRequest request;

    try {
      request = this.objectMapper.readValue(data, WebsocketApiRequest.class);
    }
    catch (IOException e) {
      final Throwable cause = ApiUtils.mapJsonError(e);
      final boolean apiException = cause instanceof ApiException;

      if (!apiException || cause.getCause() == null) {
        log.error("Unable to parse websocket api request.", e);
      }

      if (apiException) {
        return Mono.just(new ErrorResponse(((ApiException) cause).getResponse()));
      }

      return Mono.just(
        new DefaultResponse((DefaultApiResponse) ApiResponse.of(HttpResponseStatus.INTERNAL_SERVER_ERROR))
      );
    }

    request.setObjectMapper(this.objectMapper);
    return this.executor.execute(request).map(response -> WebsocketApiResponse.of(request, response));
  }

  public String toString(final WebsocketApiResponse response) {
    try {
      return this.objectMapper.writeValueAsString(response);
    }
    catch (JsonProcessingException e) {
      log.error("Unable to convert apiResponse to json.", e);
      return "{\"status\":{\"code\":500,\"\":\"Internal Server Error\"}}";
    }
  }
}
