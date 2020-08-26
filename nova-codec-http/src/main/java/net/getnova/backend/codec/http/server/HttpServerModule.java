package net.getnova.backend.codec.http.server;

import net.getnova.backend.boot.module.Module;
import net.getnova.backend.netty.codec.CodecHandler;
import net.getnova.backend.netty.server.InetServer;
import net.getnova.backend.netty.server.ServerModule;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;

@ComponentScan
@Module(ServerModule.class)
public class HttpServerModule {

  private final HttpServerConfig config;
  private final HttpServerCodec codec;
  private final ServerModule serverModule;

  public HttpServerModule(final HttpServerConfig config,
                          final ServerModule serverModule) {
    this.config = config;
    this.codec = new HttpServerCodec();
    this.serverModule = serverModule;
  }

  @PostConstruct
  private void postConstruct() {
    this.serverModule.addServer(new InetServer("http",
      new InetSocketAddress(this.config.getHost(), this.config.getPort()),
      null,
      new CodecHandler(this.codec)));
  }

  /**
   * Adds a new {@link HttpLocationProvider} to the {@link java.util.Map} of location providers.
   *
   * @param path     the path to the {@link HttpLocation}. Without a {@code /} at the start
   * @param provider {@link HttpLocationProvider} instance of the {@link HttpLocation}
   * @see HttpLocationProvider
   * @see HttpLocation
   */
  public void addLocationProvider(final String path, final HttpLocationProvider<?> provider) {
    this.codec.addLocationProvider(path.toLowerCase(), provider);
  }
}
