package net.getnova.framework.cdn;

import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import lombok.RequiredArgsConstructor;
import reactor.netty.http.server.HttpServerRequest;

@RequiredArgsConstructor
class CdnHttpRequest implements HttpRequest {

  private final HttpServerRequest request;

  @Override
  @Deprecated
  public HttpMethod getMethod() {
    return this.request.method();
  }

  @Override
  public HttpMethod method() {
    return this.request.method();
  }

  @Override
  public HttpRequest setMethod(final HttpMethod method) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  public String getUri() {
    return this.request.uri();
  }

  @Override
  public String uri() {
    return this.request.uri();
  }

  @Override
  public HttpRequest setUri(final String uri) {
    throw new UnsupportedOperationException();
  }

  @Override
  @Deprecated
  public HttpVersion getProtocolVersion() {
    return this.request.version();
  }

  @Override
  public HttpVersion protocolVersion() {
    return this.request.version();
  }

  @Override
  public HttpRequest setProtocolVersion(final HttpVersion version) {
    throw new UnsupportedOperationException();
  }

  @Override
  public HttpHeaders headers() {
    return this.request.requestHeaders();
  }

  @Override
  @Deprecated
  public DecoderResult getDecoderResult() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setDecoderResult(final DecoderResult result) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DecoderResult decoderResult() {
    throw new UnsupportedOperationException();
  }
}
