package net.getnova.framework.api.exception;

import net.getnova.framework.api.data.ApiResponse;
import net.getnova.framework.api.data.ToApiResponse;

public class ResponseApiException extends ApiException implements ToApiResponse {

  private final ApiResponse response;

  public ResponseApiException(final ApiResponse response) {
    this.response = response;
  }

  public ResponseApiException(final ApiResponse response, final Throwable cause) {
    super(cause);
    this.response = response;
  }

  @Override
  public ApiResponse toApiResponse() {
    return this.response;
  }
}
