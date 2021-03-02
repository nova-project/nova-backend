package net.getnova.framework.api.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpMethod;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.api.data.AbstractApiRequest;
import net.getnova.framework.api.exception.ParserApiException;
import net.getnova.framework.api.exception.RuntimeApiException;
import net.getnova.framework.web.server.http.HttpUtils;
import net.getnova.framework.web.server.http.route.HttpRoute;
import reactor.netty.http.server.HttpServerRequest;

@RequiredArgsConstructor
@EqualsAndHashCode
public class RestApiRequest extends AbstractApiRequest {

  private final ObjectMapper objectMapper;

  private final HttpRoute route;
  private final HttpServerRequest request;
  private final byte[] body;

  @Override
  public HttpMethod getMethod() {
    return this.request.method();
  }

  @Override
  public String getPath() {
    return this.route.getPath(this.request);
  }

  @Override
  public <T> T getData(final Class<T> clazz) throws ParserApiException {
    if (this.body.length == 0) {
      return null;
    }

    final Optional<Charset> charset = HttpUtils.getCharset(this.request.requestHeaders());

    try {
      // TODO: Optional
      return charset.isPresent()
        ? this.objectMapper.readValue(new String(this.body, charset.get()), clazz)
        : this.objectMapper.readValue(this.body, clazz);
    }
    catch (JsonParseException e) {
      throw new ParserApiException(e);
    }
    catch (IOException e) {
      throw new RuntimeApiException(e);
    }
  }
}
