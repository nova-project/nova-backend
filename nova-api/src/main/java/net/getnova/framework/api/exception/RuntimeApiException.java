package net.getnova.framework.api.exception;

public class RuntimeApiException extends RuntimeException {

  public RuntimeApiException(final String message) {
    super(message);
  }

  public RuntimeApiException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public RuntimeApiException(final Throwable cause) {
    super(cause);
  }
}
