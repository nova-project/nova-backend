package net.getnova.backend.network.server.http;

import lombok.AccessLevel;
import lombok.Getter;
import net.getnova.backend.boot.config.Config;
import org.springframework.beans.factory.annotation.Value;

@Config
@Getter(AccessLevel.PACKAGE)
public class HttpConfig {

  @Value("${HTTP_HOST:0.0.0.0}")
  private String host;

  @Value("${HTTP_PORT:6060}")
  private int port;

  @Value("${HTTP_TLS_CRT}")
  private String crtPath;

  @Value("${HTTP_TLS_KEY}")
  private String keyPath;
}
