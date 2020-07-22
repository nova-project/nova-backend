package net.getnova.backend.api.handler.rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.EqualsAndHashCode;
import net.getnova.backend.api.data.ApiEndpointData;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.data.ApiResponseStatus;
import net.getnova.backend.api.executor.ApiExecutor;
import net.getnova.backend.codec.http.HttpUtils;
import net.getnova.backend.codec.http.server.HttpLocation;
import net.getnova.backend.json.JsonTypeMappingException;
import net.getnova.backend.json.JsonUtils;
import org.jetbrains.annotations.NotNull;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@EqualsAndHashCode
public final class RestApiLocation extends HttpLocation<FullHttpRequest> {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private final Map<String, ApiEndpointData> endpoints;

    public RestApiLocation(@NotNull final Map<String, ApiEndpointData> endpoints) {
        super(new HttpObjectAggregator(65536));
        this.endpoints = endpoints;
    }

    @Override
    protected void channelRead0(@NotNull final ChannelHandlerContext ctx, @NotNull final FullHttpRequest msg) throws Exception {
        if (!(msg.method().equals(HttpMethod.POST) || msg.method().equals(HttpMethod.GET))) {
            HttpUtils.sendStatus(ctx, msg, HttpResponseStatus.METHOD_NOT_ALLOWED);
            return;
        }

        JsonObject json = null;
        ApiResponse apiResponse = null;

        try {
            json = JsonUtils.fromJson(JsonParser.parseString(msg.content().toString(CHARSET)), JsonObject.class);
        } catch (JsonSyntaxException e) {
            apiResponse = new ApiResponse(ApiResponseStatus.BAD_REQUEST, "JSON_SYNTAX");
        } catch (JsonTypeMappingException e) {
            apiResponse = e.getCause() instanceof JsonSyntaxException
                    ? new ApiResponse(ApiResponseStatus.BAD_REQUEST, "JSON_SYNTAX")
                    : new ApiResponse(ApiResponseStatus.INTERNAL_SERVER_ERROR);
        }

        if (apiResponse == null) {
            apiResponse = ApiExecutor.execute(this.endpoints, new ApiRequest(new URI(msg.uri()).getPath().substring(1), json == null ? new JsonObject() : json, null));
        }

        if (apiResponse != null) {
            final HttpResponse httpResponse = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.valueOf(
                    apiResponse.getResponseCode().getCode(),
                    apiResponse.getResponseCode().getDisplayName()
            ));
            HttpUtils.setContentTypeHeader(httpResponse, HttpHeaderValues.APPLICATION_JSON, CHARSET);

            HttpUtils.sendAndCleanupConnection(ctx, msg, httpResponse,
                    new DefaultHttpContent(Unpooled.copiedBuffer(apiResponse.serialize().toString(), CHARSET)));
        }
    }
}
