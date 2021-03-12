package net.getnova.framework.api.data.response;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.Data;

@Data
public class DefaultApiResponse implements ApiResponse {

  private final HttpResponseStatus status;

  @Override
  public HttpResponseStatus getStatus() {
    return this.status;
  }
}
