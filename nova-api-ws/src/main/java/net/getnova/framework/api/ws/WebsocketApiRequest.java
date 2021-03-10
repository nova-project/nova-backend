package net.getnova.framework.api.ws;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.netty.handler.codec.http.HttpMethod;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.getnova.framework.api.data.AbstractApiRequest;

@RequiredArgsConstructor
@EqualsAndHashCode
public class WebsocketApiRequest extends AbstractApiRequest {

  @JsonDeserialize(using = HttpMethodDeserializer.class)
  private final HttpMethod method;
  private final String path;

  //  @JsonRawValue
  private final JsonNode data;

  @Setter
  @JsonIgnore
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
  public <T> T getData(final Class<T> clazz) /*throws ParserApiException*/ {
    return this.objectMapper.convertValue(this.data, clazz);
  }
}
