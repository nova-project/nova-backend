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
public class ApiResponse {

  private final HttpResponseStatus status;

  protected ApiResponse(final HttpResponseStatus status) {
    this.status = status;
  }

  public static ApiResponse of(final HttpResponseStatus status) {
    return new ApiResponse(status);
  }

  public static ApiResponse of(final String message) {
    return new ApiMessageResponse(HttpResponseStatus.OK, message);
  }

  public static ApiResponse of(final String target, final String message) {
    return new ApiMessageResponse(HttpResponseStatus.OK, message + "__" + target);
  }

  public static ApiResponse of(final HttpResponseStatus status, final String message) {
    return new ApiMessageResponse(status, message);
  }

  public static ApiResponse of(final HttpResponseStatus status, final String message, final String target) {
    return new ApiMessageResponse(status, message + "__" + target);
  }

  public static ApiResponse of(final Flux<?> data) {
    return new ApiFluxData(HttpResponseStatus.OK, data);
  }

  public static ApiResponse of(final HttpResponseStatus status, final Flux<?> data) {
    return new ApiFluxData(status, data);
  }

  public static ApiResponse of(final Object data) {
    return new ApiDataResponse(HttpResponseStatus.OK, data);
  }

  public static ApiResponse of(final HttpResponseStatus status, final Object data) {
    return new ApiDataResponse(status, data);
  }
}
