package net.getnova.backend.api.handler.websocket;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import net.getnova.backend.config.ConfigValue;

@Data
@Setter(AccessLevel.NONE)
public final class WebsocketApiConfig {

  @ConfigValue(id = "path", comment = "The path of in the url, were the websocket is available.")
  private String path = "ws";
}
