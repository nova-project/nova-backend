package net.getnova.backend.api.handler.websocket;

import lombok.Getter;
import net.getnova.backend.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Getter
@Config
public final class WebsocketApiConfig {

  @Value("${WEBSOCKET_API:ws}")
  private String path;
}
