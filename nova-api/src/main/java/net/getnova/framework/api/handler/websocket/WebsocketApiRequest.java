package net.getnova.framework.api.handler.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.getnova.framework.api.data.ApiRequest;

@Getter
@ToString
@EqualsAndHashCode
public class WebsocketApiRequest extends ApiRequest {

  private final WebsocketApiContext context;

  public WebsocketApiRequest(final String tag, final String endpoint, final JsonNode data,
    final WebsocketApiContext context) {
    super(endpoint, data, tag);
    this.context = context;
  }
}
