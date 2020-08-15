package net.getnova.backend.netty.codec;

import io.netty.channel.ChannelPipeline;

public interface CodecInitializer {

  /**
   * This method configures a {@link ChannelPipeline} with the necessary Handlers.
   *
   * @param pipeline the {@link ChannelPipeline} which should be configured
   */
  void configure(ChannelPipeline pipeline);
}
