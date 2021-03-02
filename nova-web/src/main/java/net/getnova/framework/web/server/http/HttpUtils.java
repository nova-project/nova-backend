package net.getnova.framework.web.server.http;

import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

public final class HttpUtils {

  private HttpUtils() {
    throw new UnsupportedOperationException();
  }

  public static Publisher<Void> status(final HttpServerResponse response, final HttpResponseStatus status) {
    response.status(status);
    return response.header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
      .sendString(Mono.just(status.toString()));
  }

  public static Publisher<Void> status(final HttpServerResponse response, final HttpResponseStatus status,
    final String message) {
    return response.status(status)
      .header(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.TEXT_PLAIN)
      .sendString(Mono.just(status.toString() + ": " + message));
  }

  public static Publisher<Void> methodNotAllowed(final HttpServerResponse response, final Set<HttpMethod> methods) {
    response.header(
      HttpHeaderNames.ALLOW,
      methods.stream()
        .map(HttpMethod::asciiName)
        .collect(Collectors.joining(", "))
    );

    return status(response, HttpResponseStatus.METHOD_NOT_ALLOWED);
  }

  public static Charset getCharset(final HttpServerRequest request, final Charset fallback) {
    return HttpUtil.getCharset(request.requestHeaders().get(HttpHeaderNames.CONTENT_TYPE), fallback);
  }

  public static Optional<Charset> getCharset(final HttpHeaders headers) {
    return Optional.ofNullable(
      HttpUtil.getCharsetAsSequence(headers.get(HttpHeaderNames.CONTENT_TYPE))
    ).flatMap(charsetName -> {
      try {
        return Optional.of(Charset.forName(charsetName.toString()));
      }
      catch (IllegalCharsetNameException | UnsupportedCharsetException ignored) {
        return Optional.empty();
      }
    });
  }
}
