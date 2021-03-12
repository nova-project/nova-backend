package net.getnova.framework.web.client.http;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import net.getnova.framework.web.HttpStatusException;
import reactor.netty.ByteBufMono;
import reactor.netty.http.client.HttpClientResponse;

@EqualsAndHashCode
@SuppressWarnings("PMD.MissingSerialVersionUID")
public class HttpClientStatusException extends HttpStatusException {

  @Getter
  private final HttpMethod method;
  @Getter
  private final String uri;
  @Getter
  private final HttpClientResponse response;
  @Getter
  private final ByteBufMono data;

  public HttpClientStatusException(
    final HttpMethod method,
    final String uri,
    final HttpClientResponse response,
    final ByteBufMono data
  ) {
    super(response.status().toString() + " - " + method.toString() + " " + uri);
    this.method = method;
    this.uri = uri;
    this.response = response;
    this.data = data;
  }

  @Override
  public HttpResponseStatus getStatus() {
    return this.response.status();
  }
}
