package net.getnova.backend.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.netty.codec.CodecInitializer;

import java.util.List;

@RequiredArgsConstructor
@EqualsAndHashCode
public final class NettyInitializer extends ChannelInitializer<Channel> {

    private final SslContext sslContext;
    private final List<CodecInitializer> initializers;

    @Override
    protected void initChannel(final Channel channel) {
        final ChannelPipeline pipeline = channel.pipeline();

        if (this.sslContext != null) pipeline.addLast("ssl", this.sslContext.newHandler(channel.alloc()));
        this.initializers.forEach(initializer -> initializer.configure(pipeline));
    }
}
