package net.getnova.backend.netty.server;

import net.getnova.backend.boot.module.Module;
import net.getnova.backend.netty.NettyModule;

import javax.annotation.PreDestroy;
import java.util.LinkedList;
import java.util.List;

@Module(NettyModule.class)
public class ServerModule {

  private final NettyModule nettyModule;
  private final List<Server> servers;

  /**
   * Creates a new {@link ServerModule}.
   *
   * @param nettyModule the service provided by the dependency injection
   */
  public ServerModule(final NettyModule nettyModule) {
    this.nettyModule = nettyModule;
    this.servers = new LinkedList<>();
  }

  @PreDestroy
  private void preDestroy() {
    this.servers.forEach(Server::close);
  }

  /**
   * Adds a server to the managed server lifecycle.
   *
   * @param server the server witch should be added to the lifecycle.
   */
  public void addServer(final Server server) {
    server.setNettyModule(this.nettyModule);
    this.servers.add(server);
    server.start();
  }
}
