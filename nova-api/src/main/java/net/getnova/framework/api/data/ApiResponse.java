package net.getnova.framework.api.data;

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

  private ApiResponse(final HttpResponseStatus status) {
    this.status = status;
  }

  public static ApiResponse of(final HttpResponseStatus status) {
    return new ApiResponse(status);
  }

  public static ApiResponse of(final String message) {
    return new MessageResponse(HttpResponseStatus.OK, message);
  }

  public static ApiResponse of(final String target, final String message) {
    return new TargetMessageResponse(HttpResponseStatus.OK, target, message);
  }

  public static ApiResponse of(final HttpResponseStatus status, final String message) {
    return new MessageResponse(status, message);
  }

  public static ApiResponse of(final HttpResponseStatus status, final String target, final String message) {
    return new TargetMessageResponse(status, target, message);
  }

  public static ApiResponse of(final Flux<Object> data) {
    return new FluxDataResponse(HttpResponseStatus.OK, data);
  }

  public static ApiResponse of(final HttpResponseStatus status, final Flux<Object> data) {
    return new FluxDataResponse(status, data);
  }

  public static ApiResponse of(final Object data) {
    return new DataResponse(HttpResponseStatus.OK, data);
  }

  public static ApiResponse of(final HttpResponseStatus status, final Object data) {
    return new DataResponse(status, data);
  }

  @Getter
  @Setter
  @ToString
  @EqualsAndHashCode
  public static final class DataResponse extends ApiResponse {

    private final Object data;

    DataResponse(final HttpResponseStatus status, final Object data) {
      super(status);
      this.data = data;
    }
  }

  @Getter
  @Setter
  @ToString
  @EqualsAndHashCode
  public static final class FluxDataResponse extends ApiResponse {

    private final Flux<Object> data;

    FluxDataResponse(final HttpResponseStatus status, final Flux<Object> data) {
      super(status);
      this.data = data;
    }
  }

  @Getter
  @Setter
  @ToString
  @EqualsAndHashCode
  public static class MessageResponse extends ApiResponse {

    private final String message;

    MessageResponse(final HttpResponseStatus status, final String message) {
      super(status);
      this.message = message;
    }
  }

  @Getter
  @Setter
  @ToString
  @EqualsAndHashCode
  public static final class TargetMessageResponse extends MessageResponse {

    private final String target;

    TargetMessageResponse(final HttpResponseStatus status, final String target, final String message) {
      super(status, message);
      this.target = target;
    }
  }
}
