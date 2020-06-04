package net.getnova.backend.netty.server;

import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.ssl.SslContext;
import net.getnova.backend.netty.codec.CodecHandler;

import java.net.InetSocketAddress;

public class InetServer extends Server {

    /**
     * Creates a new Server which is able to liston on any network interface.
     *
     * @param id           the if of the server
     * @param address      the {@link InetSocketAddress} of the server
     * @param sslContext   the {@link SslContext} of the server,
     *                     it can be {@code null} to disable encryption.
     * @param codecHandler the {@link CodecHandler} with the codecs for the server
     */
    public InetServer(final String id, final InetSocketAddress address, final SslContext sslContext, final CodecHandler codecHandler) {
        super(id, address, sslContext, codecHandler);
    }

    @Override
    protected final Class<? extends ServerChannel> getServerChannelType(final boolean epoll) {
        return epoll ? EpollServerSocketChannel.class : NioServerSocketChannel.class;
    }
}
