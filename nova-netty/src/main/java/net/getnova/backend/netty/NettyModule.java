package net.getnova.backend.netty;

import io.netty.channel.EventLoopGroup;
import net.getnova.backend.boot.module.Module;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Module
public class NettyModule {

  private EventLoopGroupHandler eventLoopGroupHandler;

  @PostConstruct
  private void postConstruct() {
    this.eventLoopGroupHandler = new EventLoopGroupHandler();
  }

  @PreDestroy
  private void preDestroy() {
    this.eventLoopGroupHandler.stop();
  }

  /**
   * Returns the current bos group.
   *
   * @return the current bos group.
   * @see EventLoopGroup
   */
  public EventLoopGroup getBossGroup() {
    return this.eventLoopGroupHandler.getBossGroup();
  }

  /**
   * Returns the current worker group.
   *
   * @return the current worker group.
   * @see EventLoopGroup
   */
  public EventLoopGroup getWorkerGroup() {
    return this.eventLoopGroupHandler.getWorkGroup();
  }
}
