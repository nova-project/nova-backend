package net.getnova.backend.api.handler.websocket;

import com.google.gson.JsonObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.getnova.backend.api.data.ApiRequest;
import reactor.netty.http.websocket.WebsocketInbound;
import reactor.netty.http.websocket.WebsocketOutbound;

@Getter
@ToString
@EqualsAndHashCode
public class WebsocketApiRequest extends ApiRequest {

  private final WebsocketInbound inbound;
  private final WebsocketOutbound outbound;

  public WebsocketApiRequest(final String tag, final String endpoint, final JsonObject data, final WebsocketInbound inbound, final WebsocketOutbound outbound) {
    super(endpoint, data, tag);
    this.inbound = inbound;
    this.outbound = outbound;
  }
}
