package net.getnova.backend.codec.http.server.location.file;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.codec.http.HttpUtils;
import net.getnova.backend.codec.http.server.HttpLocation;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
class HttpFileLocation extends HttpLocation<HttpMessage> {

    private final String baseDir;
    private final boolean edit;
    private HttpRequest request;
    private File file;
    private boolean finish = false;

    @Override
    protected void channelRead0(final ChannelHandlerContext ctx, final HttpMessage msg) throws Exception {
        if (msg instanceof HttpRequest) this.handleRequest(ctx, (HttpRequest) msg);
        else if (msg instanceof HttpContent && !this.finish) this.handleContent(ctx, (HttpContent) msg);
    }

    private void handleRequest(final ChannelHandlerContext ctx, final HttpRequest request) throws IOException {
        this.request = request;

        if (!request.decoderResult().isSuccess()) {
            HttpUtils.sendStatus(ctx, request, HttpResponseStatus.BAD_REQUEST);
            this.finish = true;
            return;
        }

        this.file = new File(this.baseDir + request.uri()).getAbsoluteFile();

        final boolean head = request.method().equals(HttpMethod.HEAD);
        if (request.method().equals(HttpMethod.GET) || head) {
            this.finish = true;

            if (!HttpUtils.fileExist(this.baseDir, this.file)) {
                HttpUtils.sendStatus(ctx, request, HttpResponseStatus.NOT_FOUND, !head);
                return;
            }

            if (this.file.isDirectory()) {
                if (!request.uri().endsWith("/")) HttpUtils.sendRedirect(ctx, request, request.uri() + '/');
                else sendDirectory(ctx, request.uri(), this.file, head);
                return;
            }

            HttpUtils.sendFile(ctx, request, this.file, head);
        } else {
            HttpUtils.sendStatus(ctx, request, HttpResponseStatus.METHOD_NOT_ALLOWED);
            this.finish = true;
        }
    }

    private void handleContent(final ChannelHandlerContext ctx, final HttpContent content) {
    }

    private void sendDirectory(final ChannelHandlerContext ctx, final String uri, final File file, final boolean onlyHead) {
        final HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpUtils.setContentTypeHeader(response, HttpHeaderValues.TEXT_HTML);
        HttpUtils.sendAndCleanupConnection(ctx, request, response, onlyHead ? null : new DefaultHttpContent(HttpFileLocationDirectoryBrowser.create(this.baseDir, uri, file)));
    }
}
