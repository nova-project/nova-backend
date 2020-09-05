package net.getnova.backend.api.handler.websocket;

import lombok.AccessLevel;
import lombok.Getter;
import net.getnova.backend.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Config
@Getter(AccessLevel.PACKAGE)
final class WebsocketApiConfig {

  @Value("${WEBSOCKET_API:ws}")
  private String path;
}
