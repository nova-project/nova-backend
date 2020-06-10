package net.getnova.backend.codec.http.server;

import net.getnova.backend.injection.InjectionHandler;
import net.getnova.backend.netty.codec.Codec;
import net.getnova.backend.netty.codec.CodecInitializer;

import java.util.HashMap;
import java.util.Map;

class HttpServerCodec implements Codec {

    private final InjectionHandler injectionHandler;
    private final Map<String, HttpLocationProvider<?>> locationProviders;

    HttpServerCodec(final InjectionHandler injectionHandler) {
        this.injectionHandler = injectionHandler;
        this.locationProviders = new HashMap<>();
    }

    @Override
    public final CodecInitializer getInitializer() {
        return new HttpServerCodecInitializer(this.injectionHandler, this.locationProviders);
    }

    /**
     * Adds a new {@link HttpLocationProvider} to the {@link Map} of location providers.
     *
     * @param path     the path to the {@link HttpLocation}. Without a {@code /} at the start
     * @param provider {@link HttpLocationProvider} instance of the {@link HttpLocation}
     * @see HttpLocationProvider
     * @see HttpLocation
     */
    void addLocationProvider(final String path, final HttpLocationProvider<?> provider) {
        this.locationProviders.put(path.endsWith("/") || path.isEmpty() ? path : path + "/", provider);
    }
}
