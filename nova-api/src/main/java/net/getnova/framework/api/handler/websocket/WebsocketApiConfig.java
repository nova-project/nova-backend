package net.getnova.framework.api.handler.websocket;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter(AccessLevel.PACKAGE)
final class WebsocketApiConfig {

  @Value("${WEBSOCKET_API_PATH:ws}")
  private String path;
}
