package net.getnova.backend.network.server.http;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerResponse;

public final class HttpUtils {

  private HttpUtils() {
    throw new UnsupportedOperationException();
  }

  public static Publisher<Void> sendStatus(final HttpServerResponse response, final HttpResponseStatus status) {
    return sendStatus(response, status, true);
  }

  public static Publisher<Void> sendStatus(final HttpServerResponse response, final HttpResponseStatus status, final boolean body) {
    response.status(status);
    return body
      ? response.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN).sendString(Mono.just(status.toString()))
      : response.send();
  }

  public static Publisher<Void> sendStatus(final HttpServerResponse response, final HttpResponseStatus status, final String message) {
    return response.status(status)
      .header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
      .sendString(Mono.just(status.toString() + ": " + message));
  }
}
