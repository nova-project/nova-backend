package net.getnova.framework.api.ws;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.netty.handler.codec.http.HttpMethod;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.getnova.framework.api.data.AbstractApiRequest;
import net.getnova.framework.api.exception.ParserApiException;
import net.getnova.framework.api.exception.RuntimeApiException;

@RequiredArgsConstructor
@EqualsAndHashCode
public class WebsocketApiRequest extends AbstractApiRequest {

  @JsonDeserialize(using = HttpMethodDeserializer.class)
  private final HttpMethod method;
  private final String path;
  private final String data;
  @Setter
  private ObjectMapper objectMapper;

  @Override
  public HttpMethod getMethod() {
    return this.method;
  }

  @Override
  public String getPath() {
    return this.path;
  }

  @Override
  public <T> T getData(final Class<T> clazz) throws ParserApiException {
    try {
      return this.objectMapper.readValue(this.data, clazz);
    }
    catch (JsonParseException e) {
      throw new ParserApiException(e);
    }
    catch (IOException e) {
      throw new RuntimeApiException(e);
    }
  }
}
