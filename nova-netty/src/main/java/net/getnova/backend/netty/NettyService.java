package net.getnova.backend.netty;

import io.netty.channel.EventLoopGroup;
import net.getnova.backend.service.Service;
import net.getnova.backend.service.event.PreInitService;
import net.getnova.backend.service.event.PreInitServiceEvent;
import net.getnova.backend.service.event.StopService;
import net.getnova.backend.service.event.StopServiceEvent;

import javax.inject.Singleton;

@Service(id = "netty")
@Singleton
public class NettyService {

    private EventLoopGroupHandler eventLoopGroupHandler;

    @PreInitService
    private void preInit(final PreInitServiceEvent event) {
        this.eventLoopGroupHandler = new EventLoopGroupHandler();
    }

    @StopService
    private void stop(final StopServiceEvent event) {
        this.eventLoopGroupHandler.stop();
    }

    /**
     * Returns the current bos group.
     *
     * @return the current bos group.
     * @see EventLoopGroup
     */
    public EventLoopGroup getBosGroup() {
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
