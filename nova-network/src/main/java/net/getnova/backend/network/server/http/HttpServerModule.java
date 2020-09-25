package net.getnova.backend.network.server.http;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.getnova.backend.boot.Bootstrap;
import net.getnova.backend.boot.module.Module;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.net.InetSocketAddress;
import java.time.Duration;

@Slf4j
@Module
@ComponentScan
public class HttpServerModule {

  @Getter
  private final HttpRoutes routes;
  @Getter
  private final HttpServer server;

  public HttpServerModule(
    final Bootstrap bootstrap,
    final HttpConfig config
  ) {
    this.routes = new HttpRoutes();
    if (!config.getCrtPath().isBlank() && !config.getKeyPath().isBlank()) {
      this.server = new HttpServer(
        "http",
        new InetSocketAddress(config.getHost(), config.getPort()),
        Duration.ofSeconds(10),
        this.routes,
        new File(config.getCrtPath()),
        new File(config.getKeyPath())
      );
    } else {
      this.server = new HttpServer(
        "http",
        new InetSocketAddress(config.getHost(), config.getPort()),
        Duration.ofSeconds(10),
        this.routes
      );
    }
  }

  @PostConstruct
  private void postConstruct() {
    this.server.start();
  }

  @PreDestroy
  private void onDestroy() {
    if (this.server != null) this.server.stop();
  }
}
