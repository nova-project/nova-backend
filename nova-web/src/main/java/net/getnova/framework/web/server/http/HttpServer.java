package net.getnova.framework.web.server.http;

import java.net.SocketAddress;
import java.time.Duration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.getnova.framework.web.server.http.route.HttpRoutes;
import org.springframework.util.unit.DataSize;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.http.HttpProtocol;
import reactor.netty.resources.LoopResources;

@Slf4j
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class HttpServer implements AutoCloseable {

  private static final Duration TIMEOUT = Duration.ofSeconds(5);
  private final LoopResources resources;
  private final SocketAddress address;
  private final HttpRoutes routes;
  private final boolean accessLog;
  private final DataSize minSizeToCompress;

  private DisposableServer server;

  public Mono<? extends DisposableServer> start() {
    if (this.isRunning()) {
      throw new IllegalStateException("server is already running");
    }

    return reactor.netty.http.server.HttpServer.create()
      .runOn(this.resources)
      .bindAddress(() -> this.address)
      .accessLog(this.accessLog)
      .compress(Math.toIntExact(this.minSizeToCompress.toBytes()))
      .forwarded(true)
      .protocol(HttpProtocol.HTTP11 /*, HttpProtocol.H2C*/)
      .idleTimeout(TIMEOUT)
      .handle(this.routes::handle)
      .bind()
      .timeout(TIMEOUT)
      .doOnNext(server -> {
        this.server = server;
        this.server.onDispose(() -> this.server = null);
        log.info("Netty is listening on {}.", this.server.address());

        final Thread thread = new Thread(this.server.onDispose()::block);
        thread.setDaemon(false);
        thread.start();
      });
  }

  @Override
  public void close() throws Exception {
    if (!this.isRunning()) {
      throw new IllegalStateException("Server is not running.");
    }

    log.info("Stopping Netty...");
    this.server.disposeNow(TIMEOUT);
  }

  public boolean isRunning() {
    return this.server != null;
  }

  public SocketAddress getAddress() {
    return this.server == null ? this.address : this.server.address();
  }
}
