package net.getnova.framework.network.server.http;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.boot.module.Module;
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

  public HttpServerModule(final HttpConfig config) {
    this.routes = new HttpRoutes();
    this.server = !config.getCertPath().isBlank() && !config.getKeyPath().isBlank()
      ? this.createSecureHttpServer(config.getHost(), config.getPort(), config.getCertPath(), config.getKeyPath())
      : this.createHttpServer(config.getHost(), config.getPort());
  }

  private HttpServer createHttpServer(final String host, final int port) {
    return new HttpServer("http", new InetSocketAddress(host, port), Duration.ofSeconds(10), this.routes);
  }

  private HttpServer createSecureHttpServer(final String host, final int port, final String certPath, final String keyPath) {
    return new HttpServer("http", new InetSocketAddress(host, port), Duration.ofSeconds(10), this.routes, new File(certPath), new File(keyPath));
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
