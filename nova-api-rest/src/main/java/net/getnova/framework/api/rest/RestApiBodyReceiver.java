package net.getnova.framework.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import net.getnova.framework.core.JsonUtils;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;

@Data
final class RestApiBodyReceiver {

  private final ObjectMapper objectMapper;
  private final HttpServerRequest request;

  <T> Mono<T> receive(final Class<T> clazz) {
    return JsonUtils.readValue(this.objectMapper, this.request.receive().aggregate(), clazz);
  }
}
