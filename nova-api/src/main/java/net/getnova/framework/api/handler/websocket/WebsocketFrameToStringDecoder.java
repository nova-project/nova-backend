package net.getnova.framework.api.handler.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import java.util.List;

public class WebsocketFrameToStringDecoder extends MessageToMessageDecoder<WebSocketFrame> {

  @Override
  protected void decode(final ChannelHandlerContext ctx, final WebSocketFrame msg, final List<Object> out)
    throws Exception {
    out.add(msg.content());
  }
}
