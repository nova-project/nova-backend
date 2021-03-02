package net.getnova.framework.web.server.http.route;

import org.reactivestreams.Publisher;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public interface HttpRouteHandler {

  Publisher<Void> handle(HttpRoute route, HttpServerRequest request, HttpServerResponse response) throws Exception;
}
