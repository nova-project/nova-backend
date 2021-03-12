package net.getnova.framework.api;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.api.data.response.ApiError;
import net.getnova.framework.api.data.response.ApiResponse;
import net.getnova.framework.api.data.response.ErrorApiResponse;

@Data
@SuppressWarnings("PMD.MissingSerialVersionUID")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiException extends RuntimeException {

  private final ErrorApiResponse response;

  private ApiException(final ErrorApiResponse response, final Throwable cause) {
    super(cause);
    this.response = response;
  }

  public static ApiException of(final ErrorApiResponse response) {
    return new ApiException(response);
  }

  public static ApiException of(final ErrorApiResponse response, final Throwable cause) {
    return new ApiException(response, cause);
  }

  public static ApiException of(final HttpResponseStatus status, final ApiError... errors) {
    return new ApiException((ErrorApiResponse) ApiResponse.of(status, errors));
  }

  public static ApiException of(final HttpResponseStatus status, final Throwable cause, final ApiError... errors) {
    return new ApiException((ErrorApiResponse) ApiResponse.of(status, errors), cause);
  }

  @Override
  public String getMessage() {
    return this.response.toString();
  }
}
