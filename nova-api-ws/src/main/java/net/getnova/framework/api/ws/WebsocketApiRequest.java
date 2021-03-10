package net.getnova.framework.api.ws;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.netty.handler.codec.http.HttpMethod;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import net.getnova.framework.api.ApiUtils;
import net.getnova.framework.api.data.AbstractApiRequest;
import net.getnova.framework.core.JsonRawDeserializer;
import reactor.core.publisher.Mono;

@EqualsAndHashCode
public class WebsocketApiRequest extends AbstractApiRequest {

  private final HttpMethod method;
  private final String path;
  private final String data;
  private ObjectMapper objectMapper;

  public WebsocketApiRequest(
    @JsonProperty("method") @JsonDeserialize(using = HttpMethodDeserializer.class) HttpMethod method,
    @JsonProperty("path") String path,
    @JsonProperty("data") @JsonDeserialize(using = JsonRawDeserializer.class) String data
  ) {
    this.method = method;
    this.path = path;
    this.data = data;
  }

  @Override
  public HttpMethod getMethod() {
    return this.method;
  }

  @Override
  public String getPath() {
    return this.path;
  }

  @Override
  public <T> Mono<T> getData(final Class<T> clazz) {
    try {
      return Mono.just(this.objectMapper.readValue(this.data, clazz));
    }
    catch (IOException e) {
      return Mono.error(ApiUtils.mapJsonError(e));
    }
  }

  @JsonIgnore
  public void setObjectMapper(final ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }
}
