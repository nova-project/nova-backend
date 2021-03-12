package net.getnova.framework.api.data.response;

import io.netty.handler.codec.http.HttpResponseStatus;

public interface ApiResponse {

  static ApiResponse of(final HttpResponseStatus status) {
    return new DefaultApiResponse(status);
  }

  static <T> ApiResponse of(final T data) {
    return new DataApiResponse(HttpResponseStatus.OK, data);
  }

  static <T> ApiResponse of(final HttpResponseStatus status, final T data) {
    return new DataApiResponse(status, data);
  }

  static ApiResponse of(final HttpResponseStatus status, final ApiError... errors) {
    return new ErrorApiResponse(status, errors);
  }

  static ApiResponse of(final HttpResponseStatus status, final String target, final String message) {
    return new ErrorApiResponse(status, new ApiError[]{ApiError.of(target, message)});
  }

  HttpResponseStatus getStatus();
}
