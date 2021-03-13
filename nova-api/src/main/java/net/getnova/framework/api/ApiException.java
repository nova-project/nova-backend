package net.getnova.framework.api;

import io.netty.handler.codec.http.HttpResponseStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.getnova.framework.api.data.response.ApiError;
import net.getnova.framework.api.data.response.ApiResponse;
import net.getnova.framework.api.data.response.ErrorApiResponse;

/**
 * An {@link ApiException} is a {@link RuntimeException} witch wraps an {@link ErrorApiResponse}.
 *
 * @see RuntimeException
 * @see ErrorApiResponse
 * @see ApiError
 */
@Data
@SuppressWarnings("PMD.MissingSerialVersionUID")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiException extends RuntimeException {

  private final ErrorApiResponse response;

  private ApiException(final ErrorApiResponse response, final Throwable cause) {
    super(cause);
    this.response = response;
  }

  /**
   * Wraps an {@link ErrorApiResponse} in an {@link ApiException}.
   *
   * @param response the {@link ErrorApiResponse} which should be wrapped.
   * @return a {@link ErrorApiResponse} wrapped in a {@link ApiException}.
   */
  public static ApiException of(final ErrorApiResponse response) {
    return new ApiException(response);
  }

  /**
   * Wraps an {@link ErrorApiResponse} in an {@link ApiException} and uses the provided {@link Throwable} as patent for
   * the created {@link ApiException}.
   *
   * @param response the {@link ErrorApiResponse} which should be wrapped.
   * @param cause    the {@link Throwable} witch should be the parent of the {@link ApiException}.
   * @return an {@link ErrorApiResponse} wrapped in an {@link ApiException}.
   * @see Throwable#getCause()
   */
  public static ApiException of(final ErrorApiResponse response, final Throwable cause) {
    return new ApiException(response, cause);
  }

  /**
   * Wraps an {@link ApiError}'s in combination with a {@link HttpResponseStatus} in an {@link ApiException}.
   *
   * @param status the {@link HttpResponseStatus} witch should be used.
   * @param errors a array of {@link ApiError}'s which should be wrapped.
   * @return an {@link ErrorApiResponse} wrapped in an {@link ApiException}.
   */
  public static ApiException of(final HttpResponseStatus status, final ApiError... errors) {
    return new ApiException((ErrorApiResponse) ApiResponse.of(status, errors));
  }

  /**
   * Wraps an {@link ApiError}'s in combination with a {@link HttpResponseStatus} in an {@link ApiException}and uses the
   * provided {@link Throwable} as patent for the created {@link ApiException}.
   *
   * @param status the {@link HttpResponseStatus} witch should be used.
   * @param cause  the {@link Throwable} witch should be the parent of the {@link ApiException}.
   * @param errors a array of {@link ApiError}'s which should be wrapped.
   * @return an {@link ErrorApiResponse} wrapped in an {@link ApiException}.
   * @see Throwable#getCause()
   */
  public static ApiException of(final HttpResponseStatus status, final Throwable cause, final ApiError... errors) {
    return new ApiException((ErrorApiResponse) ApiResponse.of(status, errors), cause);
  }

  @Override
  public String getMessage() {
    return this.response.toString();
  }
}
