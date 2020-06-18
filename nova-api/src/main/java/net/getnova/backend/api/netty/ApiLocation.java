package net.getnova.backend.api.netty;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import graphql.GraphQL;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.api.ApiExecutor;
import net.getnova.backend.codec.http.HttpUtils;
import net.getnova.backend.codec.http.server.HttpLocation;
import net.getnova.backend.json.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@EqualsAndHashCode
final class ApiLocation extends HttpLocation<HttpObject> {

    private final GraphQL graphQL;
    private HttpRequest request;
    private StringBuilder content;

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final HttpObject msg) throws Exception {
        if (msg instanceof HttpRequest) {
            this.request = (HttpRequest) msg;
            this.content = new StringBuilder();
            this.handleRequest(ctx);
        } else if (msg instanceof HttpContent) {
            final HttpContent content = (HttpContent) msg;
            this.content.append(content.content().toString(HttpUtil.getCharset(this.request, StandardCharsets.UTF_8)));
            if (content instanceof LastHttpContent) this.handleContent(ctx);
        }
    }

    private void handleRequest(final ChannelHandlerContext ctx) throws IOException {
        if (this.request.method().equals(HttpMethod.GET)) this.sendPlayground(ctx);
        else if (!this.request.method().equals(HttpMethod.POST))
            HttpUtils.sendStatus(ctx, this.request, HttpResponseStatus.METHOD_NOT_ALLOWED);
    }

    private void handleContent(final ChannelHandlerContext ctx) {
        if (this.request != null && this.request.method().equals(HttpMethod.POST)) this.executeGraphQL(ctx);
    }

    private void sendPlayground(final ChannelHandlerContext ctx) throws IOException {
        final DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpUtils.setContentTypeHeader(response, HttpHeaderValues.TEXT_HTML);
        HttpUtils.sendAndCleanupConnection(ctx, this.request, response,
                new DefaultHttpContent(Unpooled.copiedBuffer(ApiLocation.class.getResourceAsStream("/www/index.html").readAllBytes())));
    }

    private void executeGraphQL(final ChannelHandlerContext ctx) {
        final JsonObject request = JsonUtils.fromJson(JsonParser.parseString(this.content.toString()), JsonObject.class);

        final JsonElement responseJson = ApiExecutor.execute(this.graphQL, request);

        final DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpUtils.setContentTypeHeader(response, HttpHeaderValues.APPLICATION_JSON);

        HttpUtils.sendAndCleanupConnection(ctx, this.request, response,
                new DefaultHttpContent(Unpooled.copiedBuffer(responseJson.toString(), StandardCharsets.UTF_8)));
    }
}
