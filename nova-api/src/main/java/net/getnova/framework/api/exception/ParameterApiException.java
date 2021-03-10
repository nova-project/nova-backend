package net.getnova.framework.api.exception;

import io.netty.handler.codec.http.HttpResponseStatus;
import net.getnova.framework.api.data.ApiResponse;
import net.getnova.framework.api.data.ToApiResponse;

public class ParameterApiException extends ApiException implements ToApiResponse {

  public ParameterApiException(final String message) {
    super(message);
  }

  public ParameterApiException(final String message, final String target) {
    super(message + "__" + target);
  }

  public ParameterApiException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public ParameterApiException(final String message, final String target, final Throwable cause) {
    super(message + "__" + target, cause);
  }

  @Override
  public ApiResponse toApiResponse() {
    return ApiResponse.of(HttpResponseStatus.BAD_REQUEST, this.getMessage());
  }
}
