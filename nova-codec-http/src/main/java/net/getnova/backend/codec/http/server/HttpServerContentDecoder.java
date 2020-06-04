package net.getnova.backend.codec.http.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.codec.http.HttpUtils;
import net.getnova.backend.injection.InjectionHandler;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@EqualsAndHashCode
class HttpServerContentDecoder extends MessageToMessageDecoder<HttpRequest> {

    private final InjectionHandler injectionHandler;
    private final Map<String, HttpLocationProvider<?>> locationProviders;

    @Override
    protected void decode(final ChannelHandlerContext ctx, final HttpRequest msg, final List<Object> out) throws Exception {
        if (HttpUtil.is100ContinueExpected(msg)) {
            HttpUtils.sendStatus(ctx, msg, HttpResponseStatus.CONTINUE, false);
        }

        final String uri = HttpUtils.decodeUri(msg.uri());
        if (uri == null) {
            HttpUtils.sendStatus(ctx, msg, HttpResponseStatus.BAD_REQUEST);
            return;
        }

        final HttpLocation<?> location = this.getLocation(uri.substring(1).toLowerCase());
        if (location == null) {
            if (uri.endsWith("/")) HttpUtils.sendStatus(ctx, msg, HttpResponseStatus.NOT_FOUND);
            else HttpUtils.sendRedirect(ctx, msg, uri + "/");
            return;
        }

        this.configurePipeline(ctx.pipeline(), location);
        out.add(msg);
    }

    private HttpLocation<?> getLocation(final String path) {
        for (Map.Entry<String, HttpLocationProvider<?>> locationProvider : this.locationProviders.entrySet()) {
            if (path.startsWith(locationProvider.getKey())) {
                final HttpLocation<?> location = locationProvider.getValue().getLocation();
                this.injectionHandler.getInjector().injectMembers(location);
                return location;
            }
        }
        return null;
    }

    private void configurePipeline(final ChannelPipeline pipeline, final HttpLocation<?> location) {
        for (final ChannelHandler handler : location.getHandlers()) pipeline.addLast(handler);
        pipeline.addLast(location);
        pipeline.remove(this);
    }
}
