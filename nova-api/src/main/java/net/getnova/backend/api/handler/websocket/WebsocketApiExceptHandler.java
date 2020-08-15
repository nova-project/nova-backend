package net.getnova.backend.api.handler.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import net.getnova.backend.codec.http.HttpUtils;
import net.getnova.backend.netty.NettyChannelHandler;

final class WebsocketApiExceptHandler extends NettyChannelHandler<FullHttpRequest> {

  @Override
  protected void channelRead0(final ChannelHandlerContext ctx, final FullHttpRequest msg) throws Exception {
    if (!msg.method().equals(HttpMethod.GET)) HttpUtils.sendStatus(ctx, msg, HttpResponseStatus.METHOD_NOT_ALLOWED);
    else if (!msg.uri().equals("/")) HttpUtils.sendStatus(ctx, msg, HttpResponseStatus.NOT_FOUND);
  }
}
