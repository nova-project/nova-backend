package net.getnova.framework.api.handler.websocket;

import net.getnova.framework.json.JsonBuilder;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.websocket.WebsocketOutbound;

import java.nio.charset.StandardCharsets;

public class WebsocketApiNotification {

  private final String data;

  public WebsocketApiNotification(final String topic, final Object data) {
    this.data = JsonBuilder.create("topic", topic)
      .add("data", data)
      .build()
      .toString();
  }

  public Publisher<Void> send(final WebsocketOutbound outbound) {
    return outbound.sendString(Mono.just(this.data), StandardCharsets.UTF_8);
  }
}
