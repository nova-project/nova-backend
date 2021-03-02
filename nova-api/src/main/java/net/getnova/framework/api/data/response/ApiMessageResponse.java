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
public final class ApiMessageResponse extends ApiResponse {

  private final String message;

  ApiMessageResponse(final HttpResponseStatus status, final String message) {
    super(status);
    this.message = message;
  }
}
