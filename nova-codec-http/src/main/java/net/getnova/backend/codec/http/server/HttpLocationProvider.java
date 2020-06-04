package net.getnova.backend.codec.http.server;

public interface HttpLocationProvider<L extends HttpLocation<?>> {

    /**
     * Returns a {@link HttpLocation} witch has to be every time a new instance.
     *
     * @return the {@link HttpLocation} instance
     */
    L getLocation();
}
