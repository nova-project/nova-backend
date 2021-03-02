package net.getnova.framework.api.data.response;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import reactor.core.publisher.Flux;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public final class ApiFluxData extends ApiResponse {

  private final Flux<?> data;

  ApiFluxData(final HttpResponseStatus status, final Flux<?> data) {
    super(status);
    this.data = data;
  }
}
