package net.getnova.framework.api.exception;

public class PathApiException extends RuntimeApiException {

  public PathApiException(final String message) {
    super(message);
  }

  public PathApiException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public PathApiException(final Throwable cause) {
    super(cause);
  }
}
