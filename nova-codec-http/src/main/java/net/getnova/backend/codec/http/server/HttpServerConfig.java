package net.getnova.backend.codec.http.server;

import lombok.AccessLevel;
import lombok.Getter;
import net.getnova.backend.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Config
@Getter(AccessLevel.PACKAGE)
class HttpServerConfig {

  @Value("${HTTP_PORT:6060}")
  private final int port = 6060;
  @Value("${HTTP_HOST:0.0.0.0}")
  private String host;
}
