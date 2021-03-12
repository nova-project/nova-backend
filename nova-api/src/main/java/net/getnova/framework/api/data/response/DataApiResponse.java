package net.getnova.framework.api.data.response;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DataApiResponse implements ApiResponse {

  private final HttpResponseStatus status;
  private final Object data;
}
