package net.getnova.backend.api.handler.rest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.AsciiString;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.executor.ApiExecutor;
import net.getnova.backend.json.JsonBuilder;
import net.getnova.backend.json.JsonUtils;
import net.getnova.backend.network.server.http.route.HttpRoute;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
final class RestApiRoute implements HttpRoute {

  private static final Charset CHARSET = StandardCharsets.UTF_8;
  private static final AsciiString CONTENT_TYPE = AsciiString.of(HttpHeaderValues.APPLICATION_JSON + "; " + HttpHeaderValues.CHARSET + "=" + CHARSET.toString());
  private static final String EMPTY_RESPONSE = "{}";
  private final Map<String, ApiEndpointData> endpoints;

  @Override
  public Publisher<Void> execute(final HttpServerRequest httpRequest, final HttpServerResponse httpResponse) {
    return httpResponse.sendString(
      httpRequest.receive()
        .aggregate()
        .asString(CHARSET)
        .defaultIfEmpty("")
        .flatMap(content -> this.execute(httpRequest, content))
        .onErrorResume(this::handleError)
        .map(apiResponse -> this.parseResponse(httpResponse, apiResponse)),
      CHARSET
    );
  }

  private Mono<ApiResponse> execute(final HttpServerRequest httpRequest, final String content) {
    final JsonObject json;
    try {
      json = content.isEmpty() && content.isBlank() ? JsonUtils.EMPTY_OBJECT : JsonUtils.fromJson(JsonParser.parseString(content), JsonObject.class);
    } catch (JsonSyntaxException e) {
      return Mono.just(new ApiResponse(HttpResponseStatus.BAD_REQUEST, "JSON_SYNTAX"));
    }

    final String endpoint = httpRequest.path().substring(httpRequest.path().indexOf('/') + 1);
    final ApiRequest apiRequest = new RestApiRequest(endpoint, json, httpRequest);

    return ApiExecutor.execute(this.endpoints, apiRequest);
  }

  private String parseResponse(final HttpServerResponse httpResponse, final ApiResponse apiResponse) {
    httpResponse.status(apiResponse.getStatus());
    httpResponse.header(HttpHeaderNames.CONTENT_TYPE, CONTENT_TYPE);

    final boolean hasData = apiResponse.getJson() != null;
    final boolean hasMessage = apiResponse.getMessage() != null;

    if (!hasData && !hasMessage) return EMPTY_RESPONSE;

    final JsonElement jsonResponse = hasData
      ? apiResponse.getJson()
      : JsonBuilder.create("message", apiResponse.getMessage()).build();

    return jsonResponse.toString();
  }

  private Mono<ApiResponse> handleError(final Throwable cause) {
    log.error("Error while executing rest api pipeline.", cause);
    return Mono.just(new ApiResponse(HttpResponseStatus.INTERNAL_SERVER_ERROR));
  }
}
