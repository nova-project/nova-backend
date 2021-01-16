package net.getnova.framework.api.handler.websocket;

import io.netty.handler.codec.http.HttpResponseStatus;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.api.data.ApiEndpointData;
import net.getnova.framework.api.data.ApiResponse;
import net.getnova.framework.network.server.http.route.WebsocketRoute;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;

@Slf4j
@RequiredArgsConstructor
public final class WebsocketApiRoute implements WebsocketRoute {

  private static final Charset CHARSET = StandardCharsets.UTF_8;
  private final Map<String, ApiEndpointData> endpoints;
  private final Set<WebsocketApiContext> contexts;

  @Override
  public Publisher<Void> apply(final WebsocketInbound inbound, final WebsocketOutbound outbound) {
    final WebsocketApiContext context = new WebsocketApiContext(inbound, outbound);
    this.contexts.add(context);

//    return outbound.sendString(
    inbound.receiveObject()
      .doOnNext(d -> {
        System.out.println(d.getClass());
      })
//        .flatMap(content -> this.execute(context, content))
//        .onErrorResume(this::handleError)
//        .map(this::parseResponse)
//        .doFinally(signal -> this.contexts.remove(context)),
//      CHARSET
    /*)*/;

    return Mono.empty();
  }
//
//  private Mono<ApiResponse> execute(final WebsocketApiContext context, final WebSocketFrame frame) {
//
//    JsonUtils.read(frame.content().in)
//
//    final JsonObject json;
//    try {
//      json = content.isEmpty() && content.isBlank() ? JsonUtilsold.EMPTY_OBJECT : JsonUtilsold
//        .fromJson(JsonParser.parseString(content), JsonObject.class);
//    }
//    catch (JsonParseException e) {
//      return Mono.just(new ApiResponse(HttpResponseStatus.BAD_REQUEST, "JSON_SYNTAX"));
//    }
//
//    final String tag = JsonUtilsold.fromJson(json.get("tag"), String.class);
//    final JsonElement endpoint = json.get("endpoint");
//
//    final Mono<ApiResponse> response;
//
//    if (tag == null) {
//      response = Mono.just(new ApiResponse(HttpResponseStatus.BAD_REQUEST, "MISSING_TAG"));
//    }
//    else if (endpoint == null || endpoint.isJsonNull()) {
//      response = Mono.just(new ApiResponse(HttpResponseStatus.BAD_REQUEST, "MISSING_ENDPOINT"));
//    }
//    else {
//      response = ApiExecutor.execute(
//        this.endpoints,
//        new WebsocketApiRequest(
//          tag,
//          JsonUtilsold.fromJson(endpoint, String.class),
//          Optional.ofNullable(JsonUtilsold.fromJson(json.get("data"), JsonObject.class))
//            .orElse(JsonUtilsold.EMPTY_OBJECT),
//          context
//        )
//      );
//    }
//
//    return response.doOnNext(resp -> {
//      if (tag != null) {
//        resp.setRequestId(tag);
//      }
//    });
//  }
//
//  private String parseResponse(final ApiResponse response) {
//    final JsonObject status = JsonBuilder.create("code", response.getStatus().code())
//      .add("name", response.getStatus().reasonPhrase())
//      .build();
//
//    final JsonBuilder builder = JsonBuilder.create("status", status);
//
//    if (response.getRequestId() != null) {
//      builder.add("tag", response.getRequestId());
//    }
//    if (response.getMessage() != null) {
//      builder.add("message", response.getMessage());
//    }
//    if (response.getJson() != null) {
//      builder.add("data", response.getJson());
//    }
//
//    return builder.build().toString();
//  }

  private Mono<ApiResponse> handleError(final Throwable cause) {
    log.error("Error while executing websocket api pipeline.", cause);
    return Mono.just(new ApiResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR));
  }
}
