package net.getnova.backend.codec.http.server;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.RequiredArgsConstructor;
import net.getnova.backend.injection.InjectionHandler;
import net.getnova.backend.netty.codec.CodecInitializer;

import java.util.Map;

@RequiredArgsConstructor
class HttpServerCodecInitializer implements CodecInitializer {

    private final InjectionHandler injectionHandler;
    private final Map<String, HttpLocationProvider<?>> locationProviders;

    @Override
    public void configure(final ChannelPipeline pipeline) {
        pipeline.addLast("http-server-codec", new HttpServerCodec());
        pipeline.addLast("chunked-write-handler", new ChunkedWriteHandler());
        pipeline.addLast("http-server-content-handler", new HttpServerContentDecoder(this.injectionHandler, this.locationProviders));
    }
}
