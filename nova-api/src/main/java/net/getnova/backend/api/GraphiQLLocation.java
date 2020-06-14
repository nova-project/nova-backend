package net.getnova.backend.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import graphql.GraphQL;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.Data;
import net.getnova.backend.codec.http.HttpUtils;
import net.getnova.backend.codec.http.server.HttpLocation;
import net.getnova.backend.json.JsonUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Data
public class GraphiQLLocation extends HttpLocation<FullHttpRequest> {

    private final GraphQL graphQL;

    public GraphiQLLocation(final GraphQL graphQL) {
        super(new HttpObjectAggregator(1048576));
        this.graphQL = graphQL;
    }

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest msg) throws Exception {
        if (msg.method().equals(HttpMethod.GET)) this.sendPlayground(ctx, msg);
        else if (msg.method().equals(HttpMethod.POST)) this.executeGraphQL(ctx, msg);
        else HttpUtils.sendStatus(ctx, msg, HttpResponseStatus.METHOD_NOT_ALLOWED);
    }

    private void sendPlayground(final ChannelHandlerContext ctx, final FullHttpRequest msg) throws IOException {
        final DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpUtils.setContentTypeHeader(response, HttpHeaderValues.TEXT_HTML);
        HttpUtils.sendAndCleanupConnection(ctx, msg, response,
                new DefaultHttpContent(Unpooled.copiedBuffer(GraphiQLLocation.class.getResourceAsStream("/www/index.html").readAllBytes())));
    }

    private void executeGraphQL(final ChannelHandlerContext ctx, final FullHttpRequest msg) {
        final JsonObject request = JsonUtils.fromJson(JsonParser.parseString(msg.content().toString(StandardCharsets.UTF_8)), JsonObject.class);

        final JsonElement responseJson = ApiExecutor.execute(this.graphQL, request);

        final DefaultHttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpUtils.setContentTypeHeader(response, HttpHeaderValues.APPLICATION_JSON);

        HttpUtils.sendAndCleanupConnection(ctx, msg,
                response,
                new DefaultHttpContent(Unpooled.copiedBuffer(responseJson.toString(), StandardCharsets.UTF_8))
        );
    }
}
