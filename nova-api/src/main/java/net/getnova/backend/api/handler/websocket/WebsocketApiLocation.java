package net.getnova.backend.api.handler.websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.data.ApiResponseStatus;
import net.getnova.backend.api.executor.ApiExecutor;
import net.getnova.backend.json.JsonTypeMappingException;
import net.getnova.backend.json.JsonUtils;
import org.reactivestreams.Publisher;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

@RequiredArgsConstructor
public final class WebsocketApiLocation implements BiFunction<WebsocketInbound, WebsocketOutbound, Publisher<Void>> {

  private final Map<String, ApiEndpointData> endpoints;

  @Override
  public Publisher<Void> apply(final WebsocketInbound inbound, final WebsocketOutbound outbound) {
    return outbound.sendString(inbound.receiveFrames()
      .map(frame -> {
        JsonObject json = null;
        ApiResponse apiResponse = null;

        try {
          json = JsonUtils.fromJson(JsonParser.parseString(frame.content().toString(StandardCharsets.UTF_8)), JsonObject.class);
        } catch (JsonSyntaxException e) {
          apiResponse = new ApiResponse(ApiResponseStatus.BAD_REQUEST, "JSON_SYNTAX");
        } catch (JsonTypeMappingException e) {
          apiResponse = e.getCause() instanceof JsonSyntaxException
            ? new ApiResponse(ApiResponseStatus.BAD_REQUEST, "JSON_SYNTAX")
            : new ApiResponse(ApiResponseStatus.INTERNAL_SERVER_ERROR);
        }

        if (apiResponse == null) {
          final String tag = JsonUtils.fromJson(json.get("tag"), String.class);
          final String endpoint = JsonUtils.fromJson(json.get("endpoint"), String.class);
          final JsonObject data = JsonUtils.fromJson(json.get("data"), JsonObject.class);

          if (tag == null) {
            apiResponse = new ApiResponse(ApiResponseStatus.BAD_REQUEST, "MISSING_TAG");
          } else if (endpoint == null) {
            apiResponse = new ApiResponse(ApiResponseStatus.BAD_REQUEST, "MISSING_ENDPOINT");
          } else {
            apiResponse = ApiExecutor.execute(this.endpoints, new ApiRequest(endpoint, data == null ? new JsonObject() : data, tag));
          }

          if (apiResponse != null) apiResponse.setTag(tag);
        }

        return apiResponse;
      })
      .filter(Objects::nonNull)
      .map(response -> response.serialize().toString()));
  }
}
