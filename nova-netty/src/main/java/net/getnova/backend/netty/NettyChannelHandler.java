package net.getnova.backend.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class NettyChannelHandler<T> extends SimpleChannelInboundHandler<T> {

  /**
   * Flushes all pending content.
   */
  @Override
  public void channelReadComplete(final ChannelHandlerContext ctx) {
    ctx.flush();
  }

  /**
   * Prints a error message with the error on the console.
   *
   * @param ctx   the {@link ChannelHandlerContext} were the error happened
   * @param cause the {@link Throwable} of the error
   */
  @Override
  public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
    log.error("Error while processing channel \"" + ctx.channel().toString() + "\".", cause);
  }
}
