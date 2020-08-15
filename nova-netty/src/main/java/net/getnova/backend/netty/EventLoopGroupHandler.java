package net.getnova.backend.netty;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.AccessLevel;
import lombok.Getter;

@Getter(AccessLevel.PACKAGE)
final class EventLoopGroupHandler {

  private static final boolean EPOLL = Epoll.isAvailable();

  private final EventLoopGroup bossGroup;
  private final EventLoopGroup workGroup;

  EventLoopGroupHandler() {
    this.bossGroup = EPOLL ? new EpollEventLoopGroup(1) : new NioEventLoopGroup(1);
    this.workGroup = EPOLL ? new EpollEventLoopGroup() : new NioEventLoopGroup();
  }

  void stop() {
    if (!this.bossGroup.isShutdown()) this.bossGroup.shutdownGracefully();
    if (!this.workGroup.isShutdown()) this.workGroup.shutdownGracefully();
  }
}
