package net.getnova.framework.web;

import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import reactor.netty.ByteBufMono;

public abstract class HttpStatusException extends Exception {

  public HttpStatusException(final String message) {
    super(message);
  }

  public abstract HttpMethod getMethod();

  public abstract String getUri();

  public abstract HttpResponseStatus getStatus();

  public abstract ByteBufMono getData();
}
