package net.getnova.backend.api.handler.websocket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.data.ApiResponseStatus;
import net.getnova.backend.api.executor.ApiExecutor;
import net.getnova.backend.codec.http.server.HttpLocation;
import net.getnova.backend.json.JsonTypeMappingException;
import net.getnova.backend.json.JsonUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class WebsocketApiLocation extends HttpLocation<TextWebSocketFrame> {

  private final Map<String, ApiEndpointData> endpoints;

  public WebsocketApiLocation(@NotNull final Map<String, ApiEndpointData> endpoints) {
    super(new HttpObjectAggregator(65536), new WebSocketServerProtocolHandler("/"), new WebsocketApiExceptHandler());
    this.endpoints = endpoints;
  }

  @Override
  protected void channelRead0(@NotNull final ChannelHandlerContext ctx, @NotNull final TextWebSocketFrame msg) throws Exception {
    JsonObject json = null;
    ApiResponse apiResponse = null;

    try {
      json = JsonUtils.fromJson(JsonParser.parseString(msg.text()), JsonObject.class);
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

    if (apiResponse != null) ctx.write(new TextWebSocketFrame(apiResponse.serialize().toString()));
  }
}
