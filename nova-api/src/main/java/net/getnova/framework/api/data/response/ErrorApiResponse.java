package net.getnova.framework.api.data.response;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ErrorApiResponse implements ApiResponse {

  private final HttpResponseStatus status;
  private final ApiError[] errors;
}
