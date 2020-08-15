package net.getnova.backend.api.handler.playground;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.DefaultLastHttpContent;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.codec.http.HttpUtils;
import net.getnova.backend.codec.http.server.HttpLocation;

import java.io.File;
import java.net.URI;

@RequiredArgsConstructor
final class PlaygroundLocation extends HttpLocation<HttpRequest> {

  private static final String INDEX_FILE = "index.html";
  private final File baseDir;
  private final ByteBuf playgroundContent;
  private long playgroundContentLength = -1;

  @Override
  protected void channelRead0(final ChannelHandlerContext ctx, final HttpRequest msg) throws Exception {
    final String path = new URI(msg.uri()).getPath();
    final File file = new File(this.baseDir, path.endsWith("/") ? path + INDEX_FILE : path);
    final boolean head = msg.method().equals(HttpMethod.HEAD);

    if (file.getName().equals("playground.json")) {
      final DefaultHttpResponse response = new DefaultHttpResponse(msg.protocolVersion(), HttpResponseStatus.OK);
      if (this.playgroundContentLength == -1) {
        this.playgroundContentLength = this.playgroundContent.readableBytes();
      }
      HttpUtil.setContentLength(response, this.playgroundContentLength);
      HttpUtils.setContentTypeHeader(response, "application/json", HttpUtils.CHARSET);

      HttpUtils.sendAndCleanupConnection(ctx, response, new DefaultLastHttpContent(this.playgroundContent));
      return;
    }

    if (!HttpUtils.fileExist(this.baseDir, file)) {
      HttpUtils.sendStatus(ctx, msg, HttpResponseStatus.NOT_FOUND, !head);
      return;
    }

    if (file.isDirectory()) {
      HttpUtils.sendRedirect(ctx, msg, this.getOriginalUri().resolve(this.getOriginalUri().getRawPath() + '/'));
      return;
    }

    HttpUtils.sendFile(ctx, msg, file, !head);
  }
}
