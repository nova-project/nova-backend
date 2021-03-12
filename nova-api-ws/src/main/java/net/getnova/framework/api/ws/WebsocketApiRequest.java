package net.getnova.framework.api.ws;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.netty.handler.codec.http.HttpMethod;
import java.io.IOException;
import java.util.List;
import lombok.EqualsAndHashCode;
import net.getnova.framework.api.ApiUtils;
import net.getnova.framework.api.data.request.AbstractApiRequest;
import net.getnova.framework.core.JsonRawDeserializer;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@EqualsAndHashCode
public class WebsocketApiRequest extends AbstractApiRequest {

  private final HttpMethod method;
  private final String path;
  private final String tag;
  private final String data;
  private ObjectMapper objectMapper;

  public WebsocketApiRequest(
    @JsonProperty("method") @JsonDeserialize(using = HttpMethodDeserializer.class) final HttpMethod method,
    @JsonProperty("path") final String path,
    @JsonProperty("tag") final String tag,
    @JsonProperty("data") @JsonDeserialize(using = JsonRawDeserializer.class) final String data
  ) {
    this.method = method;
    this.path = path;
    this.tag = tag;
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
  public MultiValueMap<String, String> getQueryVariables() {
    throw new UnsupportedOperationException("currently not for websockets");
  }

  @Override
  public List<String> getQueryVariable(final String name) {
    throw new UnsupportedOperationException("currently not for websockets");
  }

  public String getTag() {
    return this.tag;
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
