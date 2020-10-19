package net.getnova.backend.api.handler.websocket;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.executor.ApiExecutor;
import net.getnova.backend.json.JsonBuilder;
import net.getnova.backend.json.JsonUtils;
import net.getnova.backend.network.server.http.route.WebsocketRoute;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public final class WebsocketApiRoute implements WebsocketRoute {

  private static final Charset CHARSET = StandardCharsets.UTF_8;
  private final Map<String, ApiEndpointData> endpoints;

  @Override
  public Publisher<Void> apply(final WebsocketInbound inbound, final WebsocketOutbound outbound) {
    return outbound.sendString(
      inbound.receive()
        .asString(CHARSET)
        .filter(content -> !(content.isEmpty() && content.isBlank()))
        .flatMap(content -> this.execute(inbound, outbound, content))
        .onErrorResume(this::handleError)
        .map(this::parseResponse),
      CHARSET
    );
  }

  private Mono<ApiResponse> execute(final WebsocketInbound inbound, final WebsocketOutbound outbound, final String content) {
    final JsonObject json;
    try {
      json = content.isEmpty() && content.isBlank() ? JsonUtils.EMPTY_OBJECT : JsonUtils.fromJson(JsonParser.parseString(content), JsonObject.class);
    } catch (JsonParseException e) {
      return Mono.just(new ApiResponse(HttpResponseStatus.BAD_REQUEST, "JSON_SYNTAX"));
    }

    final String tag = JsonUtils.fromJson(json.get("tag"), String.class);
    final JsonElement endpoint = json.get("endpoint");

    final Mono<ApiResponse> response;

    if (tag == null) {
      response = Mono.just(new ApiResponse(HttpResponseStatus.BAD_REQUEST, "MISSING_TAG"));
    } else if (endpoint == null || endpoint.isJsonNull()) {
      response = Mono.just(new ApiResponse(HttpResponseStatus.BAD_REQUEST, "MISSING_ENDPOINT"));
    } else {

      final ApiRequest request = new WebsocketApiRequest(
        tag,
        JsonUtils.fromJson(endpoint, String.class),
        JsonUtils.fromJson(json.get("data"), JsonObject.class) == null ? JsonUtils.EMPTY_OBJECT : JsonUtils.fromJson(json.get("data"), JsonObject.class),
        inbound,
        outbound
      );

      response = ApiExecutor.execute(this.endpoints, request);
    }

    return response.doOnNext(resp -> {
      if (tag != null) resp.setTag(tag);
    });
  }

  private String parseResponse(final ApiResponse response) {
    final JsonObject status = JsonBuilder.create("code", response.getStatus().code())
      .add("name", response.getStatus().reasonPhrase())
      .build();

    final JsonBuilder builder = JsonBuilder.create("status", status);

    if (response.getTag() != null) builder.add("tag", response.getTag());
    if (response.getMessage() != null) builder.add("message", response.getMessage());
    if (response.getJson() != null) builder.add("data", response.getJson());

    return builder.build().toString();
  }

  private Mono<ApiResponse> handleError(final Throwable cause) {
    log.error("Error while executing websocket api pipeline.", cause);
    return Mono.just(new ApiResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR));
  }
}
