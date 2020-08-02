package net.getnova.backend.api.handler.rest;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import net.getnova.backend.api.data.ApiRequest;
import net.getnova.backend.api.data.ApiResponse;
import net.getnova.backend.api.data.ApiResponseStatus;
import net.getnova.backend.codec.http.HttpUtils;
import net.getnova.backend.json.JsonTypeMappingException;
import net.getnova.backend.json.JsonUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public final class RestApiCodec extends MessageToMessageCodec<FullHttpRequest, ApiResponse> {

    private HttpRequest request;

    @Override
    protected void decode(final ChannelHandlerContext ctx, final FullHttpRequest msg, final List<Object> out) throws Exception {
        this.request = msg;
        if (msg.method().equals(HttpMethod.GET)) out.add(this.getRequest(msg));
        else if (msg.method().equals(HttpMethod.POST)) out.add(this.postRequest(msg));
        else out.add(new DefaultFullHttpResponse(msg.protocolVersion(), HttpResponseStatus.METHOD_NOT_ALLOWED));
    }

    @Override
    protected void encode(final ChannelHandlerContext ctx, final ApiResponse msg, final List<Object> out) throws Exception {
        final HttpResponse httpResponse = new DefaultHttpResponse(this.request.protocolVersion(), HttpResponseStatus.valueOf(
                msg.getResponseCode().getCode(),
                msg.getResponseCode().getName()
        ));
        HttpUtils.setContentTypeHeader(httpResponse, HttpHeaderValues.APPLICATION_JSON, HttpUtils.CHARSET);

        final ByteBuf responseContent = Unpooled.copiedBuffer(msg.serialize().toString(), HttpUtils.CHARSET);
        HttpUtil.setContentLength(httpResponse, responseContent.readableBytes());

        out.add(httpResponse);
        out.add(new DefaultLastHttpContent(responseContent));
    }

    private String getEndpoint(final String uri) throws URISyntaxException {
        return new URI(uri).getPath().substring(1);
    }

    private Object getRequest(final FullHttpRequest request) throws URISyntaxException {
        return new ApiRequest(this.getEndpoint(request.uri()), new JsonObject(), null);
    }

    private Object postRequest(final FullHttpRequest request) throws URISyntaxException {
        try {
            return new ApiRequest(
                    this.getEndpoint(request.uri()),
                    JsonUtils.fromJson(JsonParser.parseString(request.content().toString(HttpUtils.CHARSET)), JsonObject.class),
                    null
            );
        } catch (JsonSyntaxException e) {
            return new ApiResponse(ApiResponseStatus.BAD_REQUEST, "JSON_SYNTAX");
        } catch (JsonTypeMappingException e) {
            return e.getCause() instanceof JsonSyntaxException
                    ? new ApiResponse(ApiResponseStatus.BAD_REQUEST, "NOT_A_JSON_OBJECT")
                    : new ApiResponse(ApiResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
