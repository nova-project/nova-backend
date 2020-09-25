package net.getnova.backend.api.handler.rest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.data.ApiResponseStatus;
import net.getnova.backend.api.executor.ApiExecutor;
import net.getnova.backend.json.JsonBuilder;
import net.getnova.backend.json.JsonUtils;
import net.getnova.backend.network.server.http.route.HttpRoute;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public final class RestApiLocation implements HttpRoute {

  private final Map<String, ApiEndpointData> endpoints;

  @Override
  public Publisher<Void> execute(final HttpServerRequest request, final HttpServerResponse response) {
    return response.sendString(request.receive()
      .aggregate()
      .asString()
      .defaultIfEmpty("{}")
      .map(content -> {
        final ApiRequest apiRequest = new ApiRequest(
          request.path().substring(request.path().indexOf('/') + 1),
          content.isBlank() ? JsonUtils.EMPTY_OBJECT : JsonUtils.fromJson(JsonParser.parseString(content), JsonObject.class),
          null
        );
        return ApiExecutor.execute(this.endpoints, apiRequest);
      })
      .onErrorReturn(cause -> cause instanceof JsonSyntaxException || cause.getCause() instanceof JsonSyntaxException,
        new ApiResponse(ApiResponseStatus.BAD_REQUEST, "JSON_SYNTAX"))
      .flatMap(apiResponse -> {
        final ApiResponseStatus responseStatus = apiResponse.getResponseCode();

        response.status(HttpResponseStatus.valueOf(responseStatus.getCode(), responseStatus.getName()));
        response.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);

        final boolean hasData = apiResponse.getJson() != null;
        final boolean hasMessage = apiResponse.getMessage() != null;

        if (!(hasData || hasMessage)) return Mono.empty();

        final JsonElement jsonResponse = hasData
          ? apiResponse.getJson()
          : JsonBuilder.create("message", apiResponse.getMessage()).build();

        return Mono.just(jsonResponse.toString());
      })
    );
  }
}
