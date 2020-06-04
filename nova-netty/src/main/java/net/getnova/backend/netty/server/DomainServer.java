package net.getnova.backend.netty.server;

import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollServerDomainSocketChannel;
import io.netty.channel.kqueue.KQueueServerSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;
import io.netty.handler.ssl.SslContext;
import net.getnova.backend.netty.codec.CodecHandler;

public class DomainServer extends Server {

    /**
     * Creates a new Server which is able to liston on any unix socket.
     *
     * @param id           the if of the server
     * @param address      the {@link DomainSocketAddress} of the server
     * @param sslContext   the {@link SslContext} of the server,
     *                     it can be {@code null} to disable encryption.
     * @param codecHandler the {@link CodecHandler} with the codecs for the server
     */
    public DomainServer(final String id, final DomainSocketAddress address, final SslContext sslContext, final CodecHandler codecHandler) {
        super(id, address, sslContext, codecHandler);
    }

    @Override
    protected final Class<? extends ServerChannel> getServerChannelType(final boolean epoll) {
        return epoll ? EpollServerDomainSocketChannel.class : KQueueServerSocketChannel.class;
    }
}
