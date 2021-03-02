package net.getnova.framework.web.server.http.route;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;

public interface WebsocketRouteHandler extends HttpRouteHandler {

  @Override
  default Publisher<Void> handle(
    final HttpRoute route,
    final HttpServerRequest request,
    final HttpServerResponse response
  ) {
    if (request.requestHeaders().containsValue(HttpHeaderNames.CONNECTION, HttpHeaderValues.UPGRADE, true)) {
      return response.sendWebsocket(this::handle);
    }

    return response.status(HttpResponseStatus.BAD_REQUEST)
      .header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
      .sendString(Mono.just("not a WebSocket handshake request: missing upgrade"));
  }

  Publisher<Void> handle(WebsocketInbound inbound, WebsocketOutbound outbound);
}
