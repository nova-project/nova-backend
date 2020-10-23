package net.getnova.backend.cdn;

import io.netty.handler.codec.DecoderResult;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpVersion;
import lombok.RequiredArgsConstructor;
import reactor.netty.http.server.HttpServerRequest;

@RequiredArgsConstructor
public class CdnHttpRequest implements HttpRequest {

  private final HttpServerRequest request;

  @Override
  public HttpMethod getMethod() {
    return this.request.method();
  }

  @Override
  public HttpMethod method() {
    return this.request.method();
  }

  @Override
  public HttpRequest setMethod(HttpMethod method) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getUri() {
    return this.request.uri();
  }

  @Override
  public String uri() {
    return this.request.uri();
  }

  @Override
  public HttpRequest setUri(String uri) {
    throw new UnsupportedOperationException();
  }

  @Override
  public HttpVersion getProtocolVersion() {
    return this.request.version();
  }

  @Override
  public HttpVersion protocolVersion() {
    return this.request.version();
  }

  @Override
  public HttpRequest setProtocolVersion(HttpVersion version) {
    throw new UnsupportedOperationException();
  }

  @Override
  public HttpHeaders headers() {
    return this.request.requestHeaders();
  }

  @Override
  public DecoderResult getDecoderResult() {
    throw new UnsupportedOperationException();
  }

  @Override
  public DecoderResult decoderResult() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void setDecoderResult(DecoderResult result) {
    throw new UnsupportedOperationException();
  }
}
