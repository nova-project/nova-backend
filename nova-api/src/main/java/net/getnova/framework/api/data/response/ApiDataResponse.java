package net.getnova.framework.api.data.response;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public final class ApiDataResponse extends ApiResponse {

  private final Object data;

  ApiDataResponse(final HttpResponseStatus status, final Object data) {
    super(status);
    this.data = data;
  }
}
