package net.getnova.backend.network.server.http.route;

import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public interface HttpRoute {

  Publisher<Void> execute(final HttpServerRequest request, final HttpServerResponse response);
}
